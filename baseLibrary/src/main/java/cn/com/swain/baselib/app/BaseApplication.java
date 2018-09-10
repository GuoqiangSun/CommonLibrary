package cn.com.swain.baselib.app;

import android.support.multidex.MultiDexApplication;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/28 0028
 * desc :
 */

public class BaseApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {


    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    public void setTlogDebug(boolean flag) {
        Tlog.setDebug(flag);
    }

    public void setTlogRecordDebug(boolean flag) {
        Tlog.setLogRecordDebug(flag);
    }

    public void setGlobalTlogTAG(String TAG) {
        Tlog.setGlobalTag(TAG);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
