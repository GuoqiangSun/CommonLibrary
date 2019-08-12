package cn.com.swain.baselib.display;

import android.graphics.PointF;

import java.util.Arrays;
import java.util.Comparator;

/**
 * author Guoqiang_Sun
 * date 2019/8/12
 * desc
 * *               pointFS :::::
 * *                <p>
 * *                A     D
 * *                B     C
 */
public class SortRect {

    public interface RectCompare<T> {
        float getX(T mPoint);

        float getY(T mPoint);

        int compare(T o1, T o2);
    }

    public static <T> float[] middlePoint(T[] pointS, RectCompare<? super T> c) {

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (T mPointF : pointS) {
            float x = c.getX(mPointF);
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }

            float y = c.getY(mPointF);
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        float[] xy = new float[2];
        xy[0] = minX + (maxX - minX) / 2;
        xy[1] = minY + (maxY - minY) / 2;
        return xy;
    }

    public static <T> void sortABCD(T[] pointS, RectCompare<? super T> c) {
        float[] xy = middlePoint(pointS, c);
        sortABCD(pointS, xy[0], xy[1], c);
    }

    public static <T> void sortABCD(T[] pointS, T middlePoint, RectCompare<? super T> c) {
        float middleX = c.getX(middlePoint);
        float middleY = c.getY(middlePoint);
        sortABCD(pointS, middleX, middleY, c);
    }

    public static <T> void sortABCD(T[] pointS, float middleX, float middleY, final RectCompare<? super T> c) {

        Arrays.sort(pointS, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return c.compare(o1, o2);
            }
        });

        T minPoint = pointS[0];
        T maxPoint = pointS[3];
        T pointB = pointS[1];
        T pointD = pointS[2];

        for (T pointF : pointS) {
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

        pointS[0] = minPoint;
        pointS[1] = pointB;
        pointS[2] = maxPoint;
        pointS[3] = pointD;

    }


    /**
     * 对ABCD点按照
     * * A 点在左上
     * * B 点在左下
     * * C 点在右下
     * * D 点在右上
     * 的方式排序
     *
     * @param pointFS :::::
     *                <p>
     *                A     D
     *                B     C
     */
    private static void sortABCD(PointS[] pointFS) {

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (PointS mPointF : pointFS) {
            if (mPointF.x < minX) {
                minX = mPointF.x;
            }
            if (mPointF.x > maxX) {
                maxX = mPointF.x;
            }

            if (mPointF.y < minY) {
                minY = mPointF.y;
            }
            if (mPointF.y > maxY) {
                maxY = mPointF.y;
            }
        }

        PointS middlePoint = new PointS();
        middlePoint.x = minX + (maxX - minX) / 2;
        middlePoint.y = minY + (maxY - minY) / 2;
        sortABCD(pointFS, middlePoint);
    }


    /**
     * 对ABCD点按照
     * * A 点在左上
     * * B 点在左下
     * * C 点在右下
     * * D 点在右上
     * 的方式排序
     *
     * @param pointFS     :::::
     *                    <p>
     *                    A     D
     *                    B     C
     * @param middlePoint ABCD 中间点
     */
    private static void sortABCD(PointS[] pointFS, PointS middlePoint) {

        Arrays.sort(pointFS, new Comparator<PointS>() {
            @Override
            public int compare(PointS o1, PointS o2) {
                return Float.compare(o1.x * o1.y, o2.x * o2.y);
            }
        });

        PointS minPointF = pointFS[0];
        int minPointIndex = 0;
        PointS maxPointF = pointFS[3];
        int maxPointIndex = 3;
        for (int i = 0; i < pointFS.length; i++) {
            if (pointFS[i].x < middlePoint.x && pointFS[i].y < middlePoint.y) {
                minPointF = pointFS[i];
                minPointIndex = i;
            } else if (pointFS[i].x > middlePoint.x && pointFS[i].y > middlePoint.y) {
                maxPointF = pointFS[i];
                maxPointIndex = i;
            }
        }
        PointS pointF1 = pointFS[1];
        PointS pointF2 = pointFS[2];
        for (int i = 0; i < pointFS.length; i++) {
            if (i != minPointIndex &&
                    pointFS[i].x < middlePoint.x && pointFS[i].y > minPointF.y) {
                pointF1 = pointFS[i];
            }
            if (i != maxPointIndex &&
                    pointFS[i].x > middlePoint.x && pointFS[i].y < maxPointF.y) {
                pointF2 = pointFS[i];
            }
        }

        pointFS[0] = minPointF;
        pointFS[1] = pointF1;
        pointFS[2] = maxPointF;
        pointFS[3] = pointF2;

    }

    /**
     * 对ABCD点按照
     * * A 点在左上
     * * B 点在左下
     * * C 点在右下
     * * D 点在右上
     * 的方式排序
     *
     * @param pointFS :::::
     *                <p>
     *                A     D
     *                B     C
     */
    private static void sortABCD(PointF[] pointFS) {

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (PointF mPointF : pointFS) {
            if (mPointF.x < minX) {
                minX = mPointF.x;
            }
            if (mPointF.x > maxX) {
                maxX = mPointF.x;
            }

            if (mPointF.y < minY) {
                minY = mPointF.y;
            }
            if (mPointF.y > maxY) {
                maxY = mPointF.y;
            }
        }

        PointF middlePoint = new PointF();
        middlePoint.x = minX + (maxX - minX) / 2;
        middlePoint.y = minY + (maxY - minY) / 2;
        sortABCD(pointFS, middlePoint);
    }


    /**
     * 对ABCD点按照
     * * A 点在左上
     * * B 点在左下
     * * C 点在右下
     * * D 点在右上
     * 的方式排序
     *
     * @param pointFS     :::::
     *                    <p>
     *                    A     D
     *                    B     C
     * @param middlePoint ABCD 中间点
     */
    private static void sortABCD(PointF[] pointFS, PointF middlePoint) {

        Arrays.sort(pointFS, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x * o1.y, o2.x * o2.y);
            }
        });

        PointF minPointF = pointFS[0];
        int minPointIndex = 0;
        PointF maxPointF = pointFS[3];
        int maxPointIndex = 3;
        for (int i = 0; i < pointFS.length; i++) {
            if (pointFS[i].x < middlePoint.x && pointFS[i].y < middlePoint.y) {
                minPointF = pointFS[i];
                minPointIndex = i;
            } else if (pointFS[i].x > middlePoint.x && pointFS[i].y > middlePoint.y) {
                maxPointF = pointFS[i];
                maxPointIndex = i;
            }
        }
        PointF pointF1 = pointFS[1];
        PointF pointF2 = pointFS[2];
        for (int i = 0; i < pointFS.length; i++) {
            if (i != minPointIndex &&
                    pointFS[i].x < middlePoint.x && pointFS[i].y > minPointF.y) {
                pointF1 = pointFS[i];
            }
            if (i != maxPointIndex &&
                    pointFS[i].x > middlePoint.x && pointFS[i].y < maxPointF.y) {
                pointF2 = pointFS[i];
            }
        }

        pointFS[0] = minPointF;
        pointFS[1] = pointF1;
        pointFS[2] = maxPointF;
        pointFS[3] = pointF2;

    }


    public static void main(String[] args) {

        P A = new P(2, 2);
        P B = new P(4, 2);
        P C = new P(2, 8);
        P D = new P(4, 8);

        P[] pp = new P[4];
        pp[0] = A;
        pp[1] = B;
        pp[2] = C;
        pp[3] = D;

        sortABCD(pp, new RectCompare<P>() {
            @Override
            public float getX(P mPoint) {
                return mPoint.x;
            }

            @Override
            public float getY(P mPoint) {
                return mPoint.y;
            }

            @Override
            public int compare(P o1, P o2) {
                return Float.compare(o1.x * o1.y, o2.x * o2.y);
            }
        });

        for (P p : pp) {
            System.out.println(p.toString());
        }

    }

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
