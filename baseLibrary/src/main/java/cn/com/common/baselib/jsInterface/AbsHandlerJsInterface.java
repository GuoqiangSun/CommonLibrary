package cn.com.common.baselib.jsInterface;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import cn.com.common.baselib.app.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/6/29 0029
 * desc :
 */
public abstract class AbsHandlerJsInterface extends AbsJsInterface {

    private final JsHandler mHandler;

    protected Handler getHandler() {
        return mHandler;
    }

    public AbsHandlerJsInterface(String name, Looper mLooper) {
        super(name);
        this.mHandler = new JsHandler(this, mLooper);
    }

    @Override
    public void release() {
        super.release();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.releaseWR();
        }
    }

    private static class JsHandler extends Handler {
        private final WeakReference<AbsHandlerJsInterface> wr;

        void releaseWR() {
            if (wr != null) {
                wr.clear();
            }
        }

        JsHandler(AbsHandlerJsInterface jm, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<>(jm);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            AbsHandlerJsInterface jm;
            if (wr != null && (jm = wr.get()) != null) {
                jm.handleMessage(msg);
            } else {
                Tlog.e(TAG, " JsHandler AbsHandlerJsInterface==null");
            }
        }
    }

    protected abstract void handleMessage(Message msg);

}
