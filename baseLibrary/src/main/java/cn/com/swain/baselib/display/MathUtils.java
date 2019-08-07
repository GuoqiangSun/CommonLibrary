package cn.com.swain.baselib.display;

import android.graphics.PointF;

import java.util.Arrays;
import java.util.Comparator;

import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/7
 * desc
 */
public class MathUtils {


    /**
     * 求sin值
     *
     * @param degree 角度
     * @return sin值
     */
    public static double sin(double degree) {
        return Math.sin(degree / 180 * Math.PI);
    }

    /**
     * 求sin值
     *
     * @param value
     * @return 角度
     */
    public static double asin(double value) {
        return Math.asin(value) / Math.PI * 180;
    }

    /**
     * 求cos值
     *
     * @param degree 角度
     * @return cos值
     */
    public static double cos(double degree) {
        return Math.cos(degree / 180 * Math.PI);
    }

    /**
     * 已知tan值求角度
     *
     * @param value
     * @return 角度
     */
    public static double atan(double value) {
        return Math.atan(value) / Math.PI * 180;
    }

    /**
     * 计算斜边边长
     *
     * @param A 左边点A
     * @param B 左边点B
     * @return 边长长度
     */
    public static double calculationBevel(PointF A, PointF B) {
        if (A.y == B.y) {
            return Math.abs(B.x - A.x);
        } else if (A.x == B.x) {
            return Math.abs(B.y - A.y);
        }
        return Math.sqrt(Math.pow(Math.abs(B.x - A.x), 2) + Math.pow(Math.abs(B.y - A.y), 2));
    }


    /**
     * 已知三点坐标，求 夹角
     *
     * @param cen    夹角点
     * @param first  边角点1
     * @param second 边角点1
     * @return 角度
     */
    public static float angle(PointF cen, PointF first, PointF second) {
        float dx1, dx2, dy1, dy2;
        float angle;

        dx1 = first.x - cen.x;
        dy1 = first.y - cen.y;

        dx2 = second.x - cen.x;

        dy2 = second.y - cen.y;

        float c = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        if (c == 0) return -1;

        angle = (float) (Math.acos((dx1 * dx2 + dy1 * dy2) / c) / Math.PI * 180);

        return angle;
    }

    /**
     * rotatePoint 绕 coordinatePoint 旋转 degree角度后 得到新点重新赋值 rotatePoint
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          逆时针旋转角度（从上往下旋转）
     */
    public static void rotate(PointF coordinatePoint,
                              PointF rotatePoint, double degree) {
        rotate(coordinatePoint, rotatePoint, rotatePoint, degree);
    }

    /**
     * 对坐标上任意点(x,y)，
     * 绕一个坐标点(rx0,ry0)
     * 逆时针旋转a角度后的
     * 新的坐标设为(x0, y0)，
     * 有公式：
     * <p>
     * x0= (x - rx0)*cos(a) - (y - ry0)*sin(a) + rx0 ;
     * <p>
     * y0= (x - rx0)*sin(a) + (y - ry0)*cos(a) + ry0 ;
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param newPoint        得到的新点
     * @param degree          逆时针旋转角度（从上往下旋转）
     */
    public static void rotate(PointF coordinatePoint,
                              PointF rotatePoint,
                              PointF newPoint,
                              double degree) {

        float x0 = (rotatePoint.x - coordinatePoint.x) * (float) cos(degree)
                - (rotatePoint.y - coordinatePoint.y) * (float) sin(degree)
                + coordinatePoint.x;
        float y0 = (rotatePoint.x - coordinatePoint.x) * (float) sin(degree)
                + (rotatePoint.y - coordinatePoint.y) * (float) cos(degree)
                + coordinatePoint.y;
        // 计算完后再赋值,防止 newPoint == rotatePoint;
        newPoint.x = x0;
        newPoint.y = y0;
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
    public static void sortABCD(PointF[] pointFS) {

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
    public static void sortABCD(PointF[] pointFS, PointF middlePoint) {

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

}
