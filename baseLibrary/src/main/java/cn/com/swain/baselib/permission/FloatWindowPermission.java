/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package cn.com.swain.baselib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Method;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.rom.HuaweiUtils;
import cn.com.swain.baselib.permission.rom.MeizuUtils;
import cn.com.swain.baselib.permission.rom.MiuiUtils;
import cn.com.swain.baselib.permission.rom.OppoUtils;
import cn.com.swain.baselib.permission.rom.QikuUtils;
import cn.com.swain.baselib.permission.rom.RomUtils;


public class FloatWindowPermission {
    public static final String TAG = "FloatWindowPermission";

    public static final int REQUEST_CODE = 0x9637;

    private static volatile FloatWindowPermission instance;

    public static FloatWindowPermission getInstance() {
        if (instance == null) {
            synchronized (FloatWindowPermission.class) {
                if (instance == null) {
                    instance = new FloatWindowPermission();
                }
            }
        }
        return instance;
    }

    private OnFloatWindowPermissionLsn mResult;

    public void regFloatWindowPermissionListener(OnFloatWindowPermissionLsn mResult) {
        this.mResult = mResult;
    }

    public void unregFloatWindowPermissionListener(OnFloatWindowPermissionLsn mResult) {
        if (this.mResult != null && this.mResult == mResult) {
            this.mResult = null;
        }
    }

    public void unregFloatWindowPermissionListener() {
        this.mResult = null;
    }

    public interface OnFloatWindowPermissionLsn {
        void onFloatWindowPermissionResult(boolean grant);
    }

    public void applyFloatPermission(Activity context) {
        if (this.checkPermission(context)) {
            if (mResult != null) {
                mResult.onFloatWindowPermissionResult(true);
            }
        } else {
            this.applyPermission(context);
        }
    }

    public void onActivityResult(Context mContext, int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (mResult != null) {
                mResult.onFloatWindowPermissionResult(this.checkPermission(mContext));
            }
        } else {
            Tlog.v(TAG, " receive activity result but not my requestCode");
        }

    }

    public boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            } else {

//                其他机型则默认是有权限
                return true;
            }
        }
        return commonROMPermissionCheck(context);
    }

    public boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    public boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    public boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    public boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    public boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    public boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Tlog.e(TAG, " commonROMPermissionCheck ", e);
                    result = false;
                }
            }
            return result;
        }
    }

    private void applyPermission(Activity context) {
        boolean result;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                result = miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                result = meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                result = huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                result = ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                result = oppoROMPermissionApply(context);
            } else {
                result = false;
            }
        } else {
            result = commonROMPermissionApply(context);
        }

        if (!result) {
            if (mResult != null) {
                mResult.onFloatWindowPermissionResult(false);
            }
        }
    }

    private boolean ROM360PermissionApply(final Activity context) {
        return QikuUtils.applyPermission(context, REQUEST_CODE);
    }

    private boolean huaweiROMPermissionApply(final Activity context) {
        return HuaweiUtils.applyPermission(context, REQUEST_CODE);
    }

    private boolean meizuROMPermissionApply(final Activity context) {
        return MeizuUtils.applyPermission(context, REQUEST_CODE);
    }

    private boolean miuiROMPermissionApply(final Activity context) {
        return MiuiUtils.applyMiuiPermission(context, REQUEST_CODE);
    }

    private boolean oppoROMPermissionApply(final Activity context) {
        return OppoUtils.applyOppoPermission(context, REQUEST_CODE);
    }

    /**
     * 通用 rom 权限申请
     */
    private boolean commonROMPermissionApply(final Activity context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            return meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    commonROMPermissionApplyInternal(context, REQUEST_CODE);
                } catch (Exception e) {
                    Tlog.e(TAG, " commonROMPermissionApply ", e);
                    return false;
                }
            }
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void commonROMPermissionApplyInternal(Activity context, int requestCode) {

        context.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())), requestCode);

    }

}
