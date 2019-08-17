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


    /**
     * 获取PermissionRequest ,有则直接返回，没有创建再返回
     */
    private synchronized PermissionRequest getPermissionRequestAndCreate(Activity act) {
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

    /**
     * 获取缓存中的第一个PermissionRequest
     */
    private synchronized PermissionRequest getFirstPermissionRequest() {
        return map.valueAt(0);
    }

    /**
     * 获取PermissionRequest ,直接返回
     */
    private PermissionRequest getPermissionRequestNoCreate(Activity act) {
        return getPermissionRequestNoCreate(act.hashCode());
    }

    /**
     * 获取PermissionRequest ,直接返回
     */
    private synchronized PermissionRequest getPermissionRequestNoCreate(int hasCode) {
        return map.get(hasCode);
    }

    /**
     * 是否存在此activity的PermissionRequest
     */
    public boolean exitPermissionRequest(Activity act) {
        return exitPermissionRequest(act.hashCode());
    }

    /**
     * 是否存在此activity.hashCode的PermissionRequest
     */
    public synchronized boolean exitPermissionRequest(int hashCode) {
        return getPermissionRequestNoCreate(hashCode) != null;
    }

    /**
     * 注册一个PermissionRequest
     */
    public synchronized void regPermissionRequest(Activity act) {
        getPermissionRequestAndCreate(act);
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
        getPermissionRequestAndCreate(act).requestPermissions(permissionArray);
    }

    public boolean requestPermissions(int hashCode,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(permissionArray);
            return true;
        }
        return false;
    }

    public boolean requestPermissions(String... permissionArray) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(permissionArray);
            return true;
        }
        return false;
    }

    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionFinish mFinish,
                                   String... permissionArray) {
        getPermissionRequestAndCreate(act).requestPermissions(mFinish, permissionArray);
    }

    public boolean requestPermissions(int hashCode,
                                      PermissionRequest.OnPermissionFinish mFinish,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mFinish, permissionArray);
            return true;
        }
        return false;
    }

    public boolean requestPermissions(PermissionRequest.OnPermissionFinish mFinish,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mFinish, permissionArray);
            return true;
        }
        return false;
    }

    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionResult mResult,
                                   String... permissionArray) {
        getPermissionRequestAndCreate(act).requestPermissions(mResult, permissionArray);
    }

    public boolean requestPermissions(int hashCode,
                                      PermissionRequest.OnPermissionResult mResult,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mResult, permissionArray);
            return true;
        }
        return false;
    }

    public boolean requestPermissions(PermissionRequest.OnPermissionResult mResult,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mResult, permissionArray);
            return true;
        }
        return false;
    }


    public void requestPermissions(Activity act,
                                   PermissionRequest.OnPermissionFinish mFinish,
                                   PermissionRequest.OnPermissionResult mResult,
                                   String... permissionArray) {
        getPermissionRequestAndCreate(act).requestPermissions(mFinish, mResult, permissionArray);
    }

    public boolean requestPermissions(int hashCode,
                                      PermissionRequest.OnPermissionFinish mFinish,
                                      PermissionRequest.OnPermissionResult mResult,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mFinish, mResult, permissionArray);
            return true;
        }
        return false;
    }

    public boolean requestPermissions(PermissionRequest.OnPermissionFinish mFinish,
                                      PermissionRequest.OnPermissionResult mResult,
                                      String... permissionArray) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermissions(mFinish, mResult, permissionArray);
            return true;
        }
        return false;
    }

    public void requestPermission(Activity act,
                                  String permissionStr) {
        getPermissionRequestAndCreate(act).requestPermission(permissionStr);
    }

    public boolean requestPermission(int hashCode,
                                     String permissionStr) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermission(permissionStr);
            return true;
        }
        return false;
    }

    public boolean requestPermission(String permissionStr) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermission(permissionStr);
            return true;
        }
        return false;
    }

    public void requestPermission(Activity act,
                                  PermissionRequest.OnPermissionResult mResult,
                                  String permissionStr) {
        getPermissionRequestAndCreate(act).requestPermission(mResult, permissionStr);
    }

    public boolean requestPermission(int hashCode,
                                     PermissionRequest.OnPermissionResult mResult,
                                     String permissionStr) {
        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            permissionRequest.requestPermission(mResult, permissionStr);
            return true;
        }
        return false;
    }

    public boolean requestPermission(PermissionRequest.OnPermissionResult mResult,
                                     String permissionStr) {
        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            permissionRequest.requestPermission(mResult, permissionStr);
            return true;
        }
        return false;
    }

    public boolean checkSelfPermission(Context act, String permissionStr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ContextCompat.checkSelfPermission(act,
                    permissionStr) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public boolean checkSelfPermission(int hashCode, String permissionStr) {

        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            return permissionRequest.checkSelfPermission(permissionStr);
        }
        return false;
    }

    public boolean checkSelfPermission(String permissionStr) {

        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            return permissionRequest.checkSelfPermission(permissionStr);
        }
        return false;
    }

    public boolean needRationaleForPermission(Activity act, String permissionStr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 权限被禁止
            return !ActivityCompat.shouldShowRequestPermissionRationale(act, permissionStr);
        }
        return false;
    }

    public boolean needRationaleForPermission(int hashCode, String permissionStr) {

        PermissionRequest permissionRequest = getPermissionRequestNoCreate(hashCode);
        if (permissionRequest != null) {
            return permissionRequest.needRationaleForPermission(permissionStr);
        }
        return false;
    }

    public boolean needRationaleForPermission(String permissionStr) {

        PermissionRequest permissionRequest = getFirstPermissionRequest();
        if (permissionRequest != null) {
            return permissionRequest.needRationaleForPermission(permissionStr);
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
