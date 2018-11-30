package cn.com.common.test.testFun;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.common.test.R;
import cn.com.swain.baselib.util.CpuUtil;
import cn.com.swain169.log.Tlog;

public class FunctionActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Handler mUIHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        final TextView mCpuInfoTxt = findViewById(R.id.cpu_info_txt);

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
}
