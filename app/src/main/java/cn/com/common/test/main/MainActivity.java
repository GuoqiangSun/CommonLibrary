package cn.com.common.test.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.com.common.test.R;
import cn.com.common.test.testBle.BleScanActivity;
import cn.com.common.test.testProtocol.TestProtocolActivity;
import cn.com.common.test.testScrollView.ScrollViewActivity;
import cn.com.swain169.log.TFlog;
import cn.com.swain169.log.Tlog;
import cn.com.swain169.log.logRecord.AbsLogRecord;
import cn.com.swain169.log.logRecord.impl.LogRecordManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tlog.v(" MainActivity onCreate ");

        testLog();
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

    private void testLog(){
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
