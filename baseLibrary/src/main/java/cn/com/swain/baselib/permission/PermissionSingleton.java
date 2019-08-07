package cn.com.swain.baselib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

/**
 * author Guoqiang_Sun
 * date 2019/8/7
 * desc
 */
public class PermissionSingleton {

    private static volatile PermissionSingleton mPermissionSingleton;

    public static PermissionSingleton getInstance() {
        if (mPermissionSingleton == null) {
            synchronized (PermissionSingleton.class) {
                if (mPermissionSingleton == null) {
                    mPermissionSingleton = new PermissionSingleton();
                }
            }
        }
        return mPermissionSingleton;
    }

    private final SparseArray<PermissionRequest> map;

    private PermissionSingleton() {
        map = new SparseArray<>();
    }

    public synchronized void clear() {
        for (int i = 0; i < map.size(); i++) {
            PermissionRequest permissionRequest = map.valueAt(i);
            if (permissionRequest != null) {
                permissionRequest.release();
            }
        }
        map.clear();
    }

    private synchronized PermissionRequest getPermissionRequestNoCreate(Activity act) {
        return map.get(act.hashCode());
    }

    private synchronized PermissionRequest getPermissionRequest(Activity act) {
        PermissionRequest permissionRequest = map.get(act.hashCode());
        if (permissionRequest == null) {
            synchronized (this) {
                permissionRequest = map.get(act.hashCode());
                if (permissionRequest == null) {
                    permissionRequest = new PermissionRequest(act);
                    map.put(act.hashCode(), permissionRequest);
                }
            }
        }
        return permissionRequest;
    }

    public void release(Activity act) {
        PermissionRequest permissionRequest;
        synchronized (this) {
            permissionRequest = map.get(act.hashCode());
        }
        if (permissionRequest != null) {
            permissionRequest.release();
        }
        synchronized (this) {
            map.remove(act.hashCode());
        }
    }

    public void requestPermissions(Activity act,
                                   String... permissionArray) {
        getPermissionRequest(act).requestPermissions(permissionArray);
    }

    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionFinish mFinish,
                                   String... permissionArray) {
        getPermissionRequest(act).requestPermissions(mFinish, permissionArray);
    }

    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionResult mResult,
                                   String... permissionArray) {
        getPermissionRequest(act).requestPermissions(mResult, permissionArray);
    }

    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionFinish mFinish,
                                   PermissionRequest.OnPermissionResult mResult,
                                   String... permissionArray) {
        getPermissionRequest(act).requestPermissions(mFinish, mResult, permissionArray);
    }

    public void requestPermission(Activity act, String permissionStr) {
        getPermissionRequest(act).requestPermission(permissionStr);
    }

    public void requestPermission(Activity act,
                                  PermissionRequest.OnPermissionResult mResult,
                                  String permissionStr) {
        getPermissionRequest(act).requestPermission(mResult, permissionStr);
    }

    public boolean checkSelfPermission(Context act, String permissionStr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ContextCompat.checkSelfPermission(act,
                    permissionStr) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public boolean needRationaleForPermission(Activity act, String permissionStr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 权限被禁止
            return !ActivityCompat.shouldShowRequestPermissionRationale(act, permissionStr);
        }
        return false;
    }

    public void onRequestPermissionsResult(Activity act,
                                           int requestCode,
                                           String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionRequest permissionRequestNoCreate = getPermissionRequestNoCreate(act);
        if (permissionRequestNoCreate != null) {
            permissionRequestNoCreate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
