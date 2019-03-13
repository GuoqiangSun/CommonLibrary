package cn.com.swain.baselib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.WindowManager;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.app.utils.StatusBarUtil;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/15 0015
 * Desc:
 */
public class PermissionHelper {

    private static String TAG = PermissionRequest.TAG;

    /**
     * 请求权限
     *
     * @param context     Context
     * @param permissions 权限
     */
    public static void requestPermission(Context context,
                                         String... permissions) {
        requestPermission(context, null, null, permissions);
    }

    /**
     * 请求权限
     *
     * @param context     Context
     * @param mResult     单条权限结果回调
     * @param permissions 权限
     */
    public static void requestPermission(Context context,
                                         PermissionRequest.OnPermissionResult mResult,
                                         String... permissions) {
        requestPermission(context, mResult, null, permissions);
    }

    /**
     * 请求权限
     *
     * @param context     Context
     * @param mFinish     所有权限请求完毕
     * @param permissions 权限
     */
    public static void requestPermission(Context context,
                                         PermissionRequest.OnPermissionFinish mFinish,
                                         String... permissions) {
        requestPermission(context, null, mFinish, permissions);
    }

    /**
     * @param context     Context
     * @param mResult     单条权限结果回调
     * @param mFinish     所有权限请求完毕
     * @param permissions 权限
     */
    public static void requestPermission(Context context,
                                         PermissionRequest.OnPermissionResult mResult,
                                         PermissionRequest.OnPermissionFinish mFinish,
                                         String... permissions) {
        final PermissionMsg msg = new PermissionMsg(mResult, mFinish, permissions);
        requestPermission(context, msg);
    }

    /**
     * 请求权限
     *
     * @param context Context
     */
    public static void requestPermission(Context context, final PermissionMsg msg) {
        PermissionActivity.start(context, msg);
    }


    /**
     * 请求权限
     *
     * @param context     Context
     * @param permissions 权限
     */
    public static void requestSinglePermission(Context context,
                                               String permissions) {
        requestSinglePermission(context, null, permissions);
    }


    /**
     * @param context     Context
     * @param mResult     单条权限结果回调
     * @param permissions 权限
     */
    public static void requestSinglePermission(Context context,
                                               PermissionRequest.OnPermissionResult mResult,
                                               String permissions) {
        final PermissionMsg msg = new PermissionMsg(mResult, permissions);
        requestSinglePermission(context, msg);
    }

    /**
     * 请求权限
     *
     * @param context Context
     */
    public static void requestSinglePermission(Context context, PermissionMsg msg) {
        String[] permissions = PermissionGroup.getPermissions(msg.permissions[0]);
        if (permissions.length > 1) {
            boolean needRequest = false;
            for (String permission : permissions) {
                if (!isGranted(context, permission)) {
                    needRequest = true;
                    break;
                }
            }
            if (needRequest) {
                requestPermission(context, msg);
            } else {
                if (msg.mResult != null) {
                    msg.mResult.onPermissionRequestResult(msg.permissions[0], true);
                }
            }
        } else {
            if (isGranted(context, msg.permissions[0])) {
                if (msg.mResult != null) {
                    msg.mResult.onPermissionRequestResult(msg.permissions[0], true);
                }
            } else {
                requestPermission(context, msg);
            }
        }
    }


    /**
     * 是否有此权限
     */
    public static boolean isGranted(Context app, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(app, permission);
    }

    private static final SparseArray<PermissionMsg> mHelpers = new SparseArray<>();

    /**
     * 清除缓存权限
     */
    public static void clearCachePermission() {
        mHelpers.clear();
    }


    public static class PermissionActivity extends Activity
            implements PermissionRequest.OnPermissionFinish, PermissionRequest.OnPermissionResult {


        public static void start(final Context context, final PermissionMsg mHelper) {
            Intent starter = new Intent(context, PermissionActivity.class);
            final int i = mHelper.hashCode();
            mHelpers.put(i, mHelper);
            starter.putExtra("permissionMsg", i);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Tlog.w(TAG, " PermissionActivity request start activity() " + i);
            context.startActivity(starter);
        }

        private PermissionRequest mRequest;
        private PermissionMsg permissionMsg;
        private int permissionMsgIndex;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            StatusBarUtil.fullscreenShowBarFontBlack(getWindow());
            super.onCreate(savedInstanceState);

            Tlog.w(TAG, " PermissionActivity request onCreate() ");

            Bundle extras = getIntent().getExtras();

            if (extras == null) {

                Tlog.w(TAG, " PermissionActivity request Bundle == null");
                finish();

                return;
            }


            permissionMsgIndex = extras.getInt("permissionMsg");
            Tlog.w(TAG, " PermissionActivity get permissionMsg " + permissionMsgIndex);


            this.permissionMsg = mHelpers.get(permissionMsgIndex);
            if (this.permissionMsg == null) {
                Tlog.w(TAG, " PermissionActivity request permissionMsg == null");
                finish();

                return;
            }


            mRequest = new PermissionRequest(this);

            mRequest.requestPermissions(this, this, this.permissionMsg.permissions);

        }

        @Override
        protected void onDestroy() {
            Tlog.w(TAG, " PermissionActivity request onDestroy() remove i:" + permissionMsgIndex);

            mHelpers.remove(permissionMsgIndex);

            if (mRequest != null) {
                mRequest.release();
                mRequest = null;
            }

            if (permissionMsg != null) {
                permissionMsg.clear();
                permissionMsg = null;
            }

            super.onDestroy();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {

            if (mRequest != null) {
                mRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            finish();
            return true;
        }

        @Override
        public void onAllPermissionRequestFinish() {

            Tlog.w(TAG, " PermissionActivity rec onAllPermissionRequestFinish ");
            if (permissionMsg != null && permissionMsg.mFinish != null) {
                permissionMsg.mFinish.onAllPermissionRequestFinish();
            }

            finish();
        }

        @Override
        public boolean onPermissionRequestResult(String permission, boolean granted) {

            if (permissionMsg != null && permissionMsg.mResult != null) {
                boolean next = permissionMsg.mResult.onPermissionRequestResult(permission, granted);
                if (!next) {
                    Tlog.w(TAG, " PermissionActivity onPermissionRequestResult abort request ");

                    if (mRequest != null) {
                        mRequest.release();
                        mRequest = null;
                    }

                    finish();
                }
            }

            return true;
        }
    }


    public static class PermissionMsg {

        private String[] permissions;
        private PermissionRequest.OnPermissionResult mResult;
        private PermissionRequest.OnPermissionFinish mFinish;

        public PermissionMsg() {

        }

        /**
         * @param permission 权限
         */
        public PermissionMsg(String... permission) {
            this(null, null, permission);
        }

        /**
         * @param mResult    单条权限结果回调
         * @param permission 权限
         */
        public PermissionMsg(PermissionRequest.OnPermissionResult mResult, String... permission) {
            this(mResult, null, permission);
        }

        /**
         * @param mFinish    所有权限请求完毕
         * @param permission 权限
         */
        public PermissionMsg(PermissionRequest.OnPermissionFinish mFinish, String... permission) {
            this(null, mFinish, permission);
        }

        /**
         * @param mResult    单条权限结果回调
         * @param mFinish    所有权限请求完毕
         * @param permission 权限
         */
        public PermissionMsg(PermissionRequest.OnPermissionResult mResult,
                             PermissionRequest.OnPermissionFinish mFinish,
                             String... permission) {
            regPermissionFinishCallBack(mFinish);
            regPermissionResultCallBack(mResult);
            regPermissions(permission);
        }

        protected PermissionMsg(Parcel in) {
            permissions = in.createStringArray();
        }

        /**
         * 注册权限
         *
         * @param permission 权限
         */
        public PermissionMsg regPermissions(String... permission) {
            this.permissions = permission;
            return this;
        }

        /**
         * 注册单条权限结果
         *
         * @param mResult 单条权限结果回调
         */
        public PermissionMsg regPermissionResultCallBack(PermissionRequest.OnPermissionResult mResult) {
            this.mResult = mResult;
            return this;
        }

        /**
         * 注册所有权限结果
         *
         * @param mFinish 所有权限请求完毕
         */
        public PermissionMsg regPermissionFinishCallBack(PermissionRequest.OnPermissionFinish mFinish) {
            this.mFinish = mFinish;
            return this;
        }

        /**
         * gc
         */
        private void clear() {

            this.permissions = null;
            this.mFinish = null;
            this.mResult = null;
        }

    }

}
