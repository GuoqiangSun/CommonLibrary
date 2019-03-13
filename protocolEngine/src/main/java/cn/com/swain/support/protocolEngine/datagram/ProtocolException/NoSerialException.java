package cn.com.swain.support.protocolEngine.datagram.ProtocolException;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class NoSerialException extends RuntimeException {


    public NoSerialException(String name) {
        super(name);
    }

    public NoSerialException(String name, Throwable cause) {
        super(name, cause);
    }

    public NoSerialException(Exception cause) {
        super(cause);
    }
}
