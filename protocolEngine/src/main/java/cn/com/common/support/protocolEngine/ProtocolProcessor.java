package cn.com.common.support.protocolEngine;

import android.os.Looper;

import cn.com.common.support.protocolEngine.DataInspector.DataInspectorPool;
import cn.com.common.support.protocolEngine.DataInspector.DataResolveInspector;
import cn.com.common.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.common.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.common.support.protocolEngine.pack.ReceivesData;
import cn.com.common.support.protocolEngine.resolve.DataResolveQueue;
import cn.com.common.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ProtocolProcessor {

    public static final String TAG = "ProtocolProcessor";
    private DataResolveQueue mDataResolveQueue;

    public ProtocolProcessor(Looper protocolLooper, IProtocolAnalysisResult mProtocolCallBack, int protocolVersion) {
        this(protocolLooper, mProtocolCallBack, new SocketDataQueueProducer(protocolVersion), 1);
    }

    public ProtocolProcessor(Looper protocolLooper, IProtocolAnalysisResult mProtocolCallBack, int protocolVersion, int poolSize) {
        this(protocolLooper, mProtocolCallBack, new SocketDataQueueProducer(protocolVersion), poolSize);
    }

    public ProtocolProcessor(Looper protocolLooper, IProtocolAnalysisResult mProtocolCallBack,
                             ISocketDataProducer mSocketDataProducer, int poolSize) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolProcessor> protocolLooper==null . ");
        }

        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <ProtocolProcessor> ISocketDataProducer==null . ");
        }

        this.mDataResolveQueue = new DataResolveQueue(protocolLooper,
                new DataInspectorPool(new DataResolveInspector(mProtocolCallBack), poolSize),
                mSocketDataProducer);
    }

    public void release() {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.release();
            mDataResolveQueue = null;
        }
    }

    public void onInReceiveData(ReceivesData mData) {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.postReceiveDataToQueue(mData);
        }
    }

}
