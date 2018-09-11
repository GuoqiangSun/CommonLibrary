package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import java.util.ArrayList;

import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class SocketDataArrayProducer implements ISocketDataProducer {

    private String TAG = ProtocolProcessor.TAG;

    private final int version;

    public SocketDataArrayProducer(int version) {
        this.version = version;
    }

    public void clear() {
        mCache.clear();
    }

    @Override
    public SocketDataArray produceSocketDataArray() {

        return produceSocketDataArrayFromLst();
    }


    /*****************/


    private final ArrayList<SocketDataArray> mCache = new ArrayList<>();

    private SocketDataArray produceSocketDataArrayFromLst() {
        SocketDataArray mProduceSocketDataArray = null;

        boolean has = false;
        for (SocketDataArray mSocketDataArray : mCache) {
            if (!mSocketDataArray.isUsed()) {
                mProduceSocketDataArray = mSocketDataArray;
                has = true;
            }
        }

        if (!has) {
            mProduceSocketDataArray = new SocketDataArray(version);
            mCache.add(mProduceSocketDataArray);
        }
        Tlog.v(TAG, "ISocketDataProducer cacheSize : " + mCache.size() + " mSocketDataArray hash:" + mProduceSocketDataArray.hashCode());
        return mProduceSocketDataArray;
    }
}
