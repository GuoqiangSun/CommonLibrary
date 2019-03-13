package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import java.util.Arrays;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/12 0012
 * desc :
 */

public class SocketDataQueueProducer implements ISocketDataProducer {

    private String TAG = AbsProtocolProcessor.TAG;
    private static final int EXTEND_SIZE = 10;

    private final int MAX_SIZE;

    private final String name;

    private final int version;

    private final int normalSize;

    public SocketDataQueueProducer(int version) {
        this(version, 1);
    }

    /**
     * @param version    协议版本号
     * @param normalSize 一组数据默认大小
     */
    public SocketDataQueueProducer(int version, int normalSize) {
        this.version = version;
        this.name = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        if (normalSize > Byte.MAX_VALUE) {
            normalSize = Byte.MAX_VALUE;
        } else if (normalSize <= 0) {
            normalSize = 1;
        }
        this.normalSize = normalSize;
        this.MAX_SIZE = this.normalSize * EXTEND_SIZE * 12 + 2;
        create();
    }

    @Override
    public void create() {

        for (int i = 0; i < normalSize; i++) {
            extend();
        }
    }

    @Override
    public void clear() {
        bufClear();
    }

    @Override
    public SocketDataArray produceSocketDataArray() {

        return produceSocketDataArrayFromBuf();
    }


    /*****************/

    private void bufClear() {
        Tlog.e(TAG, name + " bufClear ");
        this.size = 0;
        this.point = 0;
        if (mSocketDataArrays != null) {
            Arrays.fill(mSocketDataArrays, null);
        }
    }

    private void gc() {
        if (size >= MAX_SIZE) {
            Tlog.e(TAG, name + " gc() clear buff  point:" + point + " size:" + size + " maxSize:" + MAX_SIZE);
            bufClear();
        }
    }

    private SocketDataArray[] mSocketDataArrays;
    private int size = 0;
    private int[] points;
    private int point = 0;

    private SocketDataArray getSocketDataArray() {


        SocketDataArray mProduceSocketDataArray = null;

        if (point >= size) {
            if (Tlog.isDebug()) {
                Tlog.e(TAG, name + " point arrive final , point: " + point + ",size:" + size + " setPoint=0");
            }
            point = 0;
        }

        for (int i = point; i < size; i++) {
            SocketDataArray mSocketDataArray = mSocketDataArrays[points[i]];
            if (mSocketDataArray != null && !mSocketDataArray.isUsed()) {
                if (Tlog.isDebug()) {
                    Tlog.v(TAG, name + " getSocketDataArray: point " + point + " size:" + size);
                }
                mProduceSocketDataArray = mSocketDataArray;
                point = i + 1;
                break;
            }
        }
        return mProduceSocketDataArray;
    }

    private SocketDataArray produceSocketDataArrayFromBuf() {
        SocketDataArray mProduceSocketDataArray;

        if (size <= 0 || mSocketDataArrays == null) {
            mProduceSocketDataArray = new SocketDataArray(version);
        } else {

            mProduceSocketDataArray = getSocketDataArray();
            if (mProduceSocketDataArray == null) {

                Tlog.e(TAG, name + " extend() CacheSocketData");

                gc();
                extend();
                mProduceSocketDataArray = getSocketDataArray();

                if (mProduceSocketDataArray == null) {
                    Tlog.e(TAG, name + "produceSocketDataArrayFromBuf mProduceSocketDataArray == null ; new SocketDataArray() ");
                    mProduceSocketDataArray = new SocketDataArray(version);
                }

            }

        }

        mProduceSocketDataArray.setISUsed();
        return mProduceSocketDataArray;
    }

    private void extend() {
        Tlog.v(TAG, name + " before extend size: " + size + " point:" + point);
        final int mGeneralSize = EXTEND_SIZE;
        final int oldSize = size;

        final SocketDataArray[] mGeneralSocketDataArrays = generalSocketDataArrays(mGeneralSize);
        if (size <= 0) {
            mSocketDataArrays = mGeneralSocketDataArrays;
            size = mGeneralSize;
        } else {

            final SocketDataArray[] mTmpSocketDataArrays = mSocketDataArrays;

            size += mGeneralSize;
            mSocketDataArrays = new SocketDataArray[size];

            System.arraycopy(mTmpSocketDataArrays, 0, mSocketDataArrays, 0, oldSize);
            System.arraycopy(mGeneralSocketDataArrays, 0, mSocketDataArrays, oldSize, mGeneralSize);

        }

        points = new int[size];
        for (int i = 0; i < size; i++) {
            points[i] = i;
        }
        point = oldSize;
        if (point < 0) {
            point = 0;
        }

        Tlog.v(TAG, name + " after extend, size: " + size + " point:" + point);
    }


    private SocketDataArray[] generalSocketDataArrays(int size) {
        SocketDataArray[] mSocketDataArrays = new SocketDataArray[size];
        for (int i = 0; i < size; i++) {
            mSocketDataArrays[i] = new SocketDataArray(version);
        }
        return mSocketDataArrays;
    }


}
