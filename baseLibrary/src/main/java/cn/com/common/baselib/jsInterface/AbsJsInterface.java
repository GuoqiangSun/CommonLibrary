package cn.com.common.baselib.jsInterface;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/5 0005
 * desc :
 */
public abstract class AbsJsInterface {

    public static final String TAG = "appJs";

    private final String name;

    public String getName() {
        return name;
    }

    public AbsJsInterface(String name) {

        if (name == null) {
            throw new IllegalArgumentException(" name must not be null ");
        }

        this.name = name;
    }

    public void release() {

    }

    public AbsJsInterface getJsInterface() {
        return this;
    }

}
