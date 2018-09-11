package cn.com.swain.baselib.jsInterface.base;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.AbsJsInterface;
import cn.com.swain.baselib.jsInterface.bean.BaseCommonJsRequestBean;
import cn.com.swain.baselib.jsInterface.method.BaseCommonJsUtils;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public abstract class AbsCommonJsInterfaceProxy extends AbsCommonJsInterfaceWrapper implements IJSRequest {

    public AbsCommonJsInterfaceProxy(String name, AbsJsInterface mJsInterface) {
        super(name, mJsInterface);
    }

    public AbsCommonJsInterfaceProxy(String name) {
        super(name);
    }


    @Override
    public void handleJsRequest(String jsonData) {

        try {
            JSONObject json = new JSONObject(jsonData);
            String key = json.getString(BaseCommonJsUtils.KEY_MSG_TYPE);

            final BaseCommonJsRequestBean mData = new BaseCommonJsRequestBean();
            mData.setRootJsonObj(json);
            mData.setMsgType(key);
            mData.setRootJsonStr(jsonData);

            switch (mData.getMsgType()) {
                case BaseCommonJsUtils.TYPE_REQUEST_BACK:

                    onJsPressBack();

                    break;
            }

            onJsRequest(mData);


        } catch (JSONException e) {
            e.printStackTrace();
            onJsDataParseError(e, jsonData);
        }

    }


    protected abstract void onJsRequest(BaseCommonJsRequestBean mData);

    protected abstract void onJsDataParseError(JSONException e, String jsonData);

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
