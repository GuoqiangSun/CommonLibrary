package cn.com.common.test.testUdp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.common.test.R;
import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain.support.udp.ISocketResult;
import cn.com.swain.support.udp.UdpLanCom;
import cn.com.swain.support.udp.UdpResponseMsg;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/23 0023
 * Desc:
 */
public class FastUdpActivity extends AppCompatActivity {

    private AbsFastUdp mFastUdp;
    EditText mSendEdt;
    TextView mSendTxt;
    TextView mRecTxt;

    EditText mSendIpEdt;
    EditText mSendPortEdt;

    TextView mLocalIpTxt;

    DateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fast_udp);


        mSendEdt = findViewById(R.id.edtData);
        mSendTxt = findViewById(R.id.txtSend);
        mRecTxt = findViewById(R.id.txtNotify);
        final TextView mUdpIpTxt = findViewById(R.id.udp_ip_txt);
        final TextView mUdpPortTxt = findViewById(R.id.udp_port_txt);


        mSendIpEdt = findViewById(R.id.udp_send_ip_edt);
        mSendPortEdt = findViewById(R.id.udp_send_port_edt);


        mLocalIpTxt = findViewById(R.id.local_ip_txt);
        mLocalIpTxt.setText(IpUtil.getLocalIpV4Address());

        dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

        final Handler mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG_REC) {
                    mRecTxt.append(String.valueOf(msg.obj));
                }

            }
        };


        mFastUdp = new UdpLanCom(LooperManager.getInstance().getWorkLooper(), new ISocketResult() {
            @Override
            public void onSocketInitResult(boolean result, final String ip, final int port) {

                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUdpIpTxt.setText(String.valueOf(ip));
                        mUdpPortTxt.setText(String.valueOf(port));
                    }
                });

            }


            @Override
            public void onSocketReceiveData(String ip, int port, byte[] data) {

                String format = dateFormat.format(new Date(System.currentTimeMillis()));
                String msg = format + " " + ip + ":" + port + StrUtil.toString(data);
                mUIHandler.obtainMessage(MSG_REC, msg).sendToTarget();

            }
        });
        mFastUdp.init();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFastUdp != null) {
            mFastUdp.release();
        }
    }

    private static final int MSG_REC = 0x00;

    public void sendData(View view) {

        UdpResponseMsg sendMsg = getSendMsg();
        if (sendMsg != null) {
            String format = dateFormat.format(new Date(System.currentTimeMillis()));
            mSendTxt.append(format + String.valueOf(sendMsg));
            mFastUdp.write(sendMsg);
        }

    }

    private boolean send = false;

    public void forSendData(View view) {

        if (send) {
            Toast.makeText(getApplicationContext(), "正在循环发送", Toast.LENGTH_SHORT).show();
            return;
        }

        send = true;
        UdpResponseMsg sendMsg = getSendMsg();
        if (sendMsg != null) {
            Toast.makeText(getApplicationContext(), "循环10次发送", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 10; i++) {
                String format = dateFormat.format(new Date(System.currentTimeMillis()));
                mSendTxt.append(format + String.valueOf(sendMsg));
                mFastUdp.writeDelay(sendMsg);
            }
        }
        send = false;
    }

    private UdpResponseMsg getSendMsg() {
        String ip = mSendIpEdt.getText().toString();

        if (!IpUtil.ipMatches(ip)) {
            Toast.makeText(getApplicationContext(), "ip input error", Toast.LENGTH_SHORT).show();
            return null;
        }

        String s1 = mSendPortEdt.getText().toString();
        int port;
        try {
            port = Integer.parseInt(s1);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "port input error", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (port < 0 || port > 65535) {
            Toast.makeText(getApplicationContext(), "port input error", Toast.LENGTH_SHORT).show();
            return null;
        }

        String s = mSendEdt.getText().toString();
        byte[] bytes;

        try {
            bytes = StrUtil.toHex(s);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), " input error", Toast.LENGTH_SHORT).show();
            return null;
        }

        UdpResponseMsg mUdpResponseMsg = new UdpResponseMsg();
        mUdpResponseMsg.data = bytes;
        mUdpResponseMsg.ip = ip;
        mUdpResponseMsg.port = port;
        return mUdpResponseMsg;

    }

    public void clearSendData(View view) {
        mSendTxt.setText("");
    }

    public void clearRecData(View view) {
        mRecTxt.setText("");
    }

    public void flushLocalIp(View view) {
        String localIpV4Address = IpUtil.getLocalIpV4Address();
        Toast.makeText(getApplicationContext(), "localIP:" + localIpV4Address, Toast.LENGTH_SHORT).show();
        mLocalIpTxt.setText(localIpV4Address);
    }

    public void getBroadIp(View view) {
        Context applicationContext = getApplicationContext();
        InetAddress broadcastAddress = IpUtil.getBroadcastAddress(applicationContext);
        String address;
        if (broadcastAddress != null) {
            address = broadcastAddress.getHostAddress();
        } else {
            address = "0:0:0:0";
        }
        Toast.makeText(applicationContext, "broadIP:" + address, Toast.LENGTH_SHORT).show();
    }


}
