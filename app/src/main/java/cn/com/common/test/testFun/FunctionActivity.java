package cn.com.common.test.testFun;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.common.test.R;
import cn.com.swain.baselib.util.CpuUtil;
import cn.com.swain.baselib.util.PermissionRequest;
import cn.com.swain.baselib.log.Tlog;

public class FunctionActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Handler mUIHandler;
    TextView mWifiInfoTxt;

    PermissionRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        request = new PermissionRequest(this);

        final TextView mCpuInfoTxt = findViewById(R.id.cpu_info_txt);

        mWifiInfoTxt = findViewById(R.id.wifi_mac_txt);

        executorService = Executors.newCachedThreadPool();

        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG_CPU) {
                    Float f = (Float) msg.obj;
                    mCpuInfoTxt.setText(String.valueOf(f));
                }

            }
        };

    }

    static final int MSG_CPU = 0x01;

    @Override
    protected void onDestroy() {
        testCpu = false;
        executorService.shutdown();
        super.onDestroy();
        request.release();
    }

    private boolean testCpu;

    public void testCpuRate(View view) {

        testCpu = !testCpu;

        Toast.makeText(getApplicationContext(), testCpu ? "start" : "stop", Toast.LENGTH_SHORT).show();

        if (testCpu) {

            for (int i = 0; i < 1; i++) {
                final int j = i;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
                        int k = 0;
                        while (testCpu) {
                            long currentTimeMillis = System.currentTimeMillis();
                            Tlog.v("abc", j + " -- " + ++k + " -- " + dateFormat.format(currentTimeMillis));
                        }
                    }
                });
            }

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (testCpu) {
                        float curProcessCpuRate = CpuUtil.getCurProcessCpuRate(1000);
                        Tlog.v(" curProcessCpuRate:" + curProcessCpuRate);
                        Tlog.v(" cur:" + CpuUtil.getCurUseCpuTime() + " total:" + CpuUtil.getTotalCpuTime());
                        mUIHandler.obtainMessage(MSG_CPU, curProcessCpuRate).sendToTarget();
                    }
                }
            });
        }

    }

    public void getWiFiMac(View view) {


        request.requestPermission(new PermissionRequest.OnPermissionResult() {
            @Override
            public void onPermissionRequestResult(String permission, boolean granted) {

                mWifiInfoTxt.setText(getWifiMacAddress());

            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION);

    }


    private String getWifiMacAddress() {
        String defaultMac = "02:00:00:00:00:00";
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ntwInterface : interfaces) {

                if (ntwInterface.getName().equalsIgnoreCase("wlan0")) {//之前是p2p0，修正为wlan
                    byte[] byteMac = ntwInterface.getHardwareAddress();
                    StringBuilder strBuilder = new StringBuilder();
                    if (byteMac != null) {
                        for (byte aByteMac : byteMac) {
                            strBuilder.append(String
                                    .format("%02X:", aByteMac));
                        }
                    }

                    if (strBuilder.length() > 0) {
                        strBuilder.deleteCharAt(strBuilder.length() - 1);
                    }

                    return strBuilder.toString();
                }

            }
        } catch (Exception e) {
//             Log.d(TAG, e.getMessage());
        }
        return defaultMac;
    }

}
