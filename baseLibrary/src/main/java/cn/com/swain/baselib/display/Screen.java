package cn.com.swain.baselib.display;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.Serializable;

/**
 * author Guoqiang_Sun
 * date 2019/8/6
 * desc
 */
public class Screen implements Serializable {
    public float width;
    public float height;

    public Screen() {
    }

    public Screen(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Screen(PointS mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public Screen(PointF mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public Screen(Point mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public void set(PointS mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public void set(PointF mPhoneScreen) {
        this.width = mPhoneScreen.x;
        this.height = mPhoneScreen.y;
    }

    public void set(Point mPhoneScreen) {
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
