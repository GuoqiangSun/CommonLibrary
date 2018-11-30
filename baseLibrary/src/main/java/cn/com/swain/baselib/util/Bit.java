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

    /**
     * 将 bitPoint位 置为 1
     */
    public final Bit add(int bitPoint) {
        checkPoint(bitPoint);

        final int tmp = ONE << bitPoint;

        addDevice(tmp);

        return this;
    }

    /**
     * 将 tmpDevice 添加进来
     */
    public final void addDevice(int tmpDevice) {

        this.device |= tmpDevice;

    }

    /**
     * 将 bitPoint位 置为 0
     */
    public final Bit remove(int bitPoint) {
        checkPoint(bitPoint);

        final int tmp = ONE << bitPoint;

        removeDevice(tmp);

        return this;
    }

    /**
     * 将 tmpDevice 移除出去
     */
    public final void removeDevice(int tmpDevice) {

        this.device = this.device & ~tmpDevice;

    }

    /**
     * flag true {@link #add(int)} or {@link #remove(int)}
     */
    public final Bit reserve(int bitPoint, boolean flag) {
        if (flag) {
            add(bitPoint);
        } else {
            remove(bitPoint);
        }

        return this;
    }

    /**
     * flag true {@link #addDevice(int)} or {@link #removeDevice(int)}
     */
    public final void reserveDevice(int tmpDevice, boolean flag) {
        if (flag) {
            addDevice(tmpDevice);
        } else {
            removeDevice(tmpDevice);
        }
    }

    /**
     * 置为 0x00
     */
    public final Bit fillEmpty() {
        this.device = EMPTY;
        return this;
    }

    @Override
    public String toString() {
        return " Bit=" + Integer.toBinaryString(device);
    }

    /**
     * 判断 num数中 point位 是否 是1
     */
    public static boolean isOne(int num, int point) {
        checkPoint(point);
        return ((num >> point) & 1) == 0x01;
    }

    /**
     * 判断 num数中 point位 是否 是0
     */
    public static boolean isZero(int num, int point) {
        checkPoint(point);
        return ((num >> point) & 1) == 0x00;
    }

    public static void main(String[] args) {
        int i = 0x25630;

        System.out.println("mun binary:" + Integer.toBinaryString(i));

        boolean one = isOne(i, 0);
        boolean zero = isZero(i, 0);

        System.out.println("mun" + i + " bit[" + 0 + "]" + " is one?" + one + "; is zero?" + zero);

        one = isOne(i, 4);
        zero = isZero(i, 4);
        System.out.println("mun" + i + " bit[" + 1 + "]" + " is one?" + one + "; is zero?" + zero);

        Bit mBit = new Bit(i);
        int tmpDevice  = i-1;
        System.out.println("tmpDevice binary:" + Integer.toBinaryString(tmpDevice));

        mBit.removeDevice(tmpDevice);
        System.out.println("mun " + i + " removeDevice:" + tmpDevice + " " + mBit.toString());

        mBit.add(1);
        System.out.println(" add(1) " + mBit.toString());

        mBit.add(3);
        System.out.println(" add(3) " + mBit.toString());

        mBit.remove(1);
        System.out.println(" remove(1) " + mBit.toString());

    }

}
