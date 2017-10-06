package xx.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xx.ConstDef;
import xx.IConnect;
import xx.codec.ProtocalDecoder;
import xx.codec.ProtocalEncoder;
import xx.handler.ProtocalHandler;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/7/14.
 */
public abstract class AbstractServer<E extends MessageToByteEncoder, D extends ByteToMessageDecoder, H extends SimpleChannelInboundHandler> implements IConnect {

    private static Logger logger = LoggerFactory.getLogger(AbstractServer.class.getName());

    public static final String SERVER_ROOT = "SERVER_ROOT";


    //protected final long serverStartTime;

    static final boolean SSL = System.getProperty("ssl") != null;

    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8023"));

    ServerBootstrap serverBootstrap = null;

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup = null;

    int serverId;
    long serverStartTime;
    int port;

    protected AbstractServer(int serverId, int port, long serverStartTime, final E encoder, final D decoder, final H handler) {
        this.serverId = serverId;
        this.serverStartTime = serverStartTime;
        this.port = port;
        Class<? extends ServerSocketChannel> channelClass;

        if (Epoll.isAvailable()) {
            logger.info("Netty is using Epoll");
            bossGroup = new EpollEventLoopGroup(1, new DefaultThreadFactory(ConstDef.BOSS_THREAD_NAME));
            workerGroup = new EpollEventLoopGroup(0, new DefaultThreadFactory(ConstDef.WORK_THREAD_NAME));
            channelClass = EpollServerSocketChannel.class;
        } else {
            logger.info("Netty is using NIO");
            bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(ConstDef.BOSS_THREAD_NAME));
            workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(ConstDef.WORK_THREAD_NAME));
            channelClass = NioServerSocketChannel.class;
        }

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(channelClass).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast("idleStateHandler", new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS))
                        .addLast("decoder", encoder)
                        .addLast("encoder", decoder)
                        .addLast("handler", handler);
            }
        }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_SNDBUF, 1024 * 64)
                .option(ChannelOption.SO_RCVBUF, 1024 * 64)
                .childOption(ChannelOption.SO_LINGER, 0)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);    //开启bytebuf内存池

    }

    private SslContext getSslContext() {
        SslContext sslContext = null;
        if (SSL) {
            SelfSignedCertificate ssc = null;
            try {
                ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (SSLException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } else {
            sslContext = null;
        }
        return sslContext;
    }

    public void init() {
        //TODO 初始化其他信息
    }

    public void start() {
        try {
            serverBootstrap.bind(port).sync();
            logger.info("Listening at port " + port);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Listening at port error. port=" + port, e);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
    }
}
