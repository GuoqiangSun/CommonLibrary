package cn.com.common.test.p2p.p2pAndroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.common.test.R;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/7 0007
 * Desc:
 */
public class P2pClientActivity extends AppCompatActivity {
    EditText mServerIpEdt;
    EditText mServerPortEdt;
    EditText mConIpEdt;
    EditText mConPortEdt;

    TextView mSendTxt;
    TextView mRecTxt;

    Handler mUIHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_client);

        mServerIpEdt = findViewById(R.id.server_ip_edt);
        mServerPortEdt = findViewById(R.id.server_port_edt);
        mConIpEdt = findViewById(R.id.con_ip_edt);
        mConPortEdt = findViewById(R.id.con_port_edt);

        mSendTxt = findViewById(R.id.txtSend);
        mRecTxt = findViewById(R.id.txtNotify);

        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 0) {
                    mSendTxt.append(String.valueOf(msg.obj));
                } else if (msg.what == 1) {
                    mRecTxt.append(String.valueOf(msg.obj));
                }

            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void clearSendData(View view) {
        mSendTxt.setText("");
    }


    public void clearRecData(View view) {
        mRecTxt.setText("");
    }


    public void init(View view) {

    }

    public void notwait(View v) {
    }

    public void conn(View v) {
    }


}
