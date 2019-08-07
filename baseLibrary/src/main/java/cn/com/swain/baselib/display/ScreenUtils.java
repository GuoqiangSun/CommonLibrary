package cn.com.swain.baselib.display;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * author Guoqiang_Sun
 * date 2019/7/30
 * desc
 */
public class ScreenUtils {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Get Screen Width
     */
    public static int getWidthPixels(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * Get Screen Height
     */
    public static int getHeightPixels(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * Get Density
     */
    private static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    /**
     * Get Dpi
     */
    private static int getDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * 获取应用屏幕高度
     *
     * @param context
     * @return
     */
    public static PointF getWindowWH(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return new PointF();
        }
        Point point = new Point();
        display.getSize(point);
        return new PointF(point);
    }

    /**
     * Get Screen Real Height
     * .获取屏幕的实际高度(API14)
     *
     * @param context Context
     * @return Real Height
     */
    public static PointF getScreenWH14(Context context) {
        Display display = getDisplay(context);
        PointF mPoint = new PointF();
        //版本API 19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            display.getRealMetrics(dm);
            mPoint.x = dm.widthPixels;
            mPoint.y = dm.heightPixels;
            //版本API 14
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            PointF screenWidthHeight = getWindowWH(context);
            try {
                mPoint.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                mPoint.y = screenWidthHeight.y;
            }
            try {
                mPoint.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
            } catch (Exception e) {
                mPoint.x = screenWidthHeight.x;
            }
        }
        return mPoint;
    }


    /**
     * Get Screen Real Height
     * 获取屏幕的实际高度(API19)
     *
     * @param context Context
     * @return Real Height
     */
    public static PointF getScreenWH19(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return new PointF();
        }
        DisplayMetrics dm = new DisplayMetrics();
        PointF p = new PointF();
        display.getRealMetrics(dm);
        p.x = dm.widthPixels;
        p.y = dm.heightPixels;
        return p;
    }

    /**
     * 获取实际屏幕的宽高度（API19）
     *
     * @param context
     * @return
     */
    public static PointF getScreenWH(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return new PointF();
        }
        Point outSize = new Point();
        display.getRealSize(outSize);
        return new PointF(outSize);
    }


    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            wm = activity.getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            return wm.getDefaultDisplay();
        }
        return null;
    }


    /**
     * Android 获取控件相对于屏幕位置
     *
     * @param v
     * @return
     */
    public static int[] getLocationOnScreen(View v) {
        int[] loc = new int[4];
        getLocationOnScreen(v, loc);
        return loc;
    }


    /**
     * Android 获取控件相对于屏幕位置
     *
     * @param v
     * @return
     */
    public static void getLocationOnScreen(View v, int[] loc) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        loc[0] = location[0];
        loc[1] = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();

        //base = computeWH();
    }


    /**
     * Android 获取控件相对于Activity位置
     *
     * @param v
     * @return
     */
    public static int[] getLocationInWindow(View v) {
        int[] loc = new int[4];
        getLocationInWindow(v, loc);
        return loc;
    }


    /**
     * Android 获取控件相对于Activity位置
     *
     * @param v
     * @return
     */
    public static void getLocationInWindow(View v, int[] loc) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        loc[0] = location[0];
        loc[1] = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();
    }

}
