package xx.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xx.codec.Packet;
import xx.codec.PacketReceiver;
import xx.connection.Connection;
import xx.connection.ConnectionManager;
import xx.connection.NettyConnection;

/**
 * Created by Administrator on 2017/7/14.
 */
@ChannelHandler.Sharable
public class ProtocalHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LoggerFactory.getLogger(ProtocalHandler.class);

    private final ConnectionManager connectionManager;
    private final PacketReceiver receiver;

    public ProtocalHandler(ConnectionManager connectionManager, PacketReceiver receiver) {

        this.connectionManager = connectionManager;
        this.receiver = receiver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        receiver.onReceive(packet, connection);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel());
        connectionManager.add(connection);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        logger.error("caught an ex, channel={}, conn={}", ctx.channel(), connection, cause);
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        if(connection!=null){
            connection.close();
        }
    }
}
