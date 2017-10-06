package xx.codec;

import xx.connection.Connection;

/**
 * Created by Administrator on 2017/8/23/023.
 */
public interface PacketReceiver {

    void onReceive(Packet packet, Connection connection);

}
