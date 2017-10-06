package xx.connection;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2017/8/23/023.
 */
public class ClientConnectionManager implements ConnectionManager {
    @Override
    public Connection get(Channel channel) {
        return null;
    }

    @Override
    public void addConnect(Connection connection) {

    }

    @Override
    public Connection remove(Channel channel) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
