/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package cn.com.swain.baselib.permission.rom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.FloatWindowPermission;

public class MiuiUtils {
    private static final String TAG = FloatWindowPermission.TAG;

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = RomUtils.getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Tlog.e(TAG, "get miui version code error, version : " + version, e);
            }
        }
        return -1;
    }

    /**
     * 检测 miui 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        } else {
//            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
//                return true;
//            } else {
//                return false;
//            }
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Tlog.e(TAG, " MiuiUtils checkOp ", e);
            }
        } else {
            Tlog.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 小米 ROM 权限申请
     */
    public static boolean applyMiuiPermission(Activity context, int requestCode) {
        int versionCode = getMiuiVersion();
        if (versionCode == 5) {
            return goToMiuiPermissionActivity_V5(context, requestCode);
        } else if (versionCode == 6) {
            return goToMiuiPermissionActivity_V6(context, requestCode);
        } else if (versionCode == 7) {
            return goToMiuiPermissionActivity_V7(context, requestCode);
        } else if (versionCode == 8) {
            return goToMiuiPermissionActivity_V8(context, requestCode);
        } else {
            Tlog.e(TAG, "this is a special MIUI rom version, its version code " + versionCode);
            return false;
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 小米 V5 版本 ROM权限申请
     */
    public static boolean goToMiuiPermissionActivity_V5(Activity context, int requestCode) {
        Intent intent = null;
        String packageName = context.getPackageName();
        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivityForResult(intent, requestCode);
            return true;
        } else {
            Tlog.e(TAG, "intent is not available!");
            return false;
        }

        //设置页面在应用详情页面
//        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//        PackageInfo pInfo = null;
//        try {
//            pInfo = context.getPackageManager().getPackageInfo
//                    (HostInterfaceManager.getHostInterface().getApp().getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            AVLogUtils.e(TAG, e.getMessage());
//        }
//        intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
//        intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (isIntentAvailable(intent, context)) {
//            context.startActivity(intent);
//        } else {
//            AVLogUtils.e(TAG, "Intent is not available!");
//        }
    }

    /**
     * 小米 V6 版本 ROM权限申请
     */
    public static boolean goToMiuiPermissionActivity_V6(Activity context, int requestCode) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivityForResult(intent, requestCode);
            return true;
        } else {
            Tlog.e(TAG, "Intent is not available!");
            return false;
        }
    }

    /**
     * 小米 V7 版本 ROM权限申请
     */
    public static boolean goToMiuiPermissionActivity_V7(Activity context, int requestCode) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivityForResult(intent, requestCode);
            return true;
        } else {
            Tlog.e(TAG, "Intent is not available!");
            return false;
        }
    }

    /**
     * 小米 V8 版本 ROM权限申请
     */
    public static boolean goToMiuiPermissionActivity_V8(Activity context, int requestCode) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
//        intent.setPackage("com.miui.securitycenter");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivityForResult(intent, requestCode);
            return true;
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (isIntentAvailable(intent, context)) {
                context.startActivityForResult(intent, requestCode);
                return true;
            } else {
                Tlog.e(TAG, "Intent is not available!");
                return false;
            }
        }
    }
}
