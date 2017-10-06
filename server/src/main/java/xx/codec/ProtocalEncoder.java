package xx.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xx.ProtocalMessage;


/**
 * Created by Administrator on 2017/7/14.
 */
public class ProtocalEncoder extends MessageToByteEncoder<ProtocalMessage>{

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocalMessage msg, ByteBuf out) throws Exception {

        byte[] body = msg.getBody();
        short messageLen = (short) (body.length + ProtocalMessage.HEAD_TAG + ProtocalMessage.MSGLEN_POSITION + 4 + 2);
        msg.setMsgLen(messageLen);
        ByteBuf buffer = Unpooled.buffer(ProtocalMessage.HEADER_LEN + body.length);
        buffer.writeInt(ProtocalMessage.HEAD_TAG);
        buffer.writeShort(messageLen);
        buffer.writeInt(msg.getSequence());
        buffer.writeShort(msg.getCommandID());
        buffer.writeBytes(body);
        out.writeBytes(buffer);
    }
}
