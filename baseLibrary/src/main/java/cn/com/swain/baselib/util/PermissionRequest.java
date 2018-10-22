package cn.com.swain.baselib.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import cn.com.swain.baselib.Queue.SyncLimitQueue;
import cn.com.swain169.log.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/6/9 0009
 * desc :
 */
public final class PermissionRequest {

    private String TAG = "permissionRequest";

    private WeakReference<Activity> mWr;
    private PermissionHandler mHandler;

    public PermissionRequest(Activity mActivity) {
        this.mWr = new WeakReference<>(mActivity);
        this.mHandler = new PermissionHandler(this, Looper.getMainLooper());
    }

    public void release() {
        mAllFinish = null;

        if (mWr != null) {
            mWr.clear();
            mWr = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.clear();
            mHandler = null;
        }
        mInternalPermissionQueue.clear();
        mExternalPermissionQueue.clear();
        mInternalPermissionQueueCache.clear();
        mExternalPermissionQueueCache.clear();
    }

    private Activity getActivity() {
        return (mWr != null ? mWr.get() : null);
    }

    /**
     * 请求所有的权限
     *
     * @param permissionArray 权限
     */
    public void requestAllPermission(String... permissionArray) {
        requestAllPermission(null, null, permissionArray);
    }


    /**
     * 请求所有的权限
     *
     * @param mAllFinish      所有权限请求完毕回调
     * @param permissionArray 权限
     */
    public void requestAllPermission(OnAllPermissionFinish mAllFinish, String... permissionArray) {
        requestAllPermission(mAllFinish, null, permissionArray);
    }


    /**
     * 请求所有的权限
     *
     * @param mAllFinish      所有权限请求完毕回调
     * @param mResult         单个权限请求结果
     * @param permissionArray 权限
     */
    public void requestAllPermission(OnAllPermissionFinish mAllFinish, OnPermissionResult mResult, String... permissionArray) {
        setOnAllPermissionResult(mAllFinish);
        requestAllPermission(mResult, permissionArray);
    }


    private OnAllPermissionFinish mAllFinish;

    /**
     * 监听所有权限是否请求完毕
     */
    private void setOnAllPermissionResult(OnAllPermissionFinish mAllFinish) {
        this.mAllFinish = mAllFinish;
    }


    private final Queue<RequestPermissionMsg> mInternalPermissionQueue = new SyncLimitQueue<>(12);
    private final Map<String, RequestPermissionMsg> mInternalPermissionQueueCache =
            Collections.synchronizedMap(new HashMap<String, RequestPermissionMsg>());


    /**
     * 请求所有的权限
     *
     * @param mResult         结果回调
     * @param permissionArray 权限
     */
    public void requestAllPermission(OnPermissionResult mResult, String... permissionArray) {

        final int size = mInternalPermissionQueue.size();

        if (permissionArray != null && permissionArray.length > 0) {
            for (String s : permissionArray) {
                if (s != null && !"".equals(s)) {
                    RequestPermissionMsg mMsg = new RequestPermissionMsg(mResult, s);
                    mInternalPermissionQueue.offer(mMsg);
                }
            }
        }

        if (size <= 0) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(INTERNAL_PERMISSIONS_REQUEST_CODE);
            }
        }

    }

    /**
     * 请求单个权限
     *
     * @param permissionStr 权限
     */
    public void requestPermission(String permissionStr) {
        requestPermission(null, permissionStr);
    }

    private final Queue<RequestPermissionMsg> mExternalPermissionQueue = new SyncLimitQueue<>(12);
    private final Map<String, RequestPermissionMsg> mExternalPermissionQueueCache =
            Collections.synchronizedMap(new HashMap<String, RequestPermissionMsg>());


    /**
     * 请求单个权限
     *
     * @param mResult       请求权限的结果
     * @param permissionStr 权限
     */
    public void requestPermission(OnPermissionResult mResult, String permissionStr) {
        if (permissionStr != null && !"".equals(permissionStr)) {
            final int size = mExternalPermissionQueue.size();
            RequestPermissionMsg mMsg = new RequestPermissionMsg(mResult, permissionStr);
            mExternalPermissionQueue.offer(mMsg);
            if (size <= 0) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(EXTERNAL_PERMISSIONS_REQUEST_CODE);
                }
            }
        }
    }

    private static final int INTERNAL_REQUEST_PERMISSION_FINISH_WHAT = 0x01;

    //onRequestPermissionsResult(requestCode)
    public static final int INTERNAL_PERMISSIONS_REQUEST_CODE = 0x8362;
    //onRequestPermissionsResult(requestCode)
    public static final int EXTERNAL_PERMISSIONS_REQUEST_CODE = 0x8363;

    private void handleMessage(Message msg) {

        switch (msg.what) {

            case INTERNAL_REQUEST_PERMISSION_FINISH_WHAT:

                if (mAllFinish != null) {
                    mAllFinish.onAllPermissionRequestFinish();
                }

                break;

            case INTERNAL_PERMISSIONS_REQUEST_CODE:

                RequestPermissionMsg internalPermissionMsg = mInternalPermissionQueue.poll();
                if (internalPermissionMsg == null) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(INTERNAL_REQUEST_PERMISSION_FINISH_WHAT);
                    }
                    return;
                }

                if (checkSelfPermission(internalPermissionMsg.permission)) {

                    if (internalPermissionMsg.mResult != null) {
                        internalPermissionMsg.mResult.onPermissionRequestResult(
                                internalPermissionMsg.permission, true);
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(INTERNAL_PERMISSIONS_REQUEST_CODE);
                    }

                } else {
                    mInternalPermissionQueueCache.put(internalPermissionMsg.permission, internalPermissionMsg);
                    requestPermission(internalPermissionMsg.permission, INTERNAL_PERMISSIONS_REQUEST_CODE);
                }

                break;


            case EXTERNAL_PERMISSIONS_REQUEST_CODE:

                RequestPermissionMsg externalPermission = mExternalPermissionQueue.poll();
                if (externalPermission == null) {
                    return;
                }

                if (checkSelfPermission(externalPermission.permission)) {
                    if (externalPermission.mResult != null) {
                        externalPermission.mResult.onPermissionRequestResult(
                                externalPermission.permission, true);
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(EXTERNAL_PERMISSIONS_REQUEST_CODE);
                    }

                } else {
                    mExternalPermissionQueueCache.put(externalPermission.permission, externalPermission);
                    requestPermission(externalPermission.permission, EXTERNAL_PERMISSIONS_REQUEST_CODE);
                }

                break;
        }

    }


    /**
     * 是否有权限
     */
    public boolean checkSelfPermission(String permissionStr) {
        Activity activity = getActivity();
        if (activity == null) {
            Tlog.e(TAG, " checkSelfPermission activity == null ");
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(activity,
                    permissionStr) == PackageManager.PERMISSION_GRANTED) {
                Tlog.v(TAG, " Has permission " + permissionStr);
                return true;
            }
            return false;
        }

        return true;
    }

    /**
     * 请求权限
     */
    private boolean requestPermission(String permissionStr, int requestCode) {
        Activity activity = getActivity();
        if (activity == null) {
            Tlog.e(TAG, " request permission activity == null ");
            return false;
        }
        Tlog.v(TAG, " request permission " + permissionStr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionStr},
                    requestCode);
        }

        return true;
    }

    /**
     * 被拒绝了
     * 需要请求权限的理由
     */
    public boolean needRationaleForPermission(String permissionsStr) {
        Activity activity = getActivity();
        if (activity == null) {
            Tlog.e(TAG, " needPermissionRationale activity==null ");
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsStr)) {
                // 权限被禁止
                Tlog.v(TAG, " should Show Request " + permissionsStr + " Rationale ");
                return true;
            }
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == INTERNAL_PERMISSIONS_REQUEST_CODE
                || requestCode == EXTERNAL_PERMISSIONS_REQUEST_CODE) {

            if (grantResults.length > 0 && permissions.length > 0) {

                boolean granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                Tlog.e(TAG, permissions[0] + "INTERNAL PERMISSION_GRANTED?" + granted);

                if (!granted) {
                    needRationaleForPermission(permissions[0]);
                }

                RequestPermissionMsg requestPermissionMsg;
                if (requestCode == INTERNAL_PERMISSIONS_REQUEST_CODE) {
                    requestPermissionMsg = mInternalPermissionQueueCache.get(permissions[0]);
                    mInternalPermissionQueueCache.remove(permissions[0]);
                } else {
                    requestPermissionMsg = mExternalPermissionQueueCache.get(permissions[0]);
                    mExternalPermissionQueueCache.remove(permissions[0]);
                }

                if (requestPermissionMsg != null) {
                    if (requestPermissionMsg.mResult != null) {
                        requestPermissionMsg.mResult.onPermissionRequestResult(permissions[0], granted);
                    }
                }

            }
            // 不管有没有拒绝，继续请求权限
            if (mHandler != null) {
                mHandler.sendEmptyMessage(requestCode);
            }

        }

    }


    private static final class PermissionHandler extends Handler {
        private WeakReference<PermissionRequest> wr;

        private PermissionHandler(PermissionRequest mRequest, Looper mUILooper) {
            super(mUILooper);
            wr = new WeakReference<>(mRequest);
        }

        private void clear() {
            if (wr != null) {
                wr.clear();
            }
            wr = null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            PermissionRequest mPermissionRequest;

            if (wr != null && (mPermissionRequest = wr.get()) != null) {
                mPermissionRequest.handleMessage(msg);
            }

        }
    }

    private final class RequestPermissionMsg {
        private RequestPermissionMsg(OnPermissionResult mResult, String permission) {
            this.permission = permission;
            this.mResult = mResult;
        }

        private String permission;
        private OnPermissionResult mResult;
    }

    public interface OnAllPermissionFinish {

        /**
         * 权限请求完毕
         */
        void onAllPermissionRequestFinish();
    }

    public interface OnPermissionResult {

        /**
         * 哪个权限请求的结果
         *
         * @param permission 权限名称
         * @param granted    是否授予
         */
        void onPermissionRequestResult(String permission, boolean granted);
    }

}
