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

import cn.com.swain.baselib.alg.PointS;

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
     * 屏幕密度倍数（0.75（ldpi） / 1.0(mdpi) / 1.5(hdpi) /2(xhdpi) /3(xxhdpi) /4(xxxhdpi) ）
     */
    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    /**
     * Get Dpi
     * 屏幕密度DPI（120 / 160 / 240/ 320/ 480/ 640）
     */
    public static int getDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * 密度转换像素
     */
    public static float dip2pxF(float dipValue, float scale) {
        return dipValue * scale;
    }

    /**
     * 密度转换像素
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 像素转换密度
     */
    public static float px2dipF(float pxValue, float scale) {
        return pxValue / scale;
    }

    /**
     * 像素转换密度
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
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


    public static void getLocationOnScreen(View v, PointS mPointS) {
        int[] locationOnScreen = getLocationOnScreen(v);
        mPointS.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static void getLocationOnScreen(View v, PointF mPointF) {
        int[] locationOnScreen = getLocationOnScreen(v);
        mPointF.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static void getLocationOnScreen(View v, Point mPoint) {
        int[] locationOnScreen = getLocationOnScreen(v);
        mPoint.set(locationOnScreen[0], locationOnScreen[1]);
    }

    /**
     * Android 获取控件相对于屏幕位置
     *
     * @param v v
     * @return int[]
     * index=0 : x in screen
     * index=1 : y in screen
     */
    public static int[] getLocationOnScreen(View v) {
        int[] loc = new int[2];
        getLocationOnScreen(v, loc);
        return loc;
    }


    /**
     * Android 获取控件相对于屏幕位置
     *
     * @param v v
     * @@param loc
     * index=0 : x in screen
     * index=1 : y in screen
     */
    public static void getLocationOnScreen(View v, int[] loc) {
        v.getLocationOnScreen(loc);
    }

    public static PointS getLocationInWindowS(View v) {
        PointS mPointS = new PointS();
        getLocationInWindow(v, mPointS);
        return mPointS;
    }

    public static void getLocationInWindow(View v, PointS mPointS) {
        int[] locationOnScreen = getLocationInWindow(v);
        mPointS.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static PointF getLocationInWindowF(View v) {
        PointF mPointF = new PointF();
        getLocationInWindow(v, mPointF);
        return mPointF;
    }

    public static void getLocationInWindow(View v, PointF mPointF) {
        int[] locationOnScreen = getLocationInWindow(v);
        mPointF.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static Point getLocationInWindowI(View v) {
        Point mPoint = new Point();
        getLocationInWindow(v, mPoint);
        return mPoint;
    }

    public static void getLocationInWindow(View v, Point mPoint) {
        int[] locationOnScreen = getLocationInWindow(v);
        mPoint.set(locationOnScreen[0], locationOnScreen[1]);
    }

    /**
     * Android 获取控件相对于Activity位置
     *
     * @param v v
     * @return int[]
     * index=0 : x in window
     * index=1 : y in window
     */
    public static int[] getLocationInWindow(View v) {
        int[] loc = new int[2];
        getLocationInWindow(v, loc);
        return loc;
    }


    /**
     * Android 获取控件相对于Activity位置
     *
     * @param v
     * @param loc index=0 : x in window
     *            index=1 : y in window
     *            index=2 : width
     *            index=3 : height
     */
    public static void getLocationInWindow(View v, int[] loc) {
        v.getLocationInWindow(loc);
    }

    public static PointS getViewWHS(View v) {
        PointS mPointS = new PointS();
        getViewWH(v, mPointS);
        return mPointS;
    }

    /**
     * Android 测量控件的宽高
     *
     * @param v       v
     * @param mPointS mPoint
     *                index=0 : x in window
     *                index=1 : y in window
     */
    public static void getViewWH(View v, PointS mPointS) {
        int[] locationOnScreen = getViewWH(v);
        mPointS.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static PointF getViewWHF(View v) {
        PointF mPointF = new PointF();
        getViewWH(v, mPointF);
        return mPointF;
    }

    /**
     * Android 测量控件的宽高
     *
     * @param v       v
     * @param mPointF mPoint
     *                index=0 : x in window
     *                index=1 : y in window
     */
    public static void getViewWH(View v, PointF mPointF) {
        int[] locationOnScreen = getViewWH(v);
        mPointF.set(locationOnScreen[0], locationOnScreen[1]);
    }

    public static Point getViewWHI(View v) {
        Point mPoint = new Point();
        getViewWH(v, mPoint);
        return mPoint;
    }

    /**
     * Android 测量控件的宽高
     *
     * @param v      v
     * @param mPoint mPoint
     *               index=0 : x in window
     *               index=1 : y in window
     */
    public static void getViewWH(View v, Point mPoint) {
        int[] locationOnScreen = getViewWH(v);
        mPoint.set(locationOnScreen[0], locationOnScreen[1]);
    }

    /**
     * Android 测量控件的宽高
     *
     * @param v v
     * @return int[]
     * index=0 : width
     * index=1 : height
     */
    public static int[] getViewWH(View v) {
        int[] loc = new int[2];
        getViewWH(v, loc);
        return loc;
    }

    /**
     * Android 测量控件的宽高
     *
     * @param v v
     * @return int[]
     * index=0 : width
     * index=1 : height
     */
    public static void getViewWH(View v, int[] loc) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        loc[0] = v.getMeasuredWidth();
        loc[1] = v.getMeasuredHeight();
    }

}
