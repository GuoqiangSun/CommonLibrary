package cn.com.swain.support.protocolEngine.datagram.ProtocolException;

import java.io.IOException;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class EscapeIOException extends IOException {

    public EscapeIOException(String name) {
        super(name);
    }

    public EscapeIOException(String name, Throwable cause) {
        super(name, cause);
    }

    public EscapeIOException(Exception cause) {
        super(cause);
    }
}
