package cn.com.swain.baselib.file;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import cn.com.swain.baselib.R;
import cn.com.swain.baselib.app.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/9 0009
 * desc :
 */
public class FilePermissionUtil {

    public static final String[] REQUEST_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int MY_PERMISSIONS_REQUEST_FILE = 0x09;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void initPermission(final Activity mActivity) {

        ArrayList<String> permissionStr = new ArrayList<>();

        for (int i = 0; i < REQUEST_PERMISSION.length; i++) {

            int permission = ContextCompat.checkSelfPermission(mActivity,
                    REQUEST_PERMISSION[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {

                Tlog.e(" add request permission " + REQUEST_PERMISSION[i]);
                permissionStr.add(REQUEST_PERMISSION[i]);

            } else {
                Tlog.v("PERMISSION_GRANTED " + REQUEST_PERMISSION[i]);
            }
        }

        String[] objects = (String[]) permissionStr.toArray(new String[permissionStr.size()]);

        ActivityCompat.requestPermissions(mActivity,
                objects,
                MY_PERMISSIONS_REQUEST_FILE);

    }

    private static void initPermission(final Activity mActivity, final String permissionStr) {

        int permission = ContextCompat.checkSelfPermission(mActivity,
                permissionStr);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            Tlog.e(" requestPermissions " + permissionStr);

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{permissionStr},
                    MY_PERMISSIONS_REQUEST_FILE);

        } else {
            Tlog.v("PERMISSION_GRANTED " + permissionStr);
        }
    }

    private static void showMessageOKCancel(final Activity mActivity, final String permission) {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {

            Tlog.v(" showMessageOKCancel " + permission);

            // Request Rationale

            new AlertDialog.Builder(mActivity)
                    .setMessage(R.string.file_allow_permission)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tlog.v(" dialog request permission ");

                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{permission},
                                    MY_PERMISSIONS_REQUEST_FILE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Tlog.v(" dialog.dismiss(); ");

                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        }

    }

    public static boolean onRequestPermissionsResult(Activity mCtx, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_FILE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                        //授权被允许
                        Tlog.v(" PERMISSION_GRANTED " + permissions[i]);
                        return true;
                    } else {

                        Tlog.e("  != PERMISSION_GRANTED " + permissions[i]);
                        showMessageOKCancel(mCtx, permissions[i]);

                    }
                }

            }

        }
        return false;
    }


}
