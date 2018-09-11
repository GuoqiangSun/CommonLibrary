package cn.com.common.test.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.com.common.test.R;
import cn.com.common.test.testBle.BleScanActivity;
import cn.com.common.test.testScrollView.ScrollViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void skipBleScan(View v) {
        startActivity(new Intent(this, BleScanActivity.class));
    }

    public void skipScrollTxt(View v) {
        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void skipP2p(View v) {
//        startActivity(new Intent(this, P2pComServerActivity.class));
    }

}
