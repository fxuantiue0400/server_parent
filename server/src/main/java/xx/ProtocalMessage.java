package xx;

import xx.codec.Packet;

/**
 * Created by Administrator on 2017/8/7/007.
 */
public class ProtocalMessage implements Packet{

    public static final int HEADER_LEN = 12;// 包头长度
    public static final int MSGLEN_POSITION = 2;// msgLen字段position偏移量

    /*** 起始标记 */
    public static final int HEAD_TAG = 0x77aa77aa;
    /*** 包体总长度，除了起始标记和长度字段以外的所有字段 */
    private short msgLen;
    /*** 序列号 */
    private int sequence;
    /*** 命令号 */
    private short commandID;
    /*** 包体 */
    private byte[] body;

    public ProtocalMessage(short msgLen, int sequence, short commandID, byte[] body) {
        this.msgLen = msgLen;
        this.sequence = sequence;
        this.commandID = commandID;
        this.body = body;
    }

    public short getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(short msgLen) {
        this.msgLen = msgLen;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public short getCommandID() {
        return commandID;
    }

    public void setCommandID(short commandID) {
        this.commandID = commandID;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
