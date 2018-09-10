package cn.com.swain.baselib.util;

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

    public static final int CHANGE_LANGUAGE_CHINA = 1;
    public static final int CHANGE_LANGUAGE_ENGLISH = 2;
    public static final int CHANGE_LANGUAGE_DEFAULT = 0;

    public static int getAppLanguage(String lang) {

        if (lang.equalsIgnoreCase("zh")) {
            return CHANGE_LANGUAGE_CHINA;
        } else if (lang.equalsIgnoreCase("en")) {
            return CHANGE_LANGUAGE_ENGLISH;
        }
        return CHANGE_LANGUAGE_DEFAULT;
    }

//    private static String country = null;

    public static void init(Context context, int appLanguage) {
        Resources mResources = context.getResources();
        changeLanguage(mResources, appLanguage);
    }


    private static void changeLanguage(Resources mResources, int language) {

        Configuration config = mResources.getConfiguration();     // 获得设置对象
        DisplayMetrics dm = mResources.getDisplayMetrics();
        switch (language) {
            case CHANGE_LANGUAGE_CHINA:
                config.locale = Locale.SIMPLIFIED_CHINESE;     // 中文
                config.setLayoutDirection(Locale.SIMPLIFIED_CHINESE);


                break;
            case CHANGE_LANGUAGE_ENGLISH:
                config.locale = Locale.ENGLISH;   // 英文
                config.setLayoutDirection(Locale.ENGLISH);


                break;
            case CHANGE_LANGUAGE_DEFAULT:

                String country = Locale.getDefault().getCountry();

                Locale mDefaultLocale;
                if ("CN".equals(country)) {
                    mDefaultLocale = Locale.SIMPLIFIED_CHINESE;
                } else {
                    mDefaultLocale = Locale.ENGLISH;
                }

                config.locale = mDefaultLocale;         // 系统默认语言
                config.setLayoutDirection(mDefaultLocale);

                break;
        }

        mResources.updateConfiguration(config, dm);

    }


}
