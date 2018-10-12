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
import java.util.Queue;

import cn.com.swain.baselib.Queue.SyncLimitQueue;
import cn.com.swain169.log.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/6/9 0009
 * desc :
 */
public class PermissionRequest {

    private String TAG = "permissionRequest";

    private WeakReference<Activity> mWr;
    private PermissionHandler mHandler;
    private OnPermissionResult mOnPermissionFinish;

    public PermissionRequest(Activity mActivity, OnPermissionResult mOnPermissionFinish) {
        this.mWr = new WeakReference<>(mActivity);
        this.mOnPermissionFinish = mOnPermissionFinish;
        this.mHandler = new PermissionHandler(this, Looper.getMainLooper());
    }

    public void release() {
        if (mWr != null) {
            mWr.clear();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private Activity getActivity() {
        return (mWr != null ? mWr.get() : null);
    }


    private Queue<String> mInternalPermissionQueue = new SyncLimitQueue<>(12);


    /**
     * 初始化用
     */
    public void requestAllPermission(String... permissionArray) {

        if (permissionArray != null && permissionArray.length > 0) {
            for (String s : permissionArray) {
                mInternalPermissionQueue.offer(s);
            }
        }

        mHandler.sendEmptyMessage(INTERNAL_PERMISSIONS_REQUEST_CODE);
    }

    private Queue<String> mExternalPermissionQueue = new SyncLimitQueue<>(12);

    /**
     * 外部请求权限
     */
    public void requestPermissionOnce(String permissionStr) {
        mExternalPermissionQueue.offer(permissionStr);
        mHandler.sendEmptyMessage(EXTERNAL_PERMISSIONS_REQUEST_CODE);
    }

    private static final int INTERNAL_REQUEST_PERMISSION_FINISH_WHAT = 0x01;

    private static final int INTERNAL_PERMISSIONS_REQUEST_CODE = 0x8362;

    private static final int EXTERNAL_PERMISSIONS_REQUEST_CODE = 0x8363;

    private void handleMessage(Message msg) {

        switch (msg.what) {

            case INTERNAL_REQUEST_PERMISSION_FINISH_WHAT:
                if (mOnPermissionFinish != null) {
                    mOnPermissionFinish.onAllPermissionRequestFinish();
                }
                break;

            case INTERNAL_PERMISSIONS_REQUEST_CODE:

                String internalPermissionStr = mInternalPermissionQueue.poll();
                if (internalPermissionStr == null) {
                    mHandler.sendEmptyMessage(INTERNAL_REQUEST_PERMISSION_FINISH_WHAT);
                    return;
                }

                if (checkSelfPermission(internalPermissionStr)) {

                    if (mOnPermissionFinish != null) {
                        mOnPermissionFinish.onPermissionRequestResult(internalPermissionStr, true);
                    }

                    mHandler.sendEmptyMessage(INTERNAL_PERMISSIONS_REQUEST_CODE);

                } else {
                    requestPermission(internalPermissionStr, INTERNAL_PERMISSIONS_REQUEST_CODE);
                }

                break;


            case EXTERNAL_PERMISSIONS_REQUEST_CODE:

                String externalPermissionStr = mExternalPermissionQueue.poll();
                if (externalPermissionStr == null) {
                    return;
                }

                if (checkSelfPermission(externalPermissionStr)) {
                    if (mOnPermissionFinish != null) {
                        mOnPermissionFinish.onPermissionRequestResult(externalPermissionStr, true);
                    }

                    mHandler.sendEmptyMessage(EXTERNAL_PERMISSIONS_REQUEST_CODE);

                } else {
                    requestPermission(externalPermissionStr, EXTERNAL_PERMISSIONS_REQUEST_CODE);
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

                if (mOnPermissionFinish != null) {
                    mOnPermissionFinish.onPermissionRequestResult(permissions[0], granted);
                }

            }
            // 不管有没有拒接，继续请求权限
            mHandler.sendEmptyMessage(requestCode);

        }

    }


    private static final class PermissionHandler extends Handler {
        private WeakReference<PermissionRequest> wr;

        private PermissionHandler(PermissionRequest mRequest, Looper mUILooper) {
            super(mUILooper);
            wr = new WeakReference<>(mRequest);
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

    public interface OnPermissionResult {

        /**
         * 权限请求完毕
         */
        void onAllPermissionRequestFinish();


        /**
         * 哪个权限请求的结果
         *
         * @param permission 权限名称
         * @param granted    是否授予
         */
        void onPermissionRequestResult(String permission, boolean granted);
    }

}
