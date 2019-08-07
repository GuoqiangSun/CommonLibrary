package cn.com.swain.baselib.display;

import android.graphics.PointF;

/**
 * author Guoqiang_Sun
 * date 2019/8/5
 * desc
 */
public class LinearFunction {

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
    public void calculationLinearFunction(PointF pa, PointF pb) {
        if (pa.x == pb.x) {
            k = 0;
            b = pa.x;
        } else if (pa.y == pb.y) {
            k = 0;
            b = pa.y;
        } else {
            k = (pa.y - pb.y) / (pa.x - pb.x);
            b = pa.y - pa.x * k;
        }
    }

    public float calculationX(float y) {
        return (y - b) / k;
    }

    public float calculationY(float x) {
        return k * x + b;
    }

    /**
     * 已知 k 和 点 求 函数
     */
    public void calculationLinearFunction(float k, PointF pa) {
        this.k = k;
        this.b = pa.y - k * pa.x;
    }

    /**
     * 已知本函数的 k  和 点 函数
     */
    public void calculationLinearFunction(PointF pa) {
        this.b = pa.y - this.k * pa.x;
    }

    /**
     * mLinearFunction 和 本函数的交点
     */
    public PointF intersection(LinearFunction mLinearFunction, PointF p) {
        p.x = (mLinearFunction.b - this.b) / (this.k - mLinearFunction.k);
        p.y = this.k * p.x + this.b;
        return p;
    }
}
