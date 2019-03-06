package cn.com.swain.support.protocolEngine;

import android.os.Looper;

import cn.com.swain.support.protocolEngine.DataInspector.DataInspectorPool;
import cn.com.swain.support.protocolEngine.DataInspector.DataInspector;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.resolve.DataResolveQueue;
import cn.com.swain.support.protocolEngine.resolve.XX.XXDataResolveQueue;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */
@Deprecated
public class ProtocolProcessor extends AbsProtocolProcessor {


    private DataResolveQueue mDataResolveQueue;

    public ProtocolProcessor(Looper protocolLooper,
                             IProtocolAnalysisResult mProtocolCallBack,
                             int protocolVersion) {
        this(protocolLooper, mProtocolCallBack, protocolVersion, 1);
    }

    public ProtocolProcessor(Looper protocolLooper,
                             IProtocolAnalysisResult mProtocolCallBack,
                             int protocolVersion,
                             int poolSize) {
        this(protocolLooper, mProtocolCallBack, new SocketDataQueueProducer(protocolVersion), poolSize);
    }

    public ProtocolProcessor(Looper protocolLooper,
                             IProtocolAnalysisResult mProtocolCallBack,
                             ISocketDataProducer mSocketDataProducer,
                             int poolSize) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolProcessor> protocolLooper==null . ");
        }

        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <ProtocolProcessor> ISocketDataProducer==null . ");
        }

        this.mDataResolveQueue = new XXDataResolveQueue(protocolLooper,
                new DataInspectorPool(new DataInspector(mProtocolCallBack), poolSize),
                mSocketDataProducer);
    }

    @Override
    public void release() {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.release();
            mDataResolveQueue = null;
        }
    }

    @Deprecated
    public void onInReceiveData(ReceivesData mData) {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.postReceiveDataToQueue(mData);
        }
    }

    @Override
    public void onInputServerData(ReceivesData mReceivesData) {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.postReceiveDataToQueue(mReceivesData);
        }
    }
}
