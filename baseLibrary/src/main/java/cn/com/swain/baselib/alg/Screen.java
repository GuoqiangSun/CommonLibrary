package cn.com.swain.baselib.alg;

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

    public Screen(float[] xy) {
        if (xy != null && xy.length >= 2) {
            this.width = xy[0];
            this.height = xy[1];
        }
    }

    public Screen(PointS mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public Screen(PointF mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public Screen(Point mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public Screen(Screen mScreen) {
        this.width = mScreen.width;
        this.height = mScreen.height;
    }

    public void set(PointS mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public void set(PointF mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public void set(Point mScreen) {
        this.width = mScreen.x;
        this.height = mScreen.y;
    }

    public void set(Screen mScreen) {
        this.width = mScreen.width;
        this.height = mScreen.height;
    }

    public void set(float[] xy) {
        if (xy != null && xy.length >= 2) {
            this.width = xy[0];
            this.height = xy[1];
        }
    }

    public void set(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
