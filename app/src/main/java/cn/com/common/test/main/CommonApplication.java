package cn.com.common.test.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import cn.com.common.test.global.FileManager;
import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.log.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/3/28 0027
 * desc :
 */

public class CommonApplication extends BaseApplication {

    public static final String TAG = "commonApp";

    @Override
    public void onCreate() {
        super.onCreate();

        Tlog.setGlobalTag(TAG);

        FileManager.getInstance().init(this);

//        Tlog.setLogRecordDebug(true);
        Tlog.setPrintStackDebug(true);

        LooperManager.getInstance().init(this);

        Tlog.i("CommonApplication onCreate(); pid:" + android.os.Process.myPid() + "; Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityCreated " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityStarted " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityResumed " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityPaused " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityStopped " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Tlog.d(" ActivityLifecycleCallbacks onActivitySaveInstanceState " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Tlog.d(" ActivityLifecycleCallbacks onActivityDestroyed " + activity.getLocalClassName());
            }
        });


    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        super.uncaughtException(t, e);
        Tlog.e(TAG, " CommonApplication caughtException ", e);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Tlog.v(TAG, "CommonApplication onConfigurationChanged() " + newConfig.toString());
    }

    public static void uncaughtH5Exception(String msg) {
        Tlog.e(TAG, " CommonApplication uncaughtH5Exception :\n" + msg);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
