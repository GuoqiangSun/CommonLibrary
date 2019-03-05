package cn.com.swain.baselib.util;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/12 0012
 * desc :
 */
public class StatusBarUtil {

    /**
     * 全屏隐藏状态栏
     * <p>
     * * View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏,Activity不全屏显示(恢复到有状态的正常情况)。
     * * View.INVISIBLE：隐藏状态栏,同时Activity会伸展全屏显示。
     * * View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示,且状态栏被隐藏覆盖掉。
     * * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖,状态栏依然可见，Activity顶端布局部分会被状态遮住。
     * * View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * * View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * * View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
     * * View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式),状态栏上一些图标显示会被隐藏。
     *
     * @param window
     * @param layout
     */
    public static void fullScreenHideStatusBar(Window window, boolean layout) {

        View decorView = window.getDecorView();
        int model;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            model =
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏导航图标
            ;

            if (layout) {
                model |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            } else {
                // 有流海屏的手机,用下面的方法有问题
                model |= View.SYSTEM_UI_FLAG_FULLSCREEN;
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN //隐藏状态栏
                );
            }

//            model |= decorView.getSystemUiVisibility();

//            //在使用LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES的时候，状态栏会显示为白色，这和主内容区域颜色冲突,
//            //所以我们要开启沉浸式布局模式，即真正的全屏模式,以实现状态和主体内容背景一致
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            window.setAttributes(lp);

        } else {

            model = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                model |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                ;
            }

        }

        decorView.setSystemUiVisibility(model);
    }


    /**
     * 全屏显示，状态透明,字体默认白色,不隐藏导航
     *
     * @param window
     */
    public static void fullscreenShowBarFontWhite(Window window) {

        fullScreenShowStatusBar(window, false);

    }

    /**
     * 全屏显示，状态透明,字体黑色
     *
     * @param window
     */
    public static void fullscreenShowBarFontBlack(Window window) {

        fullScreenShowStatusBar(window, true);

    }

    /**
     * 全屏显示，显示状态栏
     * <p>
     * * View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏,Activity不全屏显示(恢复到有状态的正常情况)。
     * * View.INVISIBLE：隐藏状态栏,同时Activity会伸展全屏显示。
     * * View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示,且状态栏被隐藏覆盖掉。
     * * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖,状态栏依然可见，Activity顶端布局部分会被状态遮住。
     * * View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * * View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * * View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
     * * View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式),状态栏上一些图标显示会被隐藏。
     *
     * @param window
     * @param fontBlack 字体黑色
     */
    public static void fullScreenShowStatusBar(Window window, boolean fontBlack) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.clearFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN//显示状态栏
            );

            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

//                flag |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;// 隐藏导航图标

            if (fontBlack) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;// 加了是黑色字体
                }
            }

            window.getDecorView().setSystemUiVisibility(flag);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //api21新增接口
            window.setStatusBarColor(Color.TRANSPARENT);

//                window.setNavigationBarColor(Color.TRANSPARENT);// 导航图标透明

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int getStatusBarLightMode(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(window, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            } else {//5.0

            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     */
    public static void setStatusBarLightMode(Window window) {
        int type = getStatusBarLightMode(window);
        if (type == 1) {
            MIUISetStatusBarLightMode(window, true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, true);
        } else if (type == 3) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {//5.0

        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Window window) {
        int type = getStatusBarLightMode(window);
        if (type == 1) {
            MIUISetStatusBarLightMode(window, false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, false);
        } else if (type == 3) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}
