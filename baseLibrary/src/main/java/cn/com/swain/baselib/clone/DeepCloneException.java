package cn.com.swain.baselib.clone;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class DeepCloneException extends RuntimeException {


    public DeepCloneException(String name) {
        super(name);
    }

    public DeepCloneException(String name, Throwable cause) {
        super(name, cause);
    }

    public DeepCloneException(Exception cause) {
        super(cause);
    }
}
