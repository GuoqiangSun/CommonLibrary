package cn.com.common.test;

import android.content.res.Configuration;
import android.os.Build;

import cn.com.common.baselib.app.BaseApplication;
import cn.com.common.baselib.app.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/3/28 0027
 * desc :
 */

public class SocketApplication extends BaseApplication {

    public static final String TAG = "socketApp";

    @Override
    public void onCreate() {
        super.onCreate();

        setGlobalTlogTAG(TAG);

        LooperManager.getInstance().init(this);

        Tlog.i("SocketApplication onCreate(); pid:" + android.os.Process.myPid() + "; Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        super.uncaughtException(t, e);
        Tlog.e(TAG, " SocketApplication caughtException ", e);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Tlog.v(TAG, "SocketApplication onConfigurationChanged() " + newConfig.toString());
    }

    public static void uncaughtH5Exception(String msg) {
        Tlog.e(TAG, " SocketApplication uncaughtH5Exception :\n" + msg);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
