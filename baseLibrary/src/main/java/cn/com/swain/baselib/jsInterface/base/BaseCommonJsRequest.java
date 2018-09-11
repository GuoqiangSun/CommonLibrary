package cn.com.swain.baselib.jsInterface.base;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import cn.com.swain.baselib.jsInterface.AbsHandlerJsInterface;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class BaseCommonJsRequest extends AbsHandlerJsInterface {

    private static final String NAME_JS = "Data";

    private final IJSRequest mCallBack;

    public BaseCommonJsRequest(Looper mLooper, IJSRequest mCallBack) {
        super(NAME_JS, mLooper);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void handleMessage(Message msg) {

        if (mCallBack != null) {
            mCallBack.handleJsRequest((String) msg.obj);
        } else {
            Log.e(TAG, " CommonJsRequest handleMessage IJSRequest is null ");
        }

    }

    private static final int MSG_KEY = 0x00;

    protected void intervalDataInteractionRequest(String value) {
        getHandler().obtainMessage(MSG_KEY, value).sendToTarget();
    }


}
