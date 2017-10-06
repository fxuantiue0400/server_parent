package xx;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/7/18.
 */
public class ResourceFactory {

    private final ConcurrentHashMap<Type, ByteToMessageDecoder> decoders = new ConcurrentHashMap();

    private final ConcurrentHashMap<Type, MessageToByteEncoder> encoders = new ConcurrentHashMap();
    /**
     * 检查资源名是否合法
     * <blockquote><pre>
     * name规则:
     *    1: "$"有特殊含义, 不能表示"$"资源本身
     *    2: 只能是字母、数字、(短横)-、(下划线)_、点(.)的组合
     * </pre></blockquote>
     *
     * @param name String
     */
    public void checkName(String name) {
        if (name == null || (!name.isEmpty() && !name.matches("^[a-zA-Z0-9_;\\-\\.\\[\\]\\(\\)]+$"))) {
            throw new IllegalArgumentException("Resource.name(" + name + ") contains illegal character, must be (a-z,A-Z,0-9,_,.,(,),-,[,])");
        }
    }

    /**
     *
     * @param clazz
     * @param e
     * @param <E>
     */
    public final <E extends ByteToMessageDecoder> void register(final Type clazz, E e) {
        decoders.put(clazz, e);
    }


    public final <E extends MessageToByteEncoder> void register(final Type clazz, E e) {
        encoders.put(clazz, e);
    }
}
