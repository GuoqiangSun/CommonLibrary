package cn.com.swain.baselib.display;

import android.graphics.PointF;

/**
 * author Guoqiang_Sun
 * date 2019/8/6
 * desc
 */
public class Screen {
    public float width;
    public float height;

    public Screen() {
    }

    public Screen(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Screen(PointF mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public void set(PointF mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
