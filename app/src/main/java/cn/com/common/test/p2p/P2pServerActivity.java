package cn.com.common.test.p2p;

import android.content.Context;
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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
public class P2pServerActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "p2pDemo";

    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private ExecutorService pool;

    private TextView mStateTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_server);

        mChannel = P2pComServerActivity.mChannel;
        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        pool = Executors.newSingleThreadExecutor();

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

    }


    private TextView mReceiveTxt;
    private int recTimes = 0;
    private TextView mSendTxt;
    private EditText mEdtTxt;
    private Handler mUIHandler;


    public void createGroup(View v) {
        mWifiP2pManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Tlog.d(TAG, "createGroup success");
                Toast.makeText(P2pServerActivity.this, "创建群组成功", Toast.LENGTH_SHORT).show();
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStateTxt.setText("createSuccess");
                    }
                });
                pool.execute(P2pServerActivity.this);
            }

            @Override
            public void onFailure(final int reason) {
                Tlog.e(TAG, "createGroup fail :" + reason);

                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStateTxt.setText("createFail:" + reason);
                    }
                });

                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Toast.makeText(P2pServerActivity.this, "创建群组失败,设备不支持p2p", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Toast.makeText(P2pServerActivity.this, "创建群组失败,请移除已有的组群或者连接另一WIFI重试", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.NO_SERVICE_REQUESTS) {
                    Toast.makeText(P2pServerActivity.this, "创建群组失败,没有服务", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(P2pServerActivity.this, "创建群组失败,内部错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void removeGroup(View v) {
        mWifiP2pManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Tlog.d(TAG, "removeGroup success");
                Toast.makeText(P2pServerActivity.this, "移除组群成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Tlog.e(TAG, "removeGroup fail : " + reason);

                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Toast.makeText(P2pServerActivity.this, "移除群组失败,设备不支持p2p", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Toast.makeText(P2pServerActivity.this, "移除组群失败,请创建组群重试", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.NO_SERVICE_REQUESTS) {
                    Toast.makeText(P2pServerActivity.this, "移除组群失败,请创建组群重试", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(P2pServerActivity.this, "移除群组失败,内部错误", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        if (mServer != null) {
            try {
                mServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;

    private boolean run = false;
    private ServerSocket mServer;

    @Override
    public void run() {

        try {
//            socket = new Socket(groupOwnerAddress, 1000);

            mServer = new ServerSocket();
            mServer.setReuseAddress(true);
            mServer.bind(new InetSocketAddress(10000));
            Tlog.v(TAG, "new ServerSocket() success ");

            socket = mServer.accept();

            Tlog.v(TAG, "server accept a socket ");

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " server error ", e);
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
                Tlog.e(TAG, " server socket error ", e);
            }

        }

    }
}
