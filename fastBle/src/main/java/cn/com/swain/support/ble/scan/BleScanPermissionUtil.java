package cn.com.swain.support.ble.scan;

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

import cn.com.swain.support.ble.R;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/16 0016
 * desc :
 */
public class BleScanPermissionUtil {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0x08;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void initPermission(final Activity mActivity) {

        int permission = ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            Tlog.v(" request BleScan permission ");

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            Tlog.v("  BleScan ACCESS_COARSE_LOCATION permission " + permission);
        }
    }

    private static void showMessageOKCancel(final Activity mActivity) {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Tlog.v(" BleScan shouldShowRequestPermissionRationale  READ_CONTACTS");

            // Request Rationale

            new AlertDialog.Builder(mActivity)
                    .setMessage(R.string.scan_ble_allow_permission)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tlog.v(" BleScan ok ; request permission ");

                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Tlog.v("BleScan cancel; dialog.dismiss(); ");

                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        }

    }

    public static void onRequestPermissionsResult(Activity mCtx, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //授权被允许

                Tlog.v(" BleScan onRequestPermissionsResult PERMISSION_GRANTED ");

            } else {

                Tlog.e(" BleScan onRequestPermissionsResult != PERMISSION_GRANTED ");
                if (permissions.length > 0) {
                    if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showMessageOKCancel(mCtx);
                    }
                }
//
//                //这里进行权限被拒绝的处理，就跳转到本应用的程序管理器
//                Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
//
//                String pkg = "com.android.settings";
//                String cls = "com.android.settings.applications.InstalledAppDetails";
//
//                i.setComponent(new ComponentName(pkg, cls));
//                i.setData(Uri.parse("package:" + mCtx.getPackageName()));
//                mCtx.startActivity(i);

            }
        }
    }


}
