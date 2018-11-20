package cn.com.swain.support.protocolEngine.resolve;

import android.os.Looper;
import android.util.SparseArray;

import cn.com.swain.support.protocolEngine.DataInspector.DataInspectorPool;
import cn.com.swain.support.protocolEngine.DataInspector.DataResolveInspector;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ProtocolMultichannelProcessor extends AbsProtocolProcessor {

    public static final String TAG = "ProtocolProcessor";

    private final SparseArray<DataResolveQueue> mDataResolveQueueMap;
    private final DataInspectorPool dataInspectorPool;
    private final int protocolVersion;
    private final Looper protocolLooper;
    private final boolean supportLargerPkg;


    public ProtocolMultichannelProcessor(Looper protocolLooper,
                                         IProtocolAnalysisResult mProtocolCallBack,
                                         int protocolVersion,
                                         int poolSize,
                                         boolean supportlargerPkg) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolProcessor> protocolLooper==null . ");
        }

        this.protocolLooper = protocolLooper;
        this.protocolVersion = protocolVersion;
        this.dataInspectorPool = new DataInspectorPool(new DataResolveInspector(mProtocolCallBack), poolSize);
        this.mDataResolveQueueMap = new SparseArray<>(8);
        this.supportLargerPkg = supportlargerPkg;
    }


    @Override
    public void release() {
        synchronized (synObj) {
            if (mDataResolveQueueMap != null) {

                for (int i = 0; i < mDataResolveQueueMap.size(); i++) {
                    int i1 = mDataResolveQueueMap.keyAt(i);
                    DataResolveQueue dataResolveQueue = mDataResolveQueueMap.get(i1);
                    if (dataResolveQueue != null) {
                        dataResolveQueue.release();
                    }
                }

                mDataResolveQueueMap.clear();
            }
        }
    }


    @Override
    public void onInputServerData(ReceivesData mReceivesData) {
        DataResolveQueue mDataResolveQueue = getDataResolveQueue(mReceivesData);
        if (mDataResolveQueue != null) {
            mDataResolveQueue.postReceiveDataToQueue(mReceivesData);
        }
    }

    private final Object synObj = new byte[1];

    private DataResolveQueue getDataResolveQueue(ReceivesData mReceivesData) {

        int model = mReceivesData.getReceiveModel().getModel();
        return getDataResolveQueue(model);
    }

    private DataResolveQueue getDataResolveQueue(int channel) {

        DataResolveQueue dataResolveQueue;
        synchronized (synObj) {
            dataResolveQueue = mDataResolveQueueMap.get(channel);
            if (dataResolveQueue == null) {
                dataResolveQueue = new DataResolveQueue(protocolLooper,
                        dataInspectorPool,
                        new SocketDataQueueProducer(protocolVersion),
                        supportLargerPkg ? new SocketDataQueueProducer(protocolVersion) : null);

                mDataResolveQueueMap.put(channel, dataResolveQueue);

            }
        }

        return dataResolveQueue;
    }

}
