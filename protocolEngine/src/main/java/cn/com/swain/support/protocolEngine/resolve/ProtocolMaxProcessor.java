package cn.com.swain.support.protocolEngine.resolve;

import android.os.Looper;

import cn.com.swain.support.protocolEngine.DataInspector.DataInspectorPool;
import cn.com.swain.support.protocolEngine.DataInspector.DataResolveInspector;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ProtocolMaxProcessor extends AbsProtocolProcessor {

    public static final String TAG = "ProtocolProcessor";
    private DataResolveQueue mDataResolveQueue;

    public ProtocolMaxProcessor(Looper protocolLooper,
                                IProtocolAnalysisResult mProtocolCallBack,
                                int protocolVersion) {
        this(protocolLooper, mProtocolCallBack, protocolVersion, 1);
    }

    public ProtocolMaxProcessor(Looper protocolLooper,
                                IProtocolAnalysisResult mProtocolCallBack,
                                int protocolVersion,
                                int poolSize) {
        this(protocolLooper, mProtocolCallBack, new SocketDataQueueProducer(protocolVersion), poolSize);
    }

    public ProtocolMaxProcessor(Looper protocolLooper,
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

        this.mDataResolveQueue = new DataResolveQueue(protocolLooper,
                new DataInspectorPool(new DataResolveInspector(mProtocolCallBack), poolSize),
                mSocketDataProducer);
    }

    @Override
    public void release() {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.release();
            mDataResolveQueue = null;
        }
    }

    @Override
    public void onInputServerData(ReceivesData mReceivesData) {
        if (mDataResolveQueue != null) {
            mDataResolveQueue.postReceiveDataToQueue(mReceivesData);
        }
    }
}
