package xx.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xx.ConstDef;
import xx.codec.ProtocalDecoder;
import xx.codec.ProtocalEncoder;
import xx.handler.ProtocalHandler;


/**
 * Created by Administrator on 2017/7/14.
 */
public class ProtocalServer extends AbstractServer<ProtocalEncoder, ProtocalDecoder, ProtocalHandler> {

    private Logger logger = LoggerFactory.getLogger(ProtocalServer.class.getName());

    public ProtocalServer() {
        super(ConstDef.SERVER_ID_PROTOCAL, ConstDef.SERVER_PROTOCAL_PORT, System.currentTimeMillis(), new ProtocalEncoder(), new ProtocalDecoder(), new ProtocalHandler());
        logger.info("服务{}，端口:{}.正在启动...",ConstDef.SERVER_ID_PROTOCAL, ConstDef.SERVER_PROTOCAL_PORT);
    }


}
