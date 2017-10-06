package xx.connection;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2017/8/23/023.
 */
public interface ConnectionManager {

    Connection get(Channel channel);

    void add(Connection connection);

    Connection remove(Channel channel);

    int count();

    void init();

    void destroy();
}
