package cn.com.common.support.protocolEngine.datagram.escape;

import cn.com.common.baselib.app.Tlog;
import cn.com.common.baselib.util.StrUtil;
import cn.com.common.support.protocolEngine.ProtocolProcessor;
import cn.com.common.support.protocolEngine.utils.ProtocolCode;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 * <p>
 * * 转义前	           转义后
 * STX （帧头）	    STX 转成 ESC 和 STX_ESC
 * ETX （帧尾）	    ETX 转成 ESC 和 ETX_ESC
 * ESC （转义符）	ESC 转成 ESC 和 ESC_ESC
 */

public class EscapeDataArray implements IEscapeDataArray {

    private String TAG = ProtocolProcessor.TAG;

    private static final byte STX = ProtocolCode.STX;
    private static final byte ETX = ProtocolCode.ETX;
    private static final byte ESC = ProtocolCode.ESC;

    private static final byte STX_ESC = ProtocolCode.STX_ESC;
    private static final byte ETX_ESC = ProtocolCode.ETX_ESC;
    private static final byte ESC_ESC = ProtocolCode.ESC_ESC;

    /******************************/


    public static final int STATE_UNKNOWN = 0x00;

    public static final int STATE_ESCAPE = 0x01;

    public static final int STATE_REVERSE = 0x02;

    /**
     * 状态 ，转义，反转义
     */
    private int state = STATE_UNKNOWN;

    /**
     * empty byte
     */
    public static final byte EMPTY = 0x00;

    /**
     * 数据
     */
    private byte[] DATA;

    /**
     * 数据下标
     */
    private int point;
    /**
     * 容量
     */
    private int capacity;

    private final int originCapacity;


    public EscapeDataArray(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(" =EscapeDataArray= ;  size <= 0 ");
        }
        this.DATA = new byte[size];
        this.point = 0;
        this.originCapacity = this.capacity = size;
    }

    @Override
    public final int getPoint() {
        return point;
    }

    @Override
    public final int getCapacity() {
        return capacity;
    }

    @Override
    public boolean isEscapeState() {
        return (state == STATE_ESCAPE);
    }

    @Override
    public boolean isReverseState() {
        return (state == STATE_REVERSE);
    }

    @Override
    public String getStateStr() {

        return isEscapeState() ? "ESCAPE" : isReverseState() ? "REVERSE" : "UNKNOWN";

    }


    @Override
    public void changeStateToEscape() {
        state = STATE_ESCAPE;
    }

    @Override
    public void changeStateToReverse() {
        state = STATE_REVERSE;
    }

    @Override
    public final void reset() {
        this.point = 0;
        this.state = STATE_UNKNOWN;
    }

    @Override
    public void fillEmpty() {
        if (DATA != null && point > 0) {
            java.util.Arrays.fill(DATA, 0, point, EMPTY);
        }
    }

    @Override
    public void release() {
        fillEmpty();
        reset();
        DATA = null;
        capacity = -1;
    }

    /************************************************/

    @Override
    public void onAddHead(byte b) {
        onDataAddNoER(b);
    }

    @Override
    public void onAddTail(byte b) {
        onDataAddNoER(b);
    }


    private byte lastByte = EMPTY; // 记录上一个字节

    @Override
    public void onAddDataReverse(byte b) {

        if (b != ESC) {

            // 当前不是ESC
            // 看上一个字节是否是 ESC

            if (lastByte == ESC) {

                switch (b) {
                    case STX_ESC:

                        // Tlog.e(TAG, "reverse " + STX_ESC);
                        onDataAddNoER(STX);

                        break;
                    case ETX_ESC:

                        // Tlog.e(TAG, "reverse " + ETX_ESC);
                        onDataAddNoER(ETX);

                        break;
                    case ESC_ESC:

                        // Tlog.e(TAG, "reverse " + ESC_ESC);
                        onDataAddNoER(ESC);

                        break;
                    default:
                        // 当前字节不是STX_ESC ETX_ESC ESC_ESC 记得把lastByte也加进来
                        onDataAddNoER(lastByte);
                        onDataAddNoER(b);
                        break;
                }
            } else {
                onDataAddNoER(b);
            }

        } else {

            // 当前是ESC

            // 看下一个字节

            // 这里不加到集合里面去，等下一次判断再加

        }

        lastByte = b;

    }

    @Override
    public void onAddDataEscape(byte b) {
        // Tlog.v(TAG, "--- > escape b : " + b);

        switch (b) {
            case STX:

                // Tlog.e(TAG, "escape STX ");

                onDataAddNoER(ESC);
                onDataAddNoER(STX_ESC);

                break;

            case ETX:

                // Tlog.e(TAG, "escape ETX ");

                onDataAddNoER(ESC);
                onDataAddNoER(ETX_ESC);

                break;
            case ESC:

                // Tlog.e(TAG, "escape ESC ");

                onDataAddNoER(ESC);
                onDataAddNoER(ESC_ESC);

                break;

            default:

                onDataAddNoER(b);

                break;
        }

    }

    @Override
    public void onAddPackageEscape(byte[] data) {

        if (data == null) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " NullPointerException onAddPackageEscape() " + StrUtil.toString(DATA));
            }
            throw new NullPointerException("  onAddPackageEscape() data == null  ");
        }

        int length = data.length;

//        Tlog.v(TAG, " addAllEscape ----> length : " + length);

        int end = length - 1;

        for (int i = 0; i < length; i++) {
            if (i == 0 || i == end) {

                onDataAddNoER(data[i]);

            } else {

                onAddDataEscape(data[i]);

            }
        }

    }

    @Override
    public void onAddPackageReverse(byte[] data) {

        if (data == null) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " NullPointerException onAddPackageReverse() " + StrUtil.toString(DATA));
            }
            throw new NullPointerException(" onAddPackageReverse() data == null  ");
        }

        int length = data.length;

        int end = length - 1;

        for (int i = 0; i < length; i++) {
            if (i == 0 || i == end) {

                onDataAddNoER(data[i]);

            } else {

                onAddDataReverse(data[i]);

            }
        }
    }


    /**
     * 添加进来，不考虑转义问题，自动扩容
     *
     * @param b
     */
    private void onDataAddNoER(byte b) {

        // Tlog.v(TAG, " add b : " + b + " === point : " + point);

        if (point == capacity) {

//            Tlog.d(TAG, " point == capacity == " + point + " System.arrayCopy ");

            int length = capacity + originCapacity;
            byte[] temData = new byte[length];
            System.arraycopy(DATA, 0, temData, 0, capacity);
            DATA = temData;
            capacity = length;
        }

        DATA[point] = b;
        point++;

    }

    /************************************************/


    @Override
    public byte getByte(int index) {
/*

        if (index >= point) {
            Tlog.e(TAG, " getByte(int)  index: " + index + " point: " + point);
            return EMPTY;
        }

        if (DATA == null) {
            Tlog.e(TAG, " getByte(int)  my DATA is null");
            return EMPTY;
        }
*/

        return (DATA == null) ? EMPTY : (index >= point) ? EMPTY : DATA[index];

    }

    @Override
    public byte[] toArray() {
        if (point <= 0 || DATA == null) {
            return null;
        }
        final byte[] signalData = new byte[point];
        System.arraycopy(DATA, 0, signalData, 0, point);
        return signalData;
    }


    @Override
    public byte[] toArray(int srcPoint, int length) {

        if (point < (srcPoint + length)) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " IndexOutOfBoundsException toArray() " + StrUtil.toString(DATA));
            }
            throw new IndexOutOfBoundsException(" toArray(int int) srcPoint: " + srcPoint + " length: " + length + " mDataPoint: " + point);
        }

        final byte[] mData = new byte[length];

        System.arraycopy(DATA, srcPoint, mData, 0, length);

        return mData;
    }

    @Override
    public boolean copyArray(byte[] data) {

        final byte[] mData = data;

        if (mData == null) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " NullPointerException copyArray()" + StrUtil.toString(DATA));
            }
            throw new NullPointerException(" copyArray(byte[]) data == null ");
        }

        if (mData.length < point) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " IndexOutOfBoundsException copyArray() " + StrUtil.toString(DATA));
            }
            throw new IndexOutOfBoundsException(" copyArray(byte[]);data.length must more than point; data.length(" + mData.length + ")<point(" + point + ")");
        }

        System.arraycopy(DATA, 0, mData, 0, point);
        return true;
    }


    @Override
    public boolean copyArray(byte[] data, int srcPoint, int length) {

        if (data == null) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " NullPointerException copyArray() " + StrUtil.toString(DATA));
            }
            throw new NullPointerException(" copyArray(byte[] int int) data is null ");
        }

        if (point < (srcPoint + length)) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " IndexOutOfBoundsException copyArray() " + StrUtil.toString(DATA));
            }
            throw new IndexOutOfBoundsException("copyArray(byte[] int int) srcPoint: " + srcPoint + " length: " + length + " point: " + point);
        }

        final byte[] mData = data;
        System.arraycopy(DATA, srcPoint, mData, 0, length);
        return true;
    }

    /************************************************/
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" EscapeDataArray:");
        sb.append(" curState : " + getStateStr() + ", point : " + point + ",");
        sb.append(" capacity : " + capacity + " == data.length : " + DATA.length + ",");
        sb.append(" content : ");
        for (byte b : DATA) {
            sb.append(Integer.toHexString(b & 0xFF) + ",");
        }
        sb.append(" : END. ");
        return sb.toString();
    }

}
