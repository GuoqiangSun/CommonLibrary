package cn.com.common.test.suspension;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import cn.com.common.test.R;
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
    private WindowManager.LayoutParams layoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
        Tlog.v(" SuspendService onCreate");
        isStarted = true;
        createFloatingWindow();
        showFloatingWindow();

    }

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

    @Override
    public void onDestroy() {
        Tlog.v(" SuspendService onDestroy");
        super.onDestroy();
        isStarted = false;
        if (mNavigationView != null) {
            windowManager.removeView(mNavigationView);
        }
    }

    private void createFloatingWindow() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        layoutParams = new WindowManager.LayoutParams(
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
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        layoutParams.width = 260;
//        layoutParams.height = 160;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        layoutParams.x = dm.widthPixels / 2;
        layoutParams.y = dm.heightPixels / 2;
    }

    private synchronized void showFloatingWindow() {

//        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this))) {

        if (FloatPermissionHelper.getInstance().checkPermission(getApplicationContext())) {

            Tlog.v(" windowManager addView ");
//        if (SettingsCompat.canDrawOverlays(getApplication())) {

//            Button button = new Button(getApplicationContext());
//            button.setText("detection");
//            int i = Color.parseColor("#C6E2FF");
//            button.setBackgroundColor(i);
//            windowManager.addView(button, layoutParams);

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setClickable(true);
            imageView.setFocusable(true);
            imageView.setFocusableInTouchMode(true);
            imageView.setBackgroundResource(R.mipmap.nav);
            windowManager.addView(imageView, layoutParams);

            mNavigationView = imageView;

            mNavigationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), " hello ", Toast.LENGTH_SHORT).show();
                }
            });

            mNavigationView.setOnTouchListener(new FloatingOnTouchListener());

        }
    }

    private boolean click = true;

    private class FloatingOnTouchListener implements View.OnTouchListener {
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
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
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
