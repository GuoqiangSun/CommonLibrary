package cn.com.swain.baselib.jsInterface.example;

import android.os.Looper;
import android.os.Message;
import android.webkit.JavascriptInterface;

import cn.com.swain.baselib.jsInterface.AbsHandlerJsInterface;
import cn.com.swain.baselib.jsInterface.AbsJsInterface;
import cn.com.swain.baselib.jsInterface.request.IJSRequest;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/30 0030
 * desc :
 */
public final class _X5CommonJsRequestInterface extends AbsHandlerJsInterface {

    private static final String NAME_JS = "Data";

    private final IJSRequest mCallBack;

    public _X5CommonJsRequestInterface(Looper mLooper, IJSRequest mCallBack) {
        super(NAME_JS, mLooper);
        this.mCallBack = mCallBack;
    }


    private static final int MSG_KEY = 0x00;


    @JavascriptInterface
    public void dataInteractionRequest(String value) {

        getHandler().obtainMessage(MSG_KEY, value).sendToTarget();

    }

    @Override
    public AbsJsInterface getJsInterface() {
        return this;
    }

    @Override
    protected void handleMessage(Message msg) {

        if (mCallBack != null) {
            mCallBack.handleJsRequest((String) msg.obj);
        } else {
            Tlog.e(TAG, " _X5CommonJsRequestInterface handleMessage IJSRequest is null ");
        }

    }

}
