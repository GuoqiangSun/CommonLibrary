package cn.com.swain.baselib.jsInterface;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/5 0005
 * desc :
 */
public abstract class AbsJsInterface {

    public static final String TAG = "appJs";
    
    private final String name;

    public AbsJsInterface(String name) {

        if (name == null) {
            throw new IllegalArgumentException(" name must not be null ");
        }

        this.name = name;
    }

    /**
     * JS 域名
     * <p>
     * webview.addJavascriptInterface() 用到这个域名
     */
    public String getName() {
        return name;
    }

    /**
     * JS 实例
     * <p>
     * webview.addJavascriptInterface() 用到这个实例
     *
     * @return {@link AbsJsInterface}
     */
    public AbsJsInterface getJsInterface() {
        return this;
    }


    /**
     * 释放资源
     */
    public void release() {

    }

}
