package cn.com.common.test.suspension;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.common.test.R;
import cn.com.swain.baselib.app.utils.CpuUtil;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.FloatPermissionHelper;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/19 0019
 * desc:
 */
public class SuspendService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private Handler mUIHandler;
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Tlog.v(" SuspendService onCreate");
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        executorService = Executors.newSingleThreadExecutor();
        mUIHandler = new Handler(Looper.getMainLooper()) {

            int times;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG_CPU) {
                    Float f = (Float) msg.obj;
                    if (mCpuInfoTxt != null) {
                        if (++times % 2 == 0) {
                            mCpuInfoTxt.setText("cpu:" + String.valueOf(f));
                        } else {
                            mCpuInfoTxt.setText("cpu " + String.valueOf(f));
                        }
                    }
                }

            }
        };
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!testCpu) {
                        synchronized (synObj) {
                            Tlog.d(" SuspendService getCurProcessCpuRate wait...");
                            try {
                                synObj.wait();
                            } catch (InterruptedException e) {
                                Tlog.e(" SuspendService getCurProcessCpuRate wait", e);
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                    float curProcessCpuRate = CpuUtil.getCurProcessCpuRate(1000);
                    Tlog.v(" curProcessCpuRate:" + curProcessCpuRate);
                    Tlog.v(" cur:" + CpuUtil.getCurUseCpuTime() + " total:" + CpuUtil.getTotalCpuTime());
                    mUIHandler.obtainMessage(MSG_CPU, curProcessCpuRate).sendToTarget();
                }
            }
        });
        showFloatingWindow();
    }

    private final Object synObj = new byte[1];

    static final int MSG_CPU = 0x01;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tlog.v(" SuspendService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private View mNavigationView;
    private TextView mCpuInfoTxt;

    @Override
    public void onDestroy() {
        Tlog.v(" SuspendService onDestroy");
        super.onDestroy();
        mUIHandler.removeCallbacksAndMessages(null);
        isStarted = false;
        testCpu = false;
        executorService.shutdown();
        if (windowManager != null) {
            if (mNavigationView != null) {
                windowManager.removeView(mNavigationView);
            }
            if (mCpuInfoTxt != null) {
                windowManager.removeView(mCpuInfoTxt);
            }
        }
    }

    private WindowManager.LayoutParams createFloatingWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT
                , WindowManager.LayoutParams.WRAP_CONTENT
//                ,WindowManager.LayoutParams.TYPE_APPLICATION
                , WindowManager.LayoutParams.TYPE_TOAST
                , 0
                , PixelFormat.OPAQUE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        layoutParams.width = 260;
//        layoutParams.height = 160;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        layoutParams.x = dm.widthPixels / 2;
        layoutParams.y = dm.heightPixels / 2;
        return layoutParams;
    }

    private synchronized void showFloatingWindow() {

        Tlog.v(" windowManager addView ");
//        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this))) {}

        if (!FloatPermissionHelper.getInstance().checkPermission(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "no float window permission", Toast.LENGTH_SHORT).show();
            return;
        }

        WindowManager.LayoutParams imgLayoutParams = createFloatingWindow();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setBackgroundResource(R.mipmap.nav);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " hello ", Toast.LENGTH_SHORT).show();
            }
        });
        imageView.setOnTouchListener(new FloatingOnTouchListener(imgLayoutParams));
        mNavigationView = imageView;
        windowManager.addView(imageView, imgLayoutParams);

        TextView mCpuTxt = new TextView(getApplicationContext());
        mCpuTxt.setClickable(true);
        mCpuTxt.setFocusable(true);
        mCpuTxt.setFocusableInTouchMode(true);
        mCpuTxt.setTextSize(20f);
        mCpuTxt.setTextColor(Color.RED);
        mCpuTxt.setText("cpu");
        WindowManager.LayoutParams txtLayoutParams = createFloatingWindow();
        txtLayoutParams.y += 16f * 6;
        FloatingOnTouchListener floatingOnTouchListener = new FloatingOnTouchListener(txtLayoutParams);
        mCpuTxt.setOnTouchListener(floatingOnTouchListener);
        mCpuTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingOnTouchListener.click) {
                    testCpuRate();
                }
            }
        });
        mCpuInfoTxt = mCpuTxt;
        windowManager.addView(mCpuTxt, txtLayoutParams);
    }


    private boolean testCpu;

    private void testCpuRate() {

        testCpu = !testCpu;

        Toast.makeText(getApplicationContext(), testCpu ? "start" : "stop", Toast.LENGTH_SHORT).show();

        if (testCpu) {
            synchronized (synObj) {
                synObj.notify();
            }
        }

    }


    private class FloatingOnTouchListener implements View.OnTouchListener {

        private boolean click = true;
        private WindowManager.LayoutParams fLayoutParams;

        private FloatingOnTouchListener(WindowManager.LayoutParams layoutParams) {
            this.fLayoutParams = layoutParams;
        }

        private int x;
        private int y;

        private int xDown;
        private int yDown;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    click = false;

                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    xDown = x;
                    yDown = y;

                    Tlog.v(" FloatingOnTouchListener ACTION_DOWN xDown" + xDown + " yDown" + yDown);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Tlog.v(" FloatingOnTouchListener ACTION_MOVE");

                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    fLayoutParams.x = fLayoutParams.x + movedX;
                    fLayoutParams.y = fLayoutParams.y + movedY;
                    windowManager.updateViewLayout(view, fLayoutParams);
                    break;
                case MotionEvent.ACTION_UP:

                    int xUp = (int) event.getRawX();
                    int yUp = (int) event.getRawY();
                    int absx = Math.abs(xUp - xDown);
                    int absy = Math.abs(yUp - yDown);

                    Tlog.v(" FloatingOnTouchListener ACTION_UP absx:" + absx + " absy:" + absy);

                    if (absx <= 20 && absy <= 20) {
                        click = true;
                    }

                    Tlog.v(" FloatingOnTouchListener ACTION_UP x" + xUp + " y" + yUp);
                    break;
                default:
                    Tlog.v(" FloatingOnTouchListener default " + event.getAction());
                    break;
            }
            return false;
        }
    }


    private void a() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        AlertDialog dialog = builder.setMessage("进入产测")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
        if (!dialog.isShowing()) {//此时提示框未显示
            dialog.show();
        }
    }

}
