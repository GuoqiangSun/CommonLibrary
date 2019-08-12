package cn.com.swain.baselib.display;

import android.graphics.PointF;

import java.util.Arrays;
import java.util.Comparator;

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
        return Math.sin(Math.toRadians(degree));
    }

    /**
     * 求sin值
     *
     * @param value
     * @return 角度
     */
    public static double asin(double value) {
        return Math.toDegrees(Math.asin(value));
    }

    /**
     * 求cos值
     *
     * @param degree 角度
     * @return cos值
     */
    public static double cos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    /**
     * 求cos值
     *
     * @param value
     * @return 角度
     */
    public static double acos(double value) {
        return Math.toDegrees(Math.acos(value));
    }

    /**
     * 求cos值
     *
     * @param degree 角度
     * @return cos值
     */
    public static double tan(double degree) {
        return Math.tan(Math.toRadians(degree));
    }

    /**
     * 已知tan值求角度
     *
     * @param value
     * @return 角度
     */
    public static double atan(double value) {
        return Math.toDegrees(Math.atan(value));
    }

    /**
     * 计算斜边边长
     *
     * @param A 左边点A
     * @param B 左边点B
     * @return 边长长度
     */
    public static double calculationBevel(PointS A, PointS B) {
        return calculationBevel(A.x, A.y, B.x, B.y);
    }

    /**
     * 计算斜边边长
     *
     * @param A 左边点A
     * @param B 右边点B
     * @return 边长长度
     */
    public static double calculationBevel(PointF A, PointF B) {
        return calculationBevel(A.x, A.y, B.x, B.y);
    }

    /**
     * 计算斜边边长
     *
     * @param ax 左边点x
     * @param ay 左边点y
     * @param bx 右边点x
     * @param by 右边点y
     * @return 边长长度
     */
    public static double calculationBevel(float ax, float ay, float bx, float by) {
        if (ay == by) {
            return Math.abs(bx - ax);
        } else if (ax == bx) {
            return Math.abs(by - ay);
        }
        return Math.sqrt(Math.pow(Math.abs(bx - ax), 2) + Math.pow(Math.abs(by - ay), 2));
    }


    /**
     * 已知 开始点,角度 和斜边边长 ,求结束点
     *
     * @param mAngle     角度
     * @param bevel      边长
     * @param startPoint 开始点
     * @return xy
     */
    public static float[] calculationEnd(float mAngle, float bevel, PointF startPoint) {
        return calculationStart(mAngle, bevel, startPoint.x, startPoint.y);
    }

    /**
     * 已知 开始点,角度 和斜边边长 ,求结束点
     *
     * @param mAngle     角度
     * @param bevel      边长
     * @param startPoint 开始点
     * @return xy
     */
    public static float[] calculationEnd(float mAngle, float bevel, PointS startPoint) {
        return calculationStart(mAngle, bevel, startPoint.x, startPoint.y);
    }

    /**
     * 已知 开始点,角度 和斜边边长 ,求结束点
     *
     * @param mAngle 角度
     * @param bevel  边长
     * @param startx 开始点x
     * @param starty 开始点y
     * @return xy
     */
    public static float[] calculationEnd(float mAngle, float bevel, float startx, float starty) {
        float[] xy = new float[2];
        xy[0] = (float) (startx + MathUtils.sin(mAngle) * bevel);
        xy[1] = (float) (starty + MathUtils.cos(mAngle) * bevel);
        return xy;
    }


    /**
     * 已知 结束点,角度 和斜边边长 ,求开始点
     *
     * @param mAngle   角度
     * @param bevel    边长
     * @param endPoint 结束点
     * @return xy
     */
    public static float[] calculationStart(float mAngle, float bevel, PointF endPoint) {
        return calculationStart(mAngle, bevel, endPoint.x, endPoint.y);
    }

    /**
     * 已知 结束点,角度 和斜边边长 ,求开始点
     *
     * @param mAngle   角度
     * @param bevel    边长
     * @param endPoint 结束点
     * @return xy
     */
    public static float[] calculationStart(float mAngle, float bevel, PointS endPoint) {
        return calculationStart(mAngle, bevel, endPoint.x, endPoint.y);
    }

    /**
     * 已知 结束点,角度 和斜边边长 ,求开始点
     *
     * @param mAngle 角度
     * @param bevel  边长
     * @param endx   结束点x
     * @param endy   结束点y
     * @return xy
     */
    public static float[] calculationStart(float mAngle, float bevel, float endx, float endy) {
        float[] xy = new float[2];
        xy[0] = (float) (endx - MathUtils.sin(mAngle) * bevel);
        xy[1] = (float) (endy - MathUtils.cos(mAngle) * bevel);
        return xy;
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
        return angle(cen.x, cen.y, first.x, first.y, second.x, second.y);
    }


    /**
     * 已知三点坐标，求 夹角
     *
     * @param cen    夹角点
     * @param first  边角点1
     * @param second 边角点1
     * @return 角度
     */
    public static float angle(PointS cen, PointS first, PointS second) {
        return angle(cen.x, cen.y, first.x, first.y, second.x, second.y);
    }

    public static float angle(float cx, float cy, float fx, float fy, float sx, float sy) {
        float dx1, dx2, dy1, dy2;
        float angle;

        dx1 = fx - cx;
        dy1 = fy - cy;

        dx2 = sx - cx;
        dy2 = sy - cy;

        float c = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        if (c == 0) return -1;

        angle = (float) Math.toDegrees(Math.acos((dx1 * dx2 + dy1 * dy2) / c));

        return angle;
    }

    /**
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          转角度
     * @param n               方向  :: true  逆时针旋转 （从下往上旋转） false 顺时针旋转
     */
    public static void rotate(PointF coordinatePoint,
                              PointF rotatePoint, double degree, boolean n) {
        if (n) nrotate(coordinatePoint, rotatePoint, degree);
        else srotate(coordinatePoint, rotatePoint, degree);
    }

    /**
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          转角度
     * @param n               方向  :: true  逆时针旋转 （从下往上旋转） false 顺时针旋转
     */
    public static void rotate(PointS coordinatePoint,
                              PointS rotatePoint, double degree, boolean n) {
        if (n) nrotate(coordinatePoint, rotatePoint, degree);
        else srotate(coordinatePoint, rotatePoint, degree);
    }

    /**
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param newPoint        旋转后的点
     * @param degree          转角度
     * @param n               方向  :: true  逆时针旋转  false 顺时针旋转
     */
    public static void rotate(PointF coordinatePoint,
                              PointF rotatePoint, PointF newPoint,
                              double degree, boolean n) {
        if (n) nrotate(coordinatePoint, rotatePoint, newPoint, degree);
        else srotate(coordinatePoint, rotatePoint, newPoint, degree);
    }

    /**
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param newPoint        旋转后的点
     * @param degree          转角度
     * @param n               方向  :: true  逆时针旋转  false 顺时针旋转
     */
    public static void rotate(PointS coordinatePoint,
                              PointS rotatePoint, PointS newPoint,
                              double degree, boolean n) {
        if (n) nrotate(coordinatePoint, rotatePoint, newPoint, degree);
        else srotate(coordinatePoint, rotatePoint, newPoint, degree);
    }

    /**
     * rotatePoint 绕 coordinatePoint 旋转 degree角度后 得到新点重新赋值 rotatePoint
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          逆时针旋转角度（从下往上旋转）
     */
    public static void nrotate(PointF coordinatePoint,
                               PointF rotatePoint, double degree) {
        nrotate(coordinatePoint, rotatePoint, rotatePoint, degree);
    }

    /**
     * rotatePoint 绕 coordinatePoint 旋转 degree角度后 得到新点重新赋值 rotatePoint
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          逆时针旋转角度（从下往上旋转）
     */
    public static void nrotate(PointS coordinatePoint,
                               PointS rotatePoint, double degree) {
        nrotate(coordinatePoint, rotatePoint, rotatePoint, degree);
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
     * @param degree          逆时针旋转角度（从下往上旋转）
     */
    public static void nrotate(PointF coordinatePoint,
                               PointF rotatePoint,
                               PointF newPoint,
                               double degree) {
        nrotate(coordinatePoint.x, coordinatePoint.y, rotatePoint.x, rotatePoint.y, newPoint, degree);
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
     * @param degree          逆时针旋转角度（从下往上旋转）
     */
    public static void nrotate(PointS coordinatePoint,
                               PointS rotatePoint,
                               PointS newPoint,
                               double degree) {
        nrotate(coordinatePoint.x, coordinatePoint.y, rotatePoint.x, rotatePoint.y, newPoint, degree);
    }

    public static void nrotate(float coordinatex, float coordinatey,
                               float rotatex, float rotatey,
                               PointF newPoint,
                               double degree) {
        float[] nrotate = nrotate(coordinatex, coordinatey, rotatex, rotatey, degree);
        newPoint.x = nrotate[0];
        newPoint.y = nrotate[1];
    }

    public static void nrotate(float coordinatex, float coordinatey,
                               float rotatex, float rotatey,
                               PointS newPoint,
                               double degree) {
        float[] nrotate = nrotate(coordinatex, coordinatey, rotatex, rotatey, degree);
        newPoint.x = nrotate[0];
        newPoint.y = nrotate[1];
    }


    public static float[] nrotate(float coordinatex, float coordinatey,
                                  float rotatex, float rotatey,
                                  double degree) {
        float[] point = new float[2];
        point[0] = (rotatex - coordinatex) * (float) cos(degree)
                - (rotatey - coordinatey) * (float) sin(degree)
                + coordinatex;
        point[1] = (rotatex - coordinatex) * (float) sin(degree)
                + (rotatey - coordinatey) * (float) cos(degree)
                + coordinatey;
        return point;
    }

    /**
     * rotatePoint 绕 coordinatePoint 旋转 degree角度后 得到新点重新赋值 rotatePoint
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          顺时针旋转角度（从上往下旋转）
     */
    public static void srotate(PointF coordinatePoint,
                               PointF rotatePoint, double degree) {
        srotate(coordinatePoint, rotatePoint, rotatePoint, degree);
    }

    /**
     * rotatePoint 绕 coordinatePoint 旋转 degree角度后 得到新点重新赋值 rotatePoint
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param degree          顺时针旋转角度（从上往下旋转）
     */
    public static void srotate(PointS coordinatePoint,
                               PointS rotatePoint, double degree) {
        srotate(coordinatePoint, rotatePoint, rotatePoint, degree);
    }

    /**
     * 对坐标上任意点(x,y)，
     * 绕一个坐标点(rx0,ry0)
     * 逆时针旋转a角度后的
     * 新的坐标设为(x0, y0)，
     * 有公式：
     * <p>
     * x0= (x - rx0)*cos(a) + (y - ry0)*sin(a) + rx0 ;
     * <p>
     * y0= (x - rx0)*sin(a) - (y - ry0)*cos(a) + ry0 ;
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param newPoint        得到的新点
     * @param degree          顺时针旋转角度（从上往下旋转）
     */
    public static void srotate(PointF coordinatePoint,
                               PointF rotatePoint,
                               PointF newPoint,
                               double degree) {
        srotate(coordinatePoint.x, coordinatePoint.y, rotatePoint.x, rotatePoint.y, newPoint, degree);
    }

    /**
     * 对坐标上任意点(x,y)，
     * 绕一个坐标点(rx0,ry0)
     * 逆时针旋转a角度后的
     * 新的坐标设为(x0, y0)，
     * 有公式：
     * <p>
     * x0= (x - rx0)*cos(a) + (y - ry0)*sin(a) + rx0 ;
     * <p>
     * y0= (x - rx0)*sin(a) - (y - ry0)*cos(a) + ry0 ;
     *
     * @param coordinatePoint 坐标点
     * @param rotatePoint     旋转点
     * @param newPoint        得到的新点
     * @param degree          顺时针旋转角度（从上往下旋转）
     */
    public static void srotate(PointS coordinatePoint,
                               PointS rotatePoint,
                               PointS newPoint,
                               double degree) {
        srotate(coordinatePoint.x, coordinatePoint.y, rotatePoint.x, rotatePoint.y, newPoint, degree);
    }

    public static void srotate(float coordinatex, float coordinatey,
                               float rotatex, float rotatey,
                               PointS newPoint,
                               double degree) {
        float[] srotate = srotate(coordinatex, coordinatey, rotatex, rotatey, degree);
        newPoint.x = srotate[0];
        newPoint.y = srotate[1];
    }


    public static void srotate(float coordinatex, float coordinatey,
                               float rotatex, float rotatey,
                               PointF newPoint,
                               double degree) {
        float[] srotate = srotate(coordinatex, coordinatey, rotatex, rotatey, degree);
        newPoint.x = srotate[0];
        newPoint.y = srotate[1];
    }

    public static float[] srotate(float coordinatex, float coordinatey,
                                  float rotatex, float rotatey,
                                  double degree) {
        float[] point = new float[2];
        point[0] =
                (rotatex - coordinatex) * (float) cos(degree)
                        + (rotatey - coordinatey) * (float) sin(degree)
                        + coordinatex;
        point[1] =
                (rotatey - coordinatey) * (float) cos(degree)
                        - (rotatex - coordinatex) * (float) sin(degree)
                        + coordinatey;
        return point;
    }

    private static final SortRect.RectCompare mPointFRectCompare = new SortRect.RectCompare<PointF>() {
        @Override
        public float getX(PointF mPoint) {
            return mPoint.x;
        }

        @Override
        public float getY(PointF mPoint) {
            return mPoint.y;
        }

        @Override
        public int compare(PointF o1, PointF o2) {
            return Float.compare(o1.x * o1.y, o2.x * o2.y);
        }
    };

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
        SortRect.sortABCD(pointFS, mPointFRectCompare);
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
        SortRect.sortABCD(pointFS, middlePoint, mPointFRectCompare);
    }

    private static final SortRect.RectCompare mPointSRectCompare = new SortRect.RectCompare<PointS>() {
        @Override
        public float getX(PointS mPoint) {
            return mPoint.x;
        }

        @Override
        public float getY(PointS mPoint) {
            return mPoint.y;
        }

        @Override
        public int compare(PointS o1, PointS o2) {
            return Float.compare(o1.x * o1.y, o2.x * o2.y);
        }
    };

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
    public static void sortABCD(PointS[] pointFS) {
        SortRect.sortABCD(pointFS, mPointSRectCompare);
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
    public static void sortABCD(PointS[] pointFS, PointS middlePoint) {
        SortRect.sortABCD(pointFS, middlePoint, mPointSRectCompare);
    }

    /**
     * 四舍五入
     *
     * @param value    原始数据
     * @param accuracy 10的指数
     * @return 四舍五入的数
     */
    public static double round(double value, int accuracy) {
        double pow = Math.pow(10, accuracy);
        return Math.round(value * pow) / pow;
    }

    /**
     * 四舍五入
     *
     * @param value    原始数据
     * @param accuracy 10的指数
     * @return 四舍五入的数
     */
    public static double round2(double value, int accuracy) {
        double pow = Math.pow(10, -accuracy - 1) * 5;
        return round(value + pow, accuracy);
    }

    public static void main(String[] args) {
//        test345rotate();
//        testRound();
        textRandomRotate();
//        testRotateNS();
    }

    private static void testRotateNS() {
//        +x -- >+y  逆时针
//        +x -- >-y 顺时针

        float[] nrotate = MathUtils.nrotate(2, 2, 2, 7, 1);
        System.out.println(" nrotate 1 == x:" + nrotate[0] + " y:" + nrotate[1]);
        float[] srotate = MathUtils.srotate(2, -2, 2, -7, 1);
        System.out.println(" srotate 1 == x:" + srotate[0] + " y:" + srotate[1]);
        float[] nnrotate = MathUtils.nrotate(2, -2, 2, -7, 1);
        System.out.println(" srotate 1 == x:" + nnrotate[0] + " y:" + nnrotate[1]);
//        nrotate 1 == x:1.912738 y:6.9992385
//        srotate 1 == x:1.912738 y:-6.9992385

        for (int i = 0; i < 180; i++) {
            nrotate = MathUtils.YNrotate(i, 2, 7, 2, 2);
            System.out.println(" YNrotate " + i + " == x:" + nrotate[0] + " y:" + nrotate[1]);
//            srotate = MathUtils.YSrotate(i, 2, 7, 2, 2);
//            System.out.println(" YSrotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);

//            nrotate = MathUtils.YNrotate(i, 7, 2, 2, 2);
//            System.out.println(" YNrotate " + i + " == x:" + nrotate[0] + " y:" + nrotate[1]);
//            srotate = MathUtils.YSrotate(i, 7, 2, 2, 2);
//            System.out.println(" SNrotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);
        }
    }


    //# 绕pointx,pointy逆时针旋转
    private static float[] YNrotate(float angle, float valuex, float valuey, float pointx, float pointy) {
        float[] xy = new float[2];
        xy[0] = (float) ((valuex - pointx) * cos(angle) + (valuey - pointy) * sin(angle) + pointx);
        xy[1] = (float) ((valuex - pointx) * sin(angle) + (valuey - pointy) * cos(angle) + pointy);

//        xy[0] = (float) ((valuex - pointx) * cos(angle) - (valuey - pointy) * sin(angle) + pointx);
//        xy[1] = (float) ((valuex - pointx) * sin(angle) + (valuey - pointy) * cos(angle) + pointy);
        return xy;

    }

    //#绕pointx,pointy顺时针旋转
    private static float[] YSrotate(float angle, float valuex, float valuey, float pointx, float pointy) {
        float[] xy = new float[2];
        xy[0] = (float) ((valuex - pointx) * cos(angle) - (valuey - pointy) * sin(angle) + pointx);
        xy[1] = (float) ((valuey - pointy) * cos(angle) + (valuex - pointx) * sin(angle) + pointy);
//        xy[0] = (float) ((valuex - pointx) * cos(angle) + (valuey - pointy) * sin(angle) + pointx);
//        xy[1] = (float) ((valuey - pointy) * cos(angle) - (valuex - pointx) * sin(angle) + pointy);
        return xy;
    }


    private static void textRandomRotate() {
//        36.869896
        float[] srotate;
        srotate = MathUtils.srotate(2, 2, 2, -8, 1);
        System.out.println(" srotate 1 == x:" + srotate[0] + " y:" + srotate[1]);
        srotate = MathUtils.srotate(2, 2, 2, -8, 36.869896);
        System.out.println(" srotate 36.869896 == x:" + srotate[0] + " y:" + srotate[1]);

        for (int i = 0; i < 180; i++) {
            srotate = MathUtils.srotate(2, 2, 2, -8, i);
            System.out.println(" srotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);
//            srotate = Srotate(i, 2, -8, 2, 2);
//            System.out.println(" Srotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);

//            srotate = MathUtils.nrotate(2, 2, 2, -8, i);
//            System.out.println(" nrotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);
//            srotate = Nrotate(i, 2, -8, 2, 2);
//            System.out.println(" Nrotate " + i + " == x:" + srotate[0] + " y:" + srotate[1]);
        }

    }


    //# 绕pointx,pointy逆时针旋转
    private static float[] Nrotate(float angle, float valuex, float valuey, float pointx, float pointy) {
        float[] xy = new float[2];
        xy[0] = (float) ((valuex - pointx) * cos(angle) - (valuey - pointy) * sin(angle) + pointx);
        xy[1] = (float) ((valuex - pointx) * sin(angle) + (valuey - pointy) * cos(angle) + pointy);
        return xy;

    }

    //#绕pointx,pointy顺时针旋转
    private static float[] Srotate(float angle, float valuex, float valuey, float pointx, float pointy) {
        float[] xy = new float[2];
        xy[0] = (float) ((valuex - pointx) * cos(angle) + (valuey - pointy) * sin(angle) + pointx);
        xy[1] = (float) ((valuey - pointy) * cos(angle) - (valuex - pointx) * sin(angle) + pointy);
        return xy;
    }


    private static void test345rotate() {

        float Ax = 0;
        float Ay = 0;

        float Bx = 4;
        float By = 0;

        float Cx = 4;
        float Cy = 3;

        float Dx = 0;
        float Dy = 3;

        float angle = MathUtils.angle(Ax, Ay, Bx, By, Cx, Cy);
        System.out.println(" angle : " + angle);
        double bevel = MathUtils.calculationBevel(Ax, Ay, Cx, Cy);
        System.out.println(" bevel : " + bevel);


        float[] rotate;

        rotate = MathUtils.srotate(Ax, Ay, 5, 0, angle);
        System.out.println(" srotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.srotate(Ax, Ay, 0, 5, angle);
        System.out.println(" srotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.srotate(Ax, Ay, -5, 0, angle);
        System.out.println(" srotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.srotate(Ax, Ay, 0, -5, angle);
        System.out.println(" srotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.srotate(0, 1, 0, -4, angle);
        System.out.println(" srotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.nrotate(Ax, Ay, 5, 0, angle);
        System.out.println(" nrotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.nrotate(Ax, Ay, 0, 5, angle);
        System.out.println(" nrotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.nrotate(Ax, Ay, -5, 0, angle);
        System.out.println(" nrotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.nrotate(Ax, Ay, 0, -5, angle);
        System.out.println(" nrotate : x:" + rotate[0] + " y:" + rotate[1]);

        rotate = MathUtils.nrotate(0, 1, 0, -4, angle);
        System.out.println(" nrotate : x:" + rotate[0] + " y:" + rotate[1]);


    }

    private static void testRound() {
        double round = MathUtils.round(10.0125456, 3);
        System.out.println(" round : " + round);
        double round2 = MathUtils.round2(10.0125456, 3);
        System.out.println(" round2 : " + round2);
    }

}
