package cn.com.swain.baselib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/23 0023
 * desc :
 */
public class AppUtils {

    /**
     * 判断手机是否安装某个应用
     *
     * @param context        上下文
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isAppInstalled(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取本应用的版本号
     */
    public static int getAppVersionCode(Context mContext) {
        Context applicationContext = mContext.getApplicationContext();
        return getAppVersionCode(applicationContext, applicationContext.getPackageName());
    }

    /**
     * 获取应用的版本号
     */
    public static int getAppVersionCode(Context applicationContext, String pkgName) {

        PackageManager packageManager = applicationContext.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, 0);
            return packageInfo.versionCode;
            // return String.valueOf(packageInfo.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取本应用的版本
     */
    public static String getAppVersionStr(Context mContext) {
        Context applicationContext = mContext.getApplicationContext();
        return getAppVersionStr(applicationContext, applicationContext.getPackageName());
    }

    /**
     * 获取应用的版本
     */
    public static String getAppVersionStr(Context applicationContext, String pkgName) {
        PackageManager packageManager = applicationContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, 0);
            return packageInfo.versionName;
            // return String.valueOf(packageInfo.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "00";
    }

    /**
     * 判断服务名是否正在运行
     */
    public static boolean serviceIsRun(Context mContext, String serviceClassName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        return serviceIsRun(am, serviceClassName);
    }

    /**
     * 判断service是否正在运行
     */
    public static boolean serviceIsRun(ActivityManager am, String serviceClassName) {
        if (StrUtil.isBlank(serviceClassName)) {
            return false;
        }

        boolean isWork = false;

        List<RunningServiceInfo> runningServices = am.getRunningServices(60);
        int size = runningServices.size();
        if (size <= 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            String mName = runningServices.get(i).service.getClassName();
            if (mName.equals(serviceClassName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 获取正在运行的service
     */
    public static ArrayList<String> getRunningService(Context mContext, String packageName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        return getRunningService(am, packageName);
    }

    /**
     * 获取正在运行的service
     */
    public static ArrayList<String> getRunningService(ActivityManager am, String packageName) {

        if (StrUtil.isBlank(packageName)) {
            return null;
        }

        List<RunningServiceInfo> runningServices = am.getRunningServices(60);
        int size = runningServices.size();
        if (size <= 0) {
            return null;
        }
        ArrayList<String> rs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ComponentName service = runningServices.get(i).service;
            String pkgName = service.getPackageName();
            if (pkgName.equals(packageName)) {
                rs.add(service.getClassName());
            }
        }
        return rs;
    }

    /**
     * 获取签名
     */
    public static String getSignature(Context mContext) {

        PackageManager packageManager = mContext.getPackageManager();

        try {

            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);

            Signature[] signatures = packageInfo.signatures;

//            int length = signatures.length;
//            for (int i = 0; i < length; i++) {
//                Signature signature = signatures[i];
//                String charsString = signature.toCharsString();
//            }

            return signatures[0].toCharsString();

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * app生成ssl
     */
    public static String generalSsl(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(info.signatures[0].toByteArray());
            String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            if (s == null) {
                return "";
            }
            return s.trim();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

}
