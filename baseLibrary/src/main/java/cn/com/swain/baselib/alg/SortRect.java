package cn.com.swain.baselib.alg;

import android.graphics.PointF;

import java.util.Arrays;
import java.util.Comparator;

/**
 * author Guoqiang_Sun
 * date 2019/8/12
 * desc
 *
 /**
 * 对ABCD点按照
 * * A 点在左上
 * * B 点在左下
 * * C 点在右下
 * * D 点在右上
 * 的方式排序
 *
 * *               point :::::
 * *                <p>
 * *                A     D
 * *                B     C
 */
public class SortRect {

    public interface RectCompare<T> extends Comparator<T> {

        T create();

        float getX(T mPoint);

        float getY(T mPoint);

        void setX(T mPoint, float x);

        void setY(T mPoint, float y);

    }

    public static PointS middlePointS(PointS[] points, RectCompare<PointS> c) {
        return middlePoint(points, c);
    }

    public static PointF middlePointF(PointF[] points, RectCompare<PointF> c) {
        return middlePoint(points, c);
    }

    /**
     * 获取中间值
     */
    public static <T> T middlePoint(T[] points, RectCompare<T> c) {

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (T mPoint : points) {
            float x = c.getX(mPoint);
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }

            float y = c.getY(mPoint);
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        T t = c.create();
        c.setX(t, minX + (maxX - minX) / 2);
        c.setY(t, minY + (maxY - minY) / 2);
        return t;
    }

    /**
     * 通过中间值排序
     */
    public static <T> void sortABCDByMiddle(T[] points, RectCompare<T> c) {
        T xy = middlePoint(points, c);
        sortABCDByMiddle(points, xy, c);
    }

    /**
     * 通过中间值排序
     */
    public static <T> void sortABCDByMiddle(T[] points, T middlePoint, RectCompare<? super T> c) {
        float middleX = c.getX(middlePoint);
        float middleY = c.getY(middlePoint);
        sortABCDByMiddle(points, middleX, middleY, c);
    }

    /**
     * 通过中间值排序
     */
    public static <T> void sortABCDByMiddle(T[] points, float middleX, float middleY, final RectCompare<? super T> c) {

        T minPoint = points[0];
        T maxPoint = points[3];
        T pointB = points[1];
        T pointD = points[2];

        for (T pointF : points) {
            float x = c.getX(pointF);
            float y = c.getY(pointF);
            if (x < middleX && y < middleY) {
                minPoint = pointF;
            } else if (x < middleX && y > middleY) {
                pointB = pointF;
            } else if (x > middleX && y < middleY) {
                pointD = pointF;
            } else if (x > middleX && y > middleY) {
                maxPoint = pointF;
            }
        }

        points[0] = minPoint;
        points[1] = pointB;
        points[2] = maxPoint;
        points[3] = pointD;

    }

    /**
     * 通过array.sort排序
     */
    public static <T> void sortABCDByArea(T[] pointS, final RectCompare<? super T> c) {

        Arrays.sort(pointS, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return c.compare(o1, o2);
            }
        });

//        T minPoint = pointS[0];
        T maxPoint = pointS[3];
        T point1 = pointS[1];
        T point2 = pointS[2];

//        pointS[0] = minPoint;
        pointS[2] = maxPoint;

        float x1 = c.getX(point1);
        float y1 = c.getY(point1);
//        System.out.println("x1:" + x1 + " y1:" + y1);

        float x2 = c.getX(point2);
        float y2 = c.getY(point2);
//        System.out.println("x2:" + x2 + " y2:" + y2);

        if (x1 < x2 && y1 > y2) {
            pointS[1] = point1;
            pointS[3] = point2;
        } else if (x1 > x2 && y1 < y2) {
            pointS[3] = point1;
            pointS[1] = point2;
        } else {
            pointS[1] = point1;
            pointS[3] = point2;
        }

    }


    public static void main(String[] args) {

        P A = new P(2, 2);
        P B = new P(4, 2);
        P C = new P(3.5f, 8);
        P D = new P(5, 8);

        P[] pp = new P[4];
        pp[0] = A;
        pp[1] = B;
        pp[2] = C;
        pp[3] = D;
        long l = System.nanoTime();
        sortABCDByMiddle(pp, mRectCompare);
        System.out.println(" sortABCDByMiddle use time:" + (System.nanoTime() - l));
        for (P p : pp) {
            System.out.println(p.toString());
        }

        P[] ppp = new P[4];
        ppp[0] = A;
        ppp[1] = B;
        ppp[2] = C;
        ppp[3] = D;
        l = System.nanoTime();
        sortABCDByArea(ppp, mRectCompare);
        System.out.println(" sortABCDByArea use time:" + (System.nanoTime() - l));
        for (P p : ppp) {
            System.out.println(p.toString());
        }

    }

    private static final RectCompare mRectCompare = new RectCompare<P>() {

        @Override
        public P create() {
            return new P();
        }

        @Override
        public float getX(P mPoint) {
            return mPoint.x;
        }

        @Override
        public float getY(P mPoint) {
            return mPoint.y;
        }

        @Override
        public void setX(P mPoint, float x) {
            mPoint.x = x;
        }

        @Override
        public void setY(P mPoint, float y) {
            mPoint.y = y;
        }

        @Override
        public int compare(P o1, P o2) {
            return Float.compare(o1.x * o1.y, o2.x * o2.y);
        }
    };

    private static class P {
        private P() {
        }

        private P(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;

        @Override
        public String toString() {
            return "[x=" + x + " y=" + y + "]";
        }
    }


}
