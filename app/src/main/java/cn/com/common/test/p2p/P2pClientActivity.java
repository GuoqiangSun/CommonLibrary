package cn.com.common.test.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.swain.baselib.app.Tlog;
import cn.com.common.test.R;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/31 0031
 * desc :
 */
public class P2pClientActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "p2pDemo";


    private final BroadcastReceiver mWiFiP2pReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Tlog.e(TAG, "P2pClientActivity mWiFiP2pReceiver " + intent.getAction());
            assert action != null;

            switch (action) {

                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:

                    //确认WIFI-P2P是否启动
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

                        Tlog.i(TAG, " WIFI_P2P_STATE_CHANGED_ACTION wifiP2pEnabled  ");

                    } else {

                        Tlog.e(TAG, " WIFI_P2P_STATE_CHANGED_ACTION wifiP2pNotEnabled  ");
                    }
                    break;


                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        Tlog.e(TAG, " WifiP2pManager.EXTRA_NETWORK_INFO is connected ");
                    } else {
                        Tlog.e(TAG, " WifiP2pManager.EXTRA_NETWORK_INFO is not connected ");
                    }
                    break;

            }

        }
    };

    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private ExecutorService pool;
    private WifiP2pDevice wifiP2pDevice;


    private TextView mStateTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_client);

        mChannel = P2pComServerActivity.mChannel;
        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        pool = Executors.newSingleThreadExecutor();
        wifiP2pDevice = getIntent().getParcelableExtra("p2p");

        TextView mName = findViewById(R.id.txtName);
        mName.setText(String.valueOf(wifiP2pDevice.deviceName));

        TextView mAddress = findViewById(R.id.txtAddress);
        mAddress.setText(String.valueOf(wifiP2pDevice.deviceAddress));

        mStateTxt = findViewById(R.id.txtState);

        mEdtTxt = findViewById(R.id.edtData);

        mSendTxt = findViewById(R.id.txtSend);

        mReceiveTxt = findViewById(R.id.txtNotify);


        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if (++recTimes <= 100) {
                    mReceiveTxt.append(String.valueOf(msg.obj));
                } else {
                    mReceiveTxt.setText(String.valueOf(msg.obj));
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        registerReceiver(mWiFiP2pReceiver, intentFilter);
    }

    private TextView mReceiveTxt;
    private int recTimes = 0;
    private TextView mSendTxt;
    private EditText mEdtTxt;
    private Handler mUIHandler;

    public void conDevice(View v) {
        connectWiFiP2pDevice(wifiP2pDevice);
    }

    public void disconDevice(View v) {
        cancelConnect();
    }

    public void cancelConnect() {
        mWifiP2pManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Tlog.d(TAG, " cancelConnect success ");
                Toast.makeText(P2pClientActivity.this, "取消连接成功", Toast.LENGTH_SHORT).show();
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStateTxt.setText("disconnected");
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Tlog.e(TAG, " cancelConnect fail " + reason);

                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Toast.makeText(P2pClientActivity.this, "取消连接失败,设备不支持p2p", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Toast.makeText(P2pClientActivity.this, "取消连接失败,繁忙", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.NO_SERVICE_REQUESTS) {
                    Toast.makeText(P2pClientActivity.this, "取消连接失败,没有服务", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(P2pClientActivity.this, "取消连接失败,内部错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private int sendTimes;

    public void sendData(View v) {

        if (outputStream == null) {
            Toast.makeText(this, "outputStream=null", Toast.LENGTH_SHORT).show();
            return;
        }

        String s = mEdtTxt.getText().toString();
        if (++sendTimes < 100) {
            mSendTxt.append(s);
        } else {
            mSendTxt.setText(s);
        }
        final byte[] bytes = s.getBytes();

        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void connectWiFiP2pDevice(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        //需要将address,WpsInfo.PBC信息包装成WifiP2pConfig
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Tlog.d(TAG, " connect success ");
                Toast.makeText(P2pClientActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStateTxt.setText("connected");
                    }
                });
                requestInfo();
            }

            @Override
            public void onFailure(final int reason) {
                Tlog.e(TAG, " connect fail " + reason);
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStateTxt.setText("fail:" + reason);
                    }
                });


                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Toast.makeText(P2pClientActivity.this, "连接失败,设备不支持p2p", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Toast.makeText(P2pClientActivity.this, "连接失败,连接繁忙", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.NO_SERVICE_REQUESTS) {
                    Toast.makeText(P2pClientActivity.this, "连接失败,没有服务", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(P2pClientActivity.this, "连接失败,内部错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private WifiP2pInfo mDeviceInfo;

    public void requestInfo() {
        mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                Tlog.v(TAG, " onConnectionInfoAvailable " + info.toString());
                mDeviceInfo = info;
                pool.execute(P2pClientActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWiFiP2pReceiver);
        run = true;
        pool.shutdownNow();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }

    }

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;

    private boolean run = false;

    @Override
    public void run() {


        InetAddress groupOwnerAddress = mDeviceInfo.groupOwnerAddress;

        Tlog.v(TAG, " run " + mDeviceInfo.toString());

        try {

            if (groupOwnerAddress == null) {
                Tlog.e(TAG, " groupOwnerAddress = null ");
                return;
            }

            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(groupOwnerAddress.getHostAddress(), 10000);
            socket.connect(inetSocketAddress);

        } catch (IOException e) {
            e.printStackTrace();

            Tlog.e(TAG, " newSocket() ", e);
            return;
        }

        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[1024 * 2];
        run = true;

        while (run) {

            try {
                int available = inputStream.available();


                while (available > 0) {
                    int read = inputStream.read(buf);
                    String s = new String(buf, 0, read);
                    mUIHandler.obtainMessage(0, s).sendToTarget();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
