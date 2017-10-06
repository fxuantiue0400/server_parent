package xx.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public class ProtocalDecoder extends ByteToMessageDecoder {
    Logger logger = LoggerFactory.getLogger(ProtocalDecoder.class.getName());
    public static final ByteBuf HEAD_FLAG = Unpooled.copyInt(0x77aa77aa);
    public static final int TAIL_FLAG = 0x77ab77ab;

    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        {
            byteBuf.markReaderIndex();
            int headIndex = headIndex(byteBuf);
            while (-1 != headIndex) {
                //读取长度,并且尾不是HEAD
                if (byteBuf.readableBytes() - headIndex > 4) {
                    int len = byteBuf.readInt();
                    //当前包不完整,完整的包包括包头+len+frame+tail
                    if (byteBuf.readableBytes() < (len + 4 + 4 + 4)) {
                        byteBuf.resetReaderIndex();
                        return;
                    }
                    //读取包体
                    ByteBuf frame = ctx.alloc().buffer(len);
                    byteBuf.readBytes(frame);
                    int tail = byteBuf.readInt();
                    //包尾不对，表示当前包是错误的，丢弃
                    if(tail != TAIL_FLAG){
                        logger.info("读到了完整的head+len+frame+tail,但是包尾错误，丢弃!");
                        byteBuf.discardReadBytes();
                    }else{
                        logger.info("读取到了正确的包，交由handle处理");
                        out.add(frame);
                    }
                }

            }

        }
    }


    private int headIndex(ByteBuf byteBuf) {
        for (int i = byteBuf.readerIndex(); i < byteBuf.writerIndex(); i++) {
            //索引字符，找到对应的字节，外层循环移位
            int findIndex = i;
            int headIndex;
            for (headIndex = 0; headIndex < HEAD_FLAG.capacity(); headIndex++) {
                //每次都进行比较字节
                if (byteBuf.getByte(findIndex) != HEAD_FLAG.getByte(headIndex)) {
                    break;
                } else {
                    //找到对应的字节
                    findIndex++;
                    //找到一个完整的包
                    if (headIndex == HEAD_FLAG.capacity() - 1) {
                        return i - byteBuf.readerIndex();
                    }
                    //当查找索引等于最后，并且HEAD没有完整
                    if (findIndex == byteBuf.writerIndex() && findIndex != HEAD_FLAG.capacity() - 1) {
                        return -1;
                    }

                }
            }

        }

        return -1;
    }
}
