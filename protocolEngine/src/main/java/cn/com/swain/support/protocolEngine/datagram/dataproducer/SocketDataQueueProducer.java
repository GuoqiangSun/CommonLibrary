package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import java.util.Arrays;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/12 0012
 * desc :
 */

public class SocketDataQueueProducer implements ISocketDataProducer {

    private String TAG = ProtocolProcessor.TAG;
    private static final int EXTEND_SIZE = 10;

    private final String name;

    private final int version;

    public SocketDataQueueProducer(int version) {
        this(version, 1);
    }

    public SocketDataQueueProducer(int version, int normalSize) {
        this.version = version;
        this.name = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        if (normalSize > 5) {
            normalSize = 5;
        } else if (normalSize <= 0) {
            normalSize = 1;
        }
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
        this.size = 0;
        this.point = 0;
        Arrays.fill(mSocketDataArrays, null);
        this.mSocketDataArrays = null;
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
                extend();
                mProduceSocketDataArray = getSocketDataArray();
            }

        }

        if (mProduceSocketDataArray == null) {
            Tlog.e(TAG, name + "produceSocketDataArrayFromBuf mProduceSocketDataArray == null ; new SocketDataArray() ");
            mProduceSocketDataArray = new SocketDataArray(version);
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
