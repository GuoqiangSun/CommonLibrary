package cn.com.common.test.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.com.common.test.R;
import cn.com.common.test.testBle.BleScanActivity;
import cn.com.common.test.testProtocol.TestProtocolActivity;
import cn.com.common.test.testScrollView.ScrollViewActivity;
import cn.com.swain169.log.Tlog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tlog.v(" MainActivity onCreate ");
        Tlog.v("swain", " MainActivity onCreate 2 ");
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

}
