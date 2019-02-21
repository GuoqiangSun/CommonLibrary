package cn.com.swain.baselib.view.LinearGradientTextView;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

import cn.com.swain.baselib.R;


/**
 * author: Guoqiang_Sun
 * date: 2019/2/20 0020
 * Desc:
 */

public class LinearGradientTextView extends AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;

    private int showTime;//显示的时间
    private int lineNumber;//行数
    private int showStyle;
    public static final int UNIDIRECTION = 0;
    public static final int TWOWAY = 1;
    private int color;

    public LinearGradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearGradientTextView);
        showTime = typedArray.getInteger(R.styleable.LinearGradientTextView_showTime, 40);
        lineNumber = typedArray.getInteger(R.styleable.LinearGradientTextView_lineNumber, 1);
        showStyle = typedArray.getInt(R.styleable.LinearGradientTextView_showStyle, UNIDIRECTION);
        color = typedArray.getColor(R.styleable.LinearGradientTextView_linearTextColor, Color.BLUE);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();

                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0,
//                        new int[]{0x33ffffff, 0xffffffff, 0x33ffffff},
                        new int[]{color - 0xAF000000, color, color - 0xAF000000},
                        new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);


                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            mTranslate += mViewWidth / 10;
            if (mTranslate > 2 * mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(showTime);
        }
    }
}
