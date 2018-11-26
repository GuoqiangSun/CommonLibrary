package cn.com.common.test.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.com.common.test.R;
import cn.com.common.test.testBle.BleScanActivity;
import cn.com.common.test.testProtocol.TestProtocolActivity;
import cn.com.common.test.testScrollView.ScrollViewActivity;
import cn.com.common.test.testUdp.FastUdpActivity;
import cn.com.swain169.log.TFlog;
import cn.com.swain169.log.Tlog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TFlog.startRecord();


        Tlog.v(" MainActivity onCreate ");
        testLog();

//        testTFLog();
    }

    @Override
    protected void onDestroy() {
        TFlog.stopRecord();
        super.onDestroy();
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


}
