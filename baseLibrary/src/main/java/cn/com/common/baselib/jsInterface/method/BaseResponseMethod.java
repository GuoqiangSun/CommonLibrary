package cn.com.common.baselib.jsInterface.method;

import cn.com.common.baselib.jsInterface.base.CommonJsResponse;
import cn.com.common.baselib.jsInterface.bean.BaseCommonJsResponseBean;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/28 0028
 * desc :
 */
public class BaseResponseMethod {

    protected final BaseCommonJsResponseBean mBean;

    protected final CommonJsResponse mResponse;

    public BaseResponseMethod() {
        mBean = new BaseCommonJsResponseBean();
        mResponse = new CommonJsResponse();
    }

    public BaseResponseMethod(String msgType) {
        mBean = new BaseCommonJsResponseBean();
        mBean.setMsgType(msgType);
        mResponse = new CommonJsResponse();
    }

    public void releaseCache() {
        mData = null;
        responseJsMethod = null;
    }

    private String mData;
    private String responseJsMethod;

    public String toMethod() {
        if (mData == null) {
            mData = mBean.toJsonStr();
        }
        if (responseJsMethod == null && mData != null) {
            responseJsMethod = mResponse.getResponseJsMethod(mData);
        }
        return responseJsMethod;
    }

}
