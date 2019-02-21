package cn.com.swain.baselib.jsInterface.request.impl;

import android.os.Looper;
import android.os.Message;

import cn.com.swain.baselib.jsInterface.AbsHandlerJsInterface;
import cn.com.swain.baselib.jsInterface.request.IJSRequest;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc : startai 和 js 通信的模板
 */
@Deprecated
public class StartaiDataJsRequest extends AbsHandlerJsInterface {

    private static final String NAME_JS = "Data";

    private final IJSRequest mCallBack;

    public StartaiDataJsRequest(Looper mLooper, IJSRequest mCallBack) {
        super(NAME_JS, mLooper);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void handleMessage(Message msg) {

        if (msg.what == MSG_KEY) {
            if (mCallBack != null) {
                // callback to AbsCommonJsInterfaceProxy
                mCallBack.handleJsRequest((String) msg.obj);
            } else {
                Tlog.e(TAG, " StartaiDataJsRequest handleMessage IJSRequest is null ");
            }
        }

    }

    private static final int MSG_KEY = 0x00;

    protected void intervalDataInteractionRequest(String value) {
        getHandler().obtainMessage(MSG_KEY, value).sendToTarget();
    }


}
