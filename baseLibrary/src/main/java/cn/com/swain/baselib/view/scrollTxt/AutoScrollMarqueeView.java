package cn.com.swain.baselib.view.scrollTxt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.com.swain.baselib.log.Tlog;


/**
 * author: Guoqiang_Sun
 * date : 2018/4/20 0020
 * desc :
 */
public class AutoScrollMarqueeView extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "AutoScrollMarqueeView";

    public AutoScrollMarqueeView(Context context) {
        super(context);
        init();
    }

    public AutoScrollMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private SurfaceHolder holder;
    private TextPaint mTextPaint;


    private void init() {
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.getHolder().setKeepScreenOn(true);
        holder = this.getHolder();
        holder.addCallback(this);

        mTextPaint = new TextPaint();
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setStyle(Paint.Style.FILL);
//        mTextPaint.setFakeBoldText(true);
        mTextPaint.setDither(true);
        mTextPaint.setFilterBitmap(true);
//        设置该项为true，将有助于文本增强在LCD屏幕上的显示效果
        mTextPaint.setSubpixelText(true);

    }

    private int mWidth;
    private int mHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        mHeight = getHeight();

        if (mDrawThread != null) {
            mDrawThread.setWidgetWH(mWidth, mHeight);
        }

        Tlog.v(TAG, " onMeasure width:" + mWidth + " height:" + mHeight);

    }


    private String mMsg;

    public void setText(String msg) {
        this.mMsg = msg;
    }

    private int speed;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setTextColor(String mColor) {
        mTextPaint.setColor(Color.parseColor(mColor));
    }

    public void setTextSize(int mTextSize) {
        mTextPaint.setTextSize(mTextSize);
    }

    private HandlerThread ht;
    private MarqueeRefreshThread mDrawThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Tlog.v(TAG, " surfaceCreated ");
        if (ht == null) {
            ht = new HandlerThread(TAG, Process.THREAD_PRIORITY_DISPLAY);
            ht.start();
        } else {
            Tlog.e(TAG, " HandlerThread!=null ");
        }
        if (mDrawThread == null) {
            mDrawThread = new MarqueeRefreshThread(holder, mTextPaint);
            mDrawThread.setWidgetWH(mWidth, mHeight);
            mDrawThread.setMsg(mMsg);
            mDrawThread.setSpeed(speed);
            mDrawThread.start();
            Handler mDrawHandler = new Handler(ht.getLooper());
            mDrawHandler.post(mDrawThread);
        } else {
            Tlog.e(TAG, " MarqueeRefreshThread!=null ");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        if (mDrawThread != null) {
            mDrawThread.setWidgetWH(mWidth, mHeight);
        }
        Tlog.v(TAG, " surfaceChanged width:" + mWidth + " height:" + mHeight);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Tlog.v(TAG, " surfaceDestroyed ");
        if (mDrawThread != null) {
            mDrawThread.stop();
            mDrawThread = null;
        }

        if (ht != null) {
            ht.quitSafely();
            ht = null;
        }

    }


    private class Memor {
        public int point;
        public float step1;
        public float step2;
    }

    private Memor mMemor = new Memor();

    @Override
    public Parcelable onSaveInstanceState() {

        Tlog.v(TAG, " view onSaveInstanceState ");

        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.point = mMemor.point;
        ss.step1 = mMemor.step1;
        ss.step1 = mMemor.step2;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        Tlog.v(TAG, " view onRestoreInstanceState ");

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mMemor.point = ss.point;
        mMemor.step1 = ss.step1;
        mMemor.step1 = ss.step2;

    }

    public static class SavedState extends BaseSavedState {
        public int point;
        public float step1 = 0.0f;
        public float step2 = 0.0f;


        private SavedState(Parcel in) {
            super(in);
            point = in.readInt();
            step1 = in.readFloat();
            step2 = in.readFloat();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(point);
            out.writeFloat(step1);
            out.writeFloat(step2);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

    }

}
