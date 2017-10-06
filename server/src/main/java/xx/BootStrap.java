package xx;


import xx.core.ProtocalServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Administrator on 2017/7/14.
 */
public class BootStrap {

    public static void main(String[] args){
        ProtocalServer protocalServer = new ProtocalServer();
        protocalServer.init();
        protocalServer.start();

    }

}
