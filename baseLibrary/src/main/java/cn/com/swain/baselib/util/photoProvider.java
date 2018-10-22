package cn.com.swain.baselib.util;

import android.support.v4.content.FileProvider;

import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/12 0012
 * desc :
 */
public class photoProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        Tlog.d(" photoProvider onCreate ");
        return super.onCreate();
    }
}
