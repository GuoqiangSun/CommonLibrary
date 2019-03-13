package cn.com.common.test.ir;

import android.hardware.ConsumerIrManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.com.common.test.R;

/**
 * @author: Guoqiang_Sun
 * @date: 2019/3/7 0007
 * @desc:
 */

public class InfraredActivity extends AppCompatActivity {
    //获取红外控制类
    private ConsumerIrManager IR;
    //判断是否有红外功能
    boolean IRBack;

    String TAG = "ir";

    Handler uiHandler;
    TextView mTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir);

        uiHandler = new Handler(Looper.getMainLooper());
        mTxt = findViewById(R.id.show_txt);

        new Thread() {
            @Override
            public void run() {
                super.run();
                findUsb();

            }
        }.start();
        inItEvent();
    }


    private void findUsb() {
        detectUsbDeviceWithUsbManager();
        detectInputDeviceWithShell();
    }

    private void detectUsbDeviceWithUsbManager() {
        UsbManager systemService = (UsbManager) getSystemService(USB_SERVICE);
        if (systemService == null) {

            return;
        }
        HashMap<String, UsbDevice> deviceHashMap =
                systemService.getDeviceList();

        for (Map.Entry entry : deviceHashMap.entrySet()) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    String s = "detectUsbDeviceWithUsbManager: " + String.valueOf(entry.getKey())
                            + ", " + String.valueOf(entry.getValue());

                    mTxt.append(s);


                }
            });
        }
    }

    private void detectInputDeviceWithShell() {
        try {
            //获得外接USB输入设备的信息
            Process p = Runtime.getRuntime().exec("cat /proc/bus/input/devices");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                String deviceInfo = line.trim();
                //对获取的每行的设备信息进行过滤，获得自己想要的。
//                if (deviceInfo.contains("Name="))
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTxt.append("detectInputDeviceWithShell: " + String.valueOf(deviceInfo));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化事务
    private void inItEvent() {
        //获取ConsumerIrManager实例
        IR = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        IRBack = false;
        //如果sdk版本大于4.4才进行是否有红外的功能（手机的android版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (IR != null) {
                IRBack = IR.hasIrEmitter();
            }

        }

        if (!IRBack) {
            Toast.makeText(getApplicationContext(), "对不起，该设备上没有红外功能!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "红外设备就绪!", Toast.LENGTH_SHORT).show();

            // 获得可用的载波频率范围
            ConsumerIrManager.CarrierFrequencyRange[] freqs = IR
                    .getCarrierFrequencies();
            // 边里获取频率段
            if (freqs != null) {
                for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
                    Toast.makeText(getApplicationContext(), String.format(Locale.CHINA, "  红外载波频率  %d - %d\n",
                            range.getMinFrequency(), range.getMaxFrequency()), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "没获取到红外载波频率", Toast.LENGTH_SHORT).show();
            }

        }


    }

    /**
     * 发射红外信号
     *
     * @param carrierFrequency 红外传输的频率，一般的遥控板都是38KHz
     * @param pattern          指以微秒为单位的红外开和关的交替时间
     */
    private void sendMsg(int carrierFrequency, int[] pattern) {
        IR.transmit(carrierFrequency, pattern);
    }

    public void send(View view) {

        // 一种交替的载波序列模式，通过毫秒测量
        int[] pattern = {1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625,
                442, 625, 442, 625, 468, 625, 442, 625, 494, 572, 1614,
                625, 1588, 625, 1614, 625, 494, 572, 442, 651, 442, 625,
                442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442,
                625, 494, 598, 442, 625, 442, 625, 520, 572, 442, 625, 442,
                625, 442, 651, 1588, 625, 1614, 625, 1588, 625, 1614, 625,
                1588, 625, 48958};

        // 在38.4KHz条件下进行模式转换
        sendMsg(38400, pattern);
    }


}
