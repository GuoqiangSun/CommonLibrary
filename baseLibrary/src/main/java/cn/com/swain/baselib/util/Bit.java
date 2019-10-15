package cn.com.swain.baselib.util;

import java.util.Arrays;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/11 0011
 * desc :
 */
public class Bit {

    private static final int[] BIT_NUM = new int[]{
            0b1,
            0b10,
            0b100,
            0b1000,
            0b10000,
            0b100000,
            0b1000000,
            0b10000000,
            0b100000000,
            0b1000000000,
            0b10000000000,
            0b100000000000,
            0b1000000000000,
            0b10000000000000,
            0b100000000000000,
            0b1000000000000000,
            0b10000000000000000,
            0b100000000000000000,
            0b1000000000000000000,
            0b10000000000000000000,
            0b100000000000000000000,
            0b1000000000000000000000,
            0b10000000000000000000000,
            0b100000000000000000000000,
            0b1000000000000000000000000,
            0b10000000000000000000000000,
            0b100000000000000000000000000,
            0b1000000000000000000000000000,
            0b10000000000000000000000000000,
            0b100000000000000000000000000000,
            0b1000000000000000000000000000000,
            0b10000000000000000000000000000000,
    };

    public static int[] getBitNumArray() {
        return Arrays.copyOf(BIT_NUM, BIT_NUM.length);
    }

    public static int getBitNum(int point) {
        checkPoint(point);
        return BIT_NUM[point];
    }

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
    public final Bit addDevice(int tmpDevice) {

        this.device |= tmpDevice;

        return this;
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
    public final Bit removeDevice(int tmpDevice) {

        this.device = this.device & ~tmpDevice;

        return this;
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
    public final Bit reserveDevice(int tmpDevice, boolean flag) {
        if (flag) {
            return addDevice(tmpDevice);
        } else {
            return removeDevice(tmpDevice);
        }
    }

    /**
     * 判断bitPoint位是否为 1
     */
    public final boolean isOne(int bitPoint) {
        checkPoint(bitPoint);
        return ((this.device >> bitPoint) & 0x01) == 1;
    }

    /**
     * 判断bitPoint位是否为 1
     */
    public final boolean isOneBit(int bitPoint) {
        checkPoint(bitPoint);
        return (this.device & BIT_NUM[bitPoint]) > 0;
    }

    /**
     * 判断bitPoint位是否为 0
     */
    public final boolean isZero(int bitPoint) {
        checkPoint(bitPoint);
        return ((this.device >> bitPoint) & 0x01) == 0;
    }

    /**
     * 判断 num数中 point位 是否 是0
     */
    public final boolean isZeroBit(int bitPoint) {
        checkPoint(bitPoint);
        return (this.device & BIT_NUM[bitPoint]) == 0;
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


    public static Bit getBit() {
        return new Bit();
    }

    public static Bit getBit(int device) {
        return new Bit(device);
    }

    /**
     * 判断 num数中 point位 是否 是1
     */
    public static boolean isOne(int num, int point) {
        checkPoint(point);
        return ((num >> point) & 0x01) == 0x01;
    }


    /**
     * 判断 num数中 point位 是否 是1
     */
    public static boolean isOneBit(int num, int point) {
        checkPoint(point);
        return (num & BIT_NUM[point]) > 0;
    }

    /**
     * 判断 num数中 point位 是否 是0
     */
    public static boolean isZero(int num, int point) {
        checkPoint(point);
        return ((num >> point) & 0x01) == 0x00;
    }

    /**
     * 判断 num数中 point位 是否 是0
     */
    public static boolean isZeroBit(int num, int point) {
        checkPoint(point);
        return (num & BIT_NUM[point]) == 0x00;
    }

    public static void main(String[] args) {
        int i = 0x25630;

        System.out.println("mun binary:" + Integer.toBinaryString(i));

        boolean one = isOne(i, 0);
        boolean zero = isZero(i, 0);

        System.out.println("mun" + i + " bit[" + 0 + "]" + " is one?" + one + "; is zero?" + zero);

        boolean onebit = isOneBit(i, 0);
        boolean zerobit = isZeroBit(i, 0);

        System.out.println("mun" + i + " bit[" + 0 + "]" + " is one bit?" + onebit + "; is zero bit?" + zerobit);

        one = isOne(i, 4);
        zero = isZero(i, 4);
        System.out.println("mun" + i + " bit[" + 1 + "]" + " is one?" + one + "; is zero?" + zero);

        onebit = isOneBit(i, 4);
        zerobit = isZeroBit(i, 4);
        System.out.println("mun" + i + " bit[" + 1 + "]" + " is one bit?" + onebit + "; is zero bit?" + zerobit);

        Bit mBit = new Bit(i);
        int tmpDevice = i - 1;
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
