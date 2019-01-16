package cn.com.swain.baselib.jsInterface.response;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/28 0028
 * desc :
 */
public class BaseResponseMethod {

    protected final BaseCommonJsResponseBean mBean;

    protected final CommonJsResponseUtils mResponse;

    public BaseResponseMethod() {
        this(null);
    }

    public BaseResponseMethod(String msgType) {
        mBean = new BaseCommonJsResponseBean();
        mResponse = new CommonJsResponseUtils();
        mBean.setMsgType(msgType);
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

    @Override
    public String toString() {
        return toMethod();
    }
}
