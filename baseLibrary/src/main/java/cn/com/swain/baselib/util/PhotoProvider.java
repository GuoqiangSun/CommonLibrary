package cn.com.swain.baselib.util;

import android.support.v4.content.FileProvider;

import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/12 0012
 * desc :
 */
public class PhotoProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        Tlog.d(" PhotoProvider onCreate ");
        return super.onCreate();
    }
}
