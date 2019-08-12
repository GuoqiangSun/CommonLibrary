package cn.com.swain.baselib.display;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * author Guoqiang_Sun
 * date 2019/8/5
 * desc
 */
public class LinearFunction implements Serializable {

    public LinearFunction() {
    }

    public LinearFunction(float k) {
        this.k = k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public void setB(float b) {
        this.b = b;
    }

    public void setKB(float k, float b) {
        this.k = k;
        this.b = b;
    }

    public float k = 0;
    public float b = 0;


    /**
     * 两点求 k b
     */
    public void calculationLinearFunction(PointS pa, PointS pb) {
        calculationLinearFunction(pa.x, pa.y, pb.x, pb.y);
    }

    /**
     * 两点求 k b
     */
    public void calculationLinearFunction(PointF pa, PointF pb) {
        calculationLinearFunction(pa.x, pa.y, pb.x, pb.y);
    }

    public void calculationLinearFunction(float ax, float ay, float bx, float by) {
        if (ax == bx) {
            k = 0;
            b = ax;
        } else if (ay == by) {
            k = 0;
            b = ay;
        } else {
            k = (ay - by) / (ax - bx);
            b = ay - ax * k;
        }
    }

    public float calculationX(float y) {
        return (y - b) / k;
    }

    public float calculationY(float x) {
        return k * x + b;
    }

    /**
     * 已知 k 和 点 求函数
     */
    public void calculationLinearFunction(float k, PointS pa) {
        this.k = k;
        this.b = pa.y - k * pa.x;
    }

    /**
     * 已知 k 和 点 求函数
     */
    public void calculationLinearFunction(float k, PointF pa) {
        this.k = k;
        this.b = pa.y - k * pa.x;
    }

    /**
     * 已知 k 和 点 求函数
     */
    public void calculationLinearFunction(float k, float ax, float ay) {
        this.k = k;
        this.b = ay - k * ax;
    }

    /**
     * 已知本函数的 k  和 点 求函数
     */
    public void calculationLinearFunction(PointS pa) {
        this.b = pa.y - this.k * pa.x;
    }

    /**
     * 已知本函数的 k  和 点 求函数
     */
    public void calculationLinearFunction(PointF pa) {
        this.b = pa.y - this.k * pa.x;
    }

    /**
     * 已知本函数的 k  和 点 求函数
     */
    public void calculationLinearFunction(float ax, float ay) {
        this.b = ay - this.k * ax;
    }

    /**
     * mLinearFunction 和 本函数的交点
     */
    public PointF intersection(LinearFunction mLinearFunction, PointF p) {
        p.x = (mLinearFunction.b - this.b) / (this.k - mLinearFunction.k);
        p.y = this.k * p.x + this.b;
        return p;
    }

    /**
     * mLinearFunction 和 本函数的交点
     */
    public PointS intersection(LinearFunction mLinearFunction, PointS p) {
        p.x = (mLinearFunction.b - this.b) / (this.k - mLinearFunction.k);
        p.y = this.k * p.x + this.b;
        return p;
    }

    /**
     * mLinearFunction 和 本函数的交点
     */
    public PointS intersection(LinearFunction mLinearFunction) {
        PointS p = new PointS();
        p.x = (mLinearFunction.b - this.b) / (this.k - mLinearFunction.k);
        p.y = this.k * p.x + this.b;
        return p;
    }

}
