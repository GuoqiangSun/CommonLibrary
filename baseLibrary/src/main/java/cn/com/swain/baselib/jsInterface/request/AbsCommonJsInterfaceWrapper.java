package cn.com.swain.baselib.jsInterface.request;

import cn.com.swain.baselib.jsInterface.AbsJsInterface;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public class AbsCommonJsInterfaceWrapper extends AbsJsInterface {

    private AbsJsInterface mJsInterface;

    AbsCommonJsInterfaceWrapper(String name) {
        super(name);
    }

    AbsCommonJsInterfaceWrapper(String name, AbsJsInterface mJsInterface) {
        super(name);
        this.mJsInterface = mJsInterface;
    }

    protected void attachAbsJsInterface(AbsJsInterface mJsInterface) {
        if (this.mJsInterface != null) {
            throw new IllegalArgumentException(" mJsInterface is not null ");
        }
        this.mJsInterface = mJsInterface;
    }

    @Override
    public String getName() {
        if (mJsInterface == null) {
            return null;
        }
        return mJsInterface.getName();
    }

    @Override
    public void release() {
        if (mJsInterface != null) {
            mJsInterface.release();
        }
    }

    @Override
    public AbsJsInterface getJsInterface() {
        if (mJsInterface == null) {
            return null;
        }
        return mJsInterface.getJsInterface();
    }

}
