package cn.com.common.test.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.com.common.test.R;
import cn.com.common.test.global.FileManager;
import cn.com.common.test.ir.InfraredActivity;
import cn.com.common.test.light.ColorLightActivity;
import cn.com.common.test.p2p.p2pAndroid.P2pClientActivity;
import cn.com.common.test.scanOR.ScanORCodeActivity;
import cn.com.common.test.shake.ShakeActivity;
import cn.com.common.test.testBle.BleScanActivity;
import cn.com.common.test.testFun.FunctionActivity;
import cn.com.common.test.testProtocol.TestProtocolActivity;
import cn.com.common.test.testScrollView.LinearViewActivity;
import cn.com.common.test.testScrollView.ScrollViewActivity;
import cn.com.common.test.testUdp.FastMultiUdpActivity;
import cn.com.common.test.testUdp.FastUdpActivity;
import cn.com.common.test.testscreen.OrientationActivity;
import cn.com.common.test.usb.DeviceListActivity;
import cn.com.swain.baselib.log.TFlog;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.PermissionGroup;
import cn.com.swain.baselib.permission.PermissionHelper;
import cn.com.swain.baselib.permission.PermissionRequest;

public class MainActivity extends AppCompatActivity {


    private void testTFLog() {
        TFlog.v("TFlog", " test Tlog v");
        TFlog.v("TFlog", " test Tlog v", new Throwable());
        TFlog.v("TFlog", new Throwable());

        TFlog.d("TFlog", " test Tlog d");
        TFlog.d("TFlog", new Throwable());
        TFlog.d("TFlog", " test Tlog d", new Throwable());

        TFlog.i("TFlog", " test Tlog i");
        TFlog.i("TFlog", new Throwable());
        TFlog.i("TFlog", " test Tlog i", new Throwable());

        TFlog.w("TFlog", " test Tlog w");
        TFlog.w("TFlog", new Throwable());
        TFlog.w("TFlog", " test Tlog w", new Throwable());

        TFlog.e("TFlog", " test Tlog e");
        TFlog.e("TFlog", new Throwable());
        TFlog.e("TFlog", " test Tlog e", new Throwable());

        TFlog.a("TFlog", " test Tlog a");
        TFlog.a("TFlog", new Throwable());
        TFlog.a("TFlog", " test Tlog a", new Throwable());

    }

    private void testLog() {
        Tlog.v("swain", " test Tlog v");
        Tlog.v("swain", " test Tlog v", new Throwable());
        Tlog.v("swain", new Throwable());

        Tlog.d("swain", " test Tlog d");
        Tlog.d("swain", new Throwable());
        Tlog.d("swain", " test Tlog d", new Throwable());

        Tlog.i("swain", " test Tlog i");
        Tlog.i("swain", new Throwable());
        Tlog.i("swain", " test Tlog i", new Throwable());

        Tlog.w("swain", " test Tlog w");
        Tlog.w("swain", new Throwable());
        Tlog.w("swain", " test Tlog w", new Throwable());

        Tlog.e("swain", " test Tlog e");
        Tlog.e("swain", new Throwable());
        Tlog.e("swain", " test Tlog e", new Throwable());

        Tlog.a("swain", " test Tlog a");
        Tlog.a("swain", new Throwable());
        Tlog.a("swain", " test Tlog a", new Throwable());

    }

    private PermissionRequest mPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Tlog.startRecord();

        mPermissionRequest = new PermissionRequest(this);
        mPermissionRequest.requestPermissions(new PermissionRequest.OnPermissionResult() {
            @Override
            public boolean onPermissionRequestResult(String permission, boolean granted) {
                Tlog.v(" OrientationActivity requestPermissions " + permission + " " + granted);
                if (granted) {
                    Tlog.setLogRecordDebug(true);
                    Tlog.set(FileManager.getInstance().getLogPath());
                    testLog();
                }
                return true;
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionGroup.LOCATION);

        Tlog.v(" OrientationActivity onCreate ");

        Tlog.p(" OrientationActivity onCreate p ");


    }

    @Override
    protected void onDestroy() {
        Tlog.v(" OrientationActivity onDestroy ");
        Tlog.stopRecord();
        if (mPermissionRequest != null) {
            mPermissionRequest.release();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionRequest != null) {
            mPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void skipBleScan(View v) {
        startActivity(new Intent(this, BleScanActivity.class));
    }

    public void skipScrollTxt(View v) {
        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void skipProtocol(View v) {
        startActivity(new Intent(this, TestProtocolActivity.class));
    }

    public void skipFastUdp(View view) {
        startActivity(new Intent(this, FastUdpActivity.class));
    }


    public void skipMultiUdp(View view) {
        startActivity(new Intent(this, FastMultiUdpActivity.class));
    }

    public void scanOR(View view) {
        startActivity(new Intent(this, ScanORCodeActivity.class));
    }


    public void skipFun(View view) {
        startActivity(new Intent(this, FunctionActivity.class));
    }


    public void p2pClient(View view) {
        startActivity(new Intent(this, P2pClientActivity.class));
    }

    public void shake(View view) {
        startActivity(new Intent(this, ShakeActivity.class));
    }

    public void requestPermission(View view) {

        PermissionHelper.requestPermission(this, new PermissionRequest.OnPermissionResult() {
            @Override
            public boolean onPermissionRequestResult(String permission, boolean granted) {

                Tlog.d(permission + " granted: " + granted);

                if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
                    return true;
                }

                return true;
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        boolean granted = PermissionHelper.isGranted(this, PermissionGroup.LOCATION);
        Tlog.d("PermissionGroup.LOCATION granted: " + granted);
    }

    public void requestPermission1(View view) {

        PermissionHelper.requestSinglePermission(this, new PermissionRequest.OnPermissionResult() {
            @Override
            public boolean onPermissionRequestResult(String permission, boolean granted) {
                Tlog.d(permission + " granted: " + granted);
                return false;
            }
        }, PermissionGroup.LOCATION);

    }


    public void colorLight(View view) {
        startActivity(new Intent(this, ColorLightActivity.class));
    }

    public void linearTxt(View view) {
        startActivity(new Intent(this, LinearViewActivity.class));
    }

    public void screenRate(View view) {
        startActivity(new Intent(this, OrientationActivity.class));
    }

    public void skipIr(View view) {
        startActivity(new Intent(this, InfraredActivity.class));
    }

    public void skipUsb(View view) {
        startActivity(new Intent(this, DeviceListActivity.class));
    }
}
