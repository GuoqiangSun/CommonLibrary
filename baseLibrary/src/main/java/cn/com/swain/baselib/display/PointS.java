package cn.com.swain.baselib.display;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.Serializable;

/**
 * author Guoqiang_Sun
 * date 2019/8/12
 * desc
 */
public class PointS implements Serializable {
    public float x;
    public float y;

    public PointS() {
    }

    public PointS(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointS(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public PointS(PointF p) {
        this.x = p.x;
        this.y = p.y;
    }

    public PointS(PointS p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public final void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void set(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Set the point's x and y coordinates to the coordinates of p
     */
    public final void set(PointF p) {
        this.x = p.x;
        this.y = p.y;
    }


    public final void set(PointS p) {
        this.x = p.x;
        this.y = p.y;
    }

    public final void negate() {
        x = -x;
        y = -y;
    }

    public final void offset(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointS pointS = (PointS) o;

        if (Float.compare(pointS.x, x) != 0) return false;
        if (Float.compare(pointS.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PointS(" + x + ", " + y + ")";
    }

    /**
     * Return the euclidian distance from (0,0) to the point
     */
    public final float length() {
        return length(x, y);
    }

    /**
     * Returns the euclidian distance from (0,0) to (x,y)
     */
    public static float length(float x, float y) {
        return (float) Math.hypot(x, y);
    }

    /**
     * Return the area  from (0,0) to the point
     */
    public final float area() {
        return x * y;
    }

}
