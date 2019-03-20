package cn.com.common.test.suspension;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.FloatWindowPermission;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/19 0019
 * desc:
 */
public class SuspensionActivity extends AppCompatActivity {

    private String TAG = FloatWindowPermission.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipProductDetection("123");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindowPermission.getInstance().unregFloatWindowPermissionListener();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        FloatWindowPermission.getInstance().onActivityResult(this, requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String productMac;
    private Intent mSuspendIntent;

    public void skipProductDetection(String conMac) {
//        Intent i = new Intent(this, ProductDetectionActivity.class);
//        i.putExtra(ProductDetectionActivity.NAME_CUR_DEVICE, conMac);
//        startActivity(i);
        productMac = conMac;
        skipService(productMac);

    }

    private void skipService(String conMac) {
        FloatWindowPermission instance = FloatWindowPermission.getInstance();
        instance.regFloatWindowPermissionListener(new FloatWindowPermission.OnFloatWindowPermissionLsn() {
            @Override
            public void onFloatWindowPermissionResult(boolean grant) {
                Tlog.d(TAG, " skipProductDetection canDrawOverlays " + grant);

                if (SuspendService.isStarted) {
                    Toast.makeText(SuspensionActivity.this, "NAV service is running", Toast.LENGTH_SHORT).show();
                    Tlog.d(TAG, " skipProductDetection SuspendService.isStarted ");
                    return;
                }

                if (grant) {
                    mSuspendIntent = new Intent(SuspensionActivity.this, SuspendService.class);
                    startService(mSuspendIntent);
                } else {
                    Toast.makeText(SuspensionActivity.this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                }
            }
        });
        instance.applyFloatPermission(this);

    }


}
