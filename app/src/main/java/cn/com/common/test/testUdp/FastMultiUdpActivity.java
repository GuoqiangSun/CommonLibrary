package cn.com.common.test.testUdp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.common.test.R;
import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain.support.udp.FastUdpFactory;
import cn.com.swain.support.udp.impl.IUDPResult;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/23 0023
 * Desc:
 */
public class FastMultiUdpActivity extends AppCompatActivity {

    private AbsFastUdp mFastUdp;
    EditText mSendEdt;
    TextView mSendTxt;
    TextView mRecTxt;

    EditText mSendIpEdt;
    EditText mSendPortEdt;

    TextView mLocalIpTxt;

    DateFormat dateFormat;

    EditText mInitIpEdt;

    Handler mUIHandler;

    TextView mUdpIpTxt;

    EditText mInitPortEdt;
    Button mUdpInitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fast_multi_udp);

        mSendEdt = findViewById(R.id.edtData);
        mSendTxt = findViewById(R.id.txtSend);
        mRecTxt = findViewById(R.id.txtNotify);
        mUdpIpTxt = findViewById(R.id.udp_ip_txt);

        mSendIpEdt = findViewById(R.id.udp_send_ip_edt);
        mSendPortEdt = findViewById(R.id.udp_send_port_edt);

        mLocalIpTxt = findViewById(R.id.local_ip_txt);
        mLocalIpTxt.setText(IpUtil.getLocalIpV4Address());

        mInitIpEdt = findViewById(R.id.udp_init_ip_edt);
        mInitPortEdt = findViewById(R.id.udp_init_port_edt);

        mUdpInitBtn = findViewById(R.id.toggle_udp_btn);

        dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG_REC) {
                    mRecTxt.append(String.valueOf(msg.obj));
                }

            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFastUdp != null) {
            mFastUdp.release();
        }
        mUIHandler.removeCallbacksAndMessages(null);
    }

    public void initMultiUdp(View view) {

        if (mFastUdp != null) {
//            Toast.makeText(getApplicationContext(), "already init udp", Toast.LENGTH_SHORT).show();
            mFastUdp.release();
            return;
        }

        String s = mInitIpEdt.getText().toString();

        InetAddress groupAddress;
        try {
            groupAddress = InetAddress.getByName(s);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ip parse error", Toast.LENGTH_SHORT).show();
            return;
        }

        String s1 = mInitPortEdt.getText().toString();

        int port;
        try {
            port = Integer.parseInt(s1);
        } catch (Exception e) {
            Tlog.e(" parseInt ", e);
            Toast.makeText(getApplicationContext(), "port input error", Toast.LENGTH_SHORT).show();
            return;
        }

        // target hostname is : 234.1.1.1, 234.2.2.2, 234.3.3.3 to 234.100.100.100

        mFastUdp = FastUdpFactory.newFastMultiUdp(LooperManager.getInstance().getWorkLooper(),
                groupAddress, port, false);

        mFastUdp.regUDPSocketResult(new IUDPResult() {
            @Override
            public void onUDPInitResult(final boolean result, final String ip, final int port) {

                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), result ? "udp init success" : "udp init error", Toast.LENGTH_SHORT).show();
                        if (result) {
                            String msg = String.valueOf(ip) + ":" + String.valueOf(port);
                            mUdpIpTxt.setText(msg);
                            String msg1 = "UnInit";
                            mUdpInitBtn.setText(msg1);
                        }
                    }
                });

            }

            @Override
            public void onUDPReceiveData(String ip, int port, byte[] data) {

                String format = dateFormat.format(new Date(System.currentTimeMillis()));
                String msg = format + " " + ip + ":" + port + StrUtil.toString(data);
                mUIHandler.obtainMessage(MSG_REC, msg).sendToTarget();

            }

            @Override
            public void onUDPReleaseResult(final boolean result) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), result ? "udp unInit success" : "udp unInit error", Toast.LENGTH_SHORT).show();

                        if (result) {
                            mFastUdp = null;
                            mUdpIpTxt.setText("");
                            String msg1 = "Init";
                            mUdpInitBtn.setText(msg1);
                        }

                    }
                });
            }
        });
        mFastUdp.init();


    }

    private static final int MSG_REC = 0x00;

    public void sendData(View view) {

        if (mFastUdp == null) {
            Toast.makeText(getApplicationContext(), "not init udp", Toast.LENGTH_SHORT).show();
            return;
        }

        DatagramPacket sendMsg = getSendMsg();
        if (sendMsg != null) {
            String format = dateFormat.format(new Date(System.currentTimeMillis()));
            mSendTxt.append(format + stringValueOf(sendMsg));
            mFastUdp.send(sendMsg);
        }

    }

    private boolean send = false;

    public void forSendData(View view) {

        if (send) {
            Toast.makeText(getApplicationContext(), "正在循环发送", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mFastUdp == null) {
            Toast.makeText(getApplicationContext(), "not init udp", Toast.LENGTH_SHORT).show();
            return;
        }

        send = true;
        DatagramPacket sendMsg = getSendMsg();
        if (sendMsg != null) {
            Toast.makeText(getApplicationContext(), "循环10次发送", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 10; i++) {
                String format = dateFormat.format(new Date(System.currentTimeMillis()));
                mSendTxt.append(format + stringValueOf(sendMsg));
                mFastUdp.sendDelay(sendMsg);
            }
        }
        send = false;
    }

    private String stringValueOf(DatagramPacket sendMsg) {
        if (sendMsg == null) {
            return "null";
        }
        return IpUtil.valueOf(sendMsg);
//        return sendMsg.getAddress().getHostName() + ":" + sendMsg.getPort() + " " + StrUtil.toString(sendMsg.getData());
    }

    private DatagramPacket getSendMsg() {
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

        InetAddress byName = null;
        try {
            byName = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new DatagramPacket(bytes, bytes.length, byName, port);

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
