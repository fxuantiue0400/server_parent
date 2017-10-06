package xx.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xx.codec.Packet;

/**
 * Created by Administrator on 2017/8/23/023.
 */
public class NettyConnection implements Connection,ChannelFutureListener {

    private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);


    private SessionContext context;
    private Channel channel;
    private volatile byte status = CONNECT_STATUS_NEW;
    private long lastReadTime;
    private long lastWriteTime;

    @Override
    public void init(Channel channel) {
        this.channel = channel;
        this.lastReadTime = System.currentTimeMillis();
        this.lastWriteTime = System.currentTimeMillis();
        this.status = CONNECT_STATUS_CONNECTED;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
            lastWriteTime = System.currentTimeMillis();
        } else {
            logger.error("connection send msg error", channelFuture.cause());
        }
    }

    @Override
    public SessionContext getSessionContext() {
        return context;
    }

    @Override
    public void setSessionContext(SessionContext context) {
        this.context = context;
    }

    @Override
    public ChannelFuture send(Packet packet) {
        return send(packet, null);
    }

    @Override
    public ChannelFuture send(Packet packet, ChannelFutureListener listener) {
        if(channel.isActive()){
            ChannelFuture future = channel.writeAndFlush(packet).addListener(this);

            if (listener != null) {
                future.addListener(listener);
            }

            if (channel.isWritable()) {
                return future;
            }
        }else{
            logger.error("channel已经关闭，不能进行发送{}", this);
        }
        return null;
    }


    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public ChannelFuture close() {
        if(channel!=null){
            return channel.close();
        }
        return null;
    }


    @Override
    public String toString() {
        return "NettyConnection{" +
                "context=" + context +
                '}';
    }
}
