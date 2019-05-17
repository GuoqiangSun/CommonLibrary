package cn.com.swain.baselib.jsInterface.request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.AbsJsInterface;
import cn.com.swain.baselib.jsInterface.request.bean.BaseCommonJsRequestBean;
import cn.com.swain.baselib.jsInterface.response.BaseCommonJsUtils;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public abstract class AbsCommonJsInterfaceProxy
        extends AbsCommonJsInterface {

    public AbsCommonJsInterfaceProxy(String name, AbsJsInterface mJsInterface) {
        super(name, mJsInterface);
    }

    public AbsCommonJsInterfaceProxy(String name) {
        super(name);
    }


    @Override
    protected void handleJsRequest(String jsonData,JSONObject json) {

        String key;
        try {
            key = json.getString(BaseCommonJsUtils.KEY_MSG_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
            onJsDataParseError(e, jsonData);
            return;
        }

        final BaseCommonJsRequestBean mData = new BaseCommonJsRequestBean();
        mData.setRootJsonObj(json);
        mData.setRootJsonStr(jsonData);
        mData.setMsgType(key);

        switch (mData.getMsgType()) {
            case BaseCommonJsUtils.TYPE_REQUEST_BACK:

                onJsPressBack();

                break;
        }

        onJsRequest(mData);
    }

    protected abstract void onJsRequest(BaseCommonJsRequestBean mData);

    protected abstract void onJsPressBackFinish();

    protected abstract void onJsPressBackFinishBefore();


    private long mLastFinishTs;
    private int mFinishTimes = 0;
    private static final int FINISH_DELAY = 1000 * 2;
    private static final int MAX_FINISH_PRESS_COUNT = 2;

    private void onJsPressBack() {

        long diff = System.currentTimeMillis() - mLastFinishTs;
        if (diff > 0 && diff <= FINISH_DELAY && ++mFinishTimes >= MAX_FINISH_PRESS_COUNT) {
            // finish
            mFinishTimes = 0;

            onJsPressBackFinish();

        } else if (Math.abs(diff) > FINISH_DELAY) {
            mFinishTimes = 1;
        }

        if ((MAX_FINISH_PRESS_COUNT - mFinishTimes) == 1) {
            onJsPressBackFinishBefore();
        }

        mLastFinishTs = System.currentTimeMillis();

    }


}
