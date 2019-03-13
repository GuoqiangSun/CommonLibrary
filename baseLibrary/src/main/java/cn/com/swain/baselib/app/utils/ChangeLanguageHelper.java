package cn.com.swain.baselib.app.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */
public class ChangeLanguageHelper {

    /**
     * 修改app内部语言
     *
     * @param context  Context
     * @param language Locale
     */
    public static void changeLanguage(Context context, Locale language) {
        Resources mResources = context.getResources();
        changeLanguage(mResources, language);
    }

    /**
     * 修改app内部语言
     *
     * @param mResources Resources
     * @param language   Locale
     */
    public static void changeLanguage(Resources mResources, Locale language) {

        Configuration config = mResources.getConfiguration();     // 获得设置对象
        DisplayMetrics dm = mResources.getDisplayMetrics();
        config.locale = language;
        config.setLayoutDirection(language);
        mResources.updateConfiguration(config, dm);

    }


}
