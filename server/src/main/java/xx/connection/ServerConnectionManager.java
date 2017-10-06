package xx.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2017/8/23/023.
 *
 * 服务端连接管理
 */
public class ServerConnectionManager implements ConnectionManager{

    private final ConcurrentMap<ChannelId, Connection> connections = new ConcurrentHashMap<>();

    private ServerConnectionManager(){

    }

    public ServerConnectionManager getInstance(){

        return SingletonHolder.connectManager;

    }

    @Override
    public Connection get(Channel channel) {
        return connections.get(channel);
    }

    @Override
    public void addConnect(Connection connection) {
        connections.putIfAbsent(connection.getChannel().id(), connection);
    }

    @Override
    public Connection remove(Channel channel) {
        return connections.remove(channel);
    }

    @Override
    public int count() {
        return connections.size();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        connections.values().forEach(Connection::close);
        connections.clear();
    }

    private static class SingletonHolder{

        private static final ServerConnectionManager connectManager = new ServerConnectionManager();

    }
}
