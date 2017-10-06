package xx.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import xx.codec.Packet;

/**
 * Created by Administrator on 2017/8/23/023.
 */
public interface Connection {
    byte CONNECT_STATUS_NEW = 0;
    byte CONNECT_STATUS_CONNECTED = 1;
    byte CONNECT_STATUS_DISCONNECTED = 2;

    SessionContext getSessionContext();

    void setSessionContext(SessionContext sessionContext);

    ChannelFuture send(Packet packet);

    ChannelFuture send(Packet packet, ChannelFutureListener listener);

    Channel getChannel();

    String getId();

    ChannelFuture close();

    void init(Channel channel);

}
