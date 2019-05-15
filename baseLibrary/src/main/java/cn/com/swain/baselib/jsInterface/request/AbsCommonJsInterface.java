package cn.com.swain.baselib.jsInterface.request;

import cn.com.swain.baselib.jsInterface.AbsJsInterface;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public abstract class AbsCommonJsInterface
        extends AbsCommonJsInterfaceWrapper
        implements IJSRequest {

    public AbsCommonJsInterface(String name) {
        super(name);
    }

    public AbsCommonJsInterface(String name, AbsJsInterface mJsInterface) {
        super(name, mJsInterface);
    }

}
