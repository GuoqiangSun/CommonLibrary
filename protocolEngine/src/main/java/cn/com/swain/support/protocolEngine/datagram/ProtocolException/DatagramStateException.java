package cn.com.swain.support.protocolEngine.datagram.ProtocolException;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class DatagramStateException extends RuntimeException {


    public DatagramStateException(String name) {
        super(name);
    }

    public DatagramStateException(String name, Throwable cause) {
        super(name, cause);
    }

    public DatagramStateException(Exception cause) {
        super(cause);
    }
}
