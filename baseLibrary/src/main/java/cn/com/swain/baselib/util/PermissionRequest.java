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
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/9 0009
 * desc :
 */
public final class PermissionRequest {

    public static final String TAG = "permissionRequest";

    private WeakReference<Activity> mWr;
    private PermissionHandler mHandler;

    public PermissionRequest(Activity mActivity) {
        this.mWr = new WeakReference<>(mActivity);
        this.mHandler = new PermissionHandler(this, Looper.getMainLooper());
    }

    public void release() {
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
    public void requestPermissions(String... permissionArray) {
        requestPermissions(null, null, permissionArray);
    }


    /**
     * 请求所有的权限
     *
     * @param mAllFinish      所有权限请求完毕回调
     * @param permissionArray 权限
     */
    public void requestPermissions(OnAllPermissionFinish mAllFinish, String... permissionArray) {
        requestPermissions(mAllFinish, null, permissionArray);
    }


    /**
     * 请求所有的权限
     *
     * @param mResult         结果回调
     * @param permissionArray 权限
     */
    public void requestPermissions(OnPermissionResult mResult, String... permissionArray) {
        requestPermissions(null, mResult, permissionArray);

    }


    private final Queue<RequestPermissionMsg> mInternalPermissionQueue = new SyncLimitQueue<>(12);
    private final Map<String, RequestPermissionMsg> mInternalPermissionQueueCache =
            Collections.synchronizedMap(new HashMap<String, RequestPermissionMsg>());

    /**
     * 请求所有的权限
     *
     * @param mAllFinish      所有权限请求完毕回调
     * @param mResult         单个权限请求结果
     * @param permissionArray 权限
     */
    public void requestPermissions(OnAllPermissionFinish mAllFinish, OnPermissionResult mResult, String... permissionArray) {

        final int size = mInternalPermissionQueue.size();
        int offer = 0;
        if (permissionArray != null && permissionArray.length > 0) {
            for (String s : permissionArray) {
                if (s != null && !"".equals(s)) {
                    RequestPermissionMsg mMsg = new RequestPermissionMsg(mResult, s, null);
                    mInternalPermissionQueue.offer(mMsg);
                    offer++;
                }
            }
        }

        if (mAllFinish != null) {
            RequestPermissionMsg mMsg = new RequestPermissionMsg(null, null, mAllFinish);
            mInternalPermissionQueue.offer(mMsg);
            offer++;
        }

        if (size <= 0) {
            if (offer > 0 && mHandler != null) {
                mHandler.sendEmptyMessage(INTERNAL_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            final int size1 = mInternalPermissionQueue.size();
            if (size1 == offer && offer > 0 && mHandler != null && !mHandler.hasMessages(INTERNAL_PERMISSIONS_REQUEST_CODE)) {
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
        if (permissionStr == null || "".equals(permissionStr)) {
            if (mResult != null) {
                mResult.onPermissionRequestResult(permissionStr, false);
            }
            return;
        }
        final int size = mExternalPermissionQueue.size();
        RequestPermissionMsg mMsg = new RequestPermissionMsg(mResult, permissionStr, null);
        mExternalPermissionQueue.offer(mMsg);
        if (size <= 0) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(EXTERNAL_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            final int size1 = mExternalPermissionQueue.size();
            if (size1 == 1) {
                if (mHandler != null && !mHandler.hasMessages(EXTERNAL_PERMISSIONS_REQUEST_CODE)) {
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

                RequestPermissionMsg internalPermissionMsgFinish = (RequestPermissionMsg) msg.obj;

                if (internalPermissionMsgFinish != null && internalPermissionMsgFinish.mAllFinish != null) {
                    internalPermissionMsgFinish.mAllFinish.onAllPermissionRequestFinish();
                }

                break;

            case INTERNAL_PERMISSIONS_REQUEST_CODE:

                RequestPermissionMsg internalPermissionMsg = mInternalPermissionQueue.poll();
                if (internalPermissionMsg == null || internalPermissionMsg.permission == null) {
                    if (mHandler != null) { // finish
                        mHandler.obtainMessage(INTERNAL_REQUEST_PERMISSION_FINISH_WHAT, internalPermissionMsg).sendToTarget();
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

            String[] permissions = PermissionConstants.getPermissions(permissionStr);

            ActivityCompat.requestPermissions(activity,
                    permissions,
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

                String permission = PermissionConstants.forPermission(permissions);

                boolean granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                Tlog.e(TAG, " onRequestPermissionsResult " + permission + " granted:" + granted);

//                if (!granted) {
//                    needRationaleForPermission(permission);
//                }

                RequestPermissionMsg requestPermissionMsg;
                if (requestCode == INTERNAL_PERMISSIONS_REQUEST_CODE) {
                    requestPermissionMsg = mInternalPermissionQueueCache.get(permission);
                    mInternalPermissionQueueCache.remove(permission);
                } else {
                    requestPermissionMsg = mExternalPermissionQueueCache.get(permission);
                    mExternalPermissionQueueCache.remove(permission);
                }

                if (requestPermissionMsg != null) {
                    if (requestPermissionMsg.mResult != null) {
                        requestPermissionMsg.mResult.onPermissionRequestResult(permission, granted);
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
        private RequestPermissionMsg(OnPermissionResult mResult, String permission, OnAllPermissionFinish mAllFinish) {
            this.permission = permission;
            this.mResult = mResult;
            this.mAllFinish = mAllFinish;
        }

        private OnAllPermissionFinish mAllFinish;
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
