package cn.com.common.test.main;

import android.content.res.Configuration;
import android.os.Build;

import java.io.File;

import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.file.FileTemplate;
import cn.com.swain169.log.TFlog;
import cn.com.swain169.log.Tlog;
import cn.com.swain169.log.logRecord.impl.LogRecordManager;


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

        FileTemplate f = new FileTemplate() {
            @Override
            protected File initMyProjectPath() {
                return null;
            }
        };
        f.init(this);
//        TFlog.set(new LogRecordManager(f.getLogPath(), "00", 1024 * 1024 * 6));
        Tlog.setLogRecordDebug(true);
        Tlog.setPrintStackDebug(true);
        LooperManager.getInstance().init(this);

        Tlog.i("CommonApplication onCreate(); pid:" + android.os.Process.myPid() + "; Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT);
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
