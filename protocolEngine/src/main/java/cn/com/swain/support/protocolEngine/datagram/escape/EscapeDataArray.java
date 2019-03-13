package cn.com.swain.support.protocolEngine.datagram.escape;

import java.io.Serializable;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public abstract class EscapeDataArray implements IEscapeDataArray, Serializable {

    private String TAG = AbsProtocolProcessor.TAG;

    /******************************/


    public static final int STATE_UNKNOWN = 0x00;

    public static final int STATE_ESCAPE = 0x01;

    public static final int STATE_REVERSE = 0x02;

    /**
     * 状态 ，转义，反转义
     */
    private volatile int state = STATE_UNKNOWN;


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

    protected EscapeDataArray(int point, byte[] data, int originCapacity, int capacity, int state) {
        this.DATA = data;
        this.point = point;
        this.originCapacity = originCapacity;
        this.capacity = capacity;
        this.state = state;
    }

    protected int getOriginCapacity() {
        return originCapacity;
    }

    protected int getState() {
        return state;
    }

    protected byte[] getDATA() {
        return DATA;
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
            if ((i == 0 || i == end) && (isHeadByte(data[i]) || isTailByte(data[i]))) {

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
            if ((i == 0 || i == end) && (isHeadByte(data[i]) || isTailByte(data[i]))) {

                onDataAddNoER(data[i]);

            } else {

                onAddDataReverse(data[i]);

            }
        }
    }

    /**
     * 添加进来，不考虑转义问题，自动扩容
     *
     * @param b data
     */
    protected void onDataAddNoER(byte b) {

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

        if (data == null) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " NullPointerException copyArray()" + StrUtil.toString(DATA));
            }
            throw new NullPointerException(" copyArray(byte[]) data == null ");
        }

        if (data.length < point) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, " IndexOutOfBoundsException copyArray() " + StrUtil.toString(DATA));
            }
            throw new IndexOutOfBoundsException(" copyArray(byte[]);data.length must more than point; data.length(" + data.length + ")<point(" + point + ")");
        }

        System.arraycopy(DATA, 0, data, 0, point);
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

        System.arraycopy(DATA, srcPoint, data, 0, length);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" EscapeDataArray:");
        sb.append(" curState:").append(getStateStr())
                .append(", point:").append(point).append(",");
        sb.append(" capacity:").append(capacity).append(" == data.length:")
                .append(DATA != null ? DATA.length : 0).append(",");
        sb.append(" content : ");
        if (DATA != null) {
            for (byte b : DATA) {
                sb.append(Integer.toHexString(b & 0xFF)).append(",");
            }
        } else {
            sb.append(" null,");
        }
        sb.append(" : END. ");
        return sb.toString();
    }

}
