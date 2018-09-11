package cn.com.swain.baselib.util;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.ref.WeakReference;


/**
 * author: Guoqiang_Sun
 * date : 2018/6/9 0009
 * desc :
 */
public class PermissionRequest {

    private String TAG = "permissionRequest";

    private WeakReference<Activity> mWr;
    private String[] permissionArray;
    private int[] res;
    private PermissionHandler mHandler;
    private static final int MY_PERMISSIONS_REQUEST_FILE = 0x09;
    private OnPermissionFinish mOnPermissionFinish;

    public PermissionRequest(Activity mActivity, OnPermissionFinish mOnPermissionFinish, String[] permissionArray, int[] res) {
        this.mWr = new WeakReference<>(mActivity);
        this.mOnPermissionFinish = mOnPermissionFinish;
        this.permissionArray = permissionArray;
        this.res = res;

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

    public void requestPermission() {
        mHandler.sendEmptyMessage(REQUEST_PERMISSION);
    }

    private int mCur = 0;

    private static final int REQUEST_PERMISSION = 0x00;
    private static final int REQUEST_PERMISSION_AGAIN = 0x01;

    private void handleMessage(Message msg) {
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, " request permission activity == null ");
            return;
        }
        Application application = activity.getApplication();

        if (application == null) {
            Log.e(TAG, "request permission  application == null ");
            return;
        }

        if (msg.what == REQUEST_PERMISSION) {
            if (mCur >= permissionArray.length) {
                //finish
                Log.v(TAG, " permission request finish");
                if (mOnPermissionFinish != null) {
                    mOnPermissionFinish.onPermissionRequestFinish();
                }

                return;
            }

            String permissionStr = permissionArray[mCur];

            boolean has = (ContextCompat.checkSelfPermission(activity,
                    permissionStr) == PackageManager.PERMISSION_GRANTED);
            ++mCur;

            if (has) {

                Log.v(TAG, " Has permission " + permissionStr);
                mHandler.sendEmptyMessage(REQUEST_PERMISSION);

            } else {
                Log.v(TAG, " request permission " + permissionStr);
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionStr},
                        MY_PERMISSIONS_REQUEST_FILE);

            }

        } else if (msg.what == REQUEST_PERMISSION_AGAIN) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{(String) msg.obj},
                    MY_PERMISSIONS_REQUEST_FILE);
        }

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FILE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                    //授权被允许
                    Log.v(TAG, " PERMISSION_GRANTED " + permissions[0]);
                    mHandler.sendEmptyMessage(REQUEST_PERMISSION);

                } else {

                    int j = -1;
                    for (int i = 0; i < permissionArray.length; i++) {
                        if (permissions[0].equalsIgnoreCase(permissionArray[i])) {
                            j = i;
                        }
                    }

                    if (j < 0) {
                        j = mCur - 1;
                    }

                    Log.e(TAG, " != PERMISSION_GRANTED res j" + j + " " + permissions[0]);
//                    showMessageOKCancel(permissions[0], res[j]);

                    Activity activity = getActivity();
                    if (activity == null) {
                        Log.e(TAG, " activity==null ");
                        return;
                    }

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
                        // 权限被禁止
                        Log.v(TAG, " shouldShowRequestPermissionRationale ");
                        mHandler.sendEmptyMessage(REQUEST_PERMISSION);
                    } else {
                        mHandler.sendEmptyMessage(REQUEST_PERMISSION);
                    }

                }

            }

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

    public interface OnPermissionFinish {
        void onPermissionRequestFinish();
    }

}
