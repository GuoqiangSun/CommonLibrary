package cn.com.swain.support.protocolEngine.datagram.ProtocolException;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class UnknownVersionException extends RuntimeException {


    public UnknownVersionException(String name) {
        super(name);
    }

    public UnknownVersionException(String name, Throwable cause) {
        super(name, cause);
    }

    public UnknownVersionException(Exception cause) {
        super(cause);
    }
}
