package cn.com.swain.baselib.util;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/11 0011
 * desc :
 */
public class Bit {

    private static final int EMPTY = 0x00000000;
    private static final int ONE = 0x00000001;

    public Bit() {
        this(EMPTY);
    }

    public Bit(int device) {
        this.device = device;
    }

    private int device;

    public int getDevice() {
        return device;
    }

    private static void checkPoint(int bitPoint) {
        if (bitPoint > 8 * 4 - 1 || bitPoint < 0) {
            throw new ArrayIndexOutOfBoundsException(" bitPoint must be less than 8*4-1 and more than 0 ; cur point:" + bitPoint);
        }
    }



    public final Bit add(int bitPoint) {
        checkPoint(bitPoint);

        final int tmp = ONE << bitPoint;

//        device = device & ~tmp;

        device = device | tmp;

        return this;
    }

    public final void addDevice(int tmpDevice) {

        this.device |= tmpDevice;

    }

    public final Bit remove(int bitPoint) {
        checkPoint(bitPoint);

        final int tmp = ONE << bitPoint;

        device = device & ~tmp;

        return this;
    }

    public final void removeDevice(int tmpDevice) {

        this.device = this.device & ~tmpDevice;

    }

    public final Bit reserve(int bitPoint, boolean flag) {

        checkPoint(bitPoint);

        final int tmp = ONE << bitPoint;

        device = device & ~tmp;

        if (flag) {
            device = device | tmp;
        }
        return this;
    }

    public final Bit fillEmpty() {
        this.device = EMPTY;
        return this;
    }

    public static boolean isOne(int num, int point) {
        checkPoint(point);
        return ((num >> point) & 1) == 0x01;
    }

}
