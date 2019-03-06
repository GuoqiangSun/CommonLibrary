package cn.com.swain.support.protocolEngine.resolve;

import android.os.Looper;
import android.util.SparseArray;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc : 多渠道
 */

public class ProtocolMultiChannelProcessor extends AbsProtocolProcessor {

    private SparseArray<AbsProtocolProcessor> mSingChannelMap;
    private final Object synObj = new byte[1];

    private int protocolVersion;
    private Looper protocolLooper;
    private IProtocolAnalysisResult mProtocolCallBack;
    private int callBackPoolSize;
    private ISocketDataProducer mSocketDataProducer;
    private ISocketDataProducer mLargerSocketDataProducer;

    /**
     * @param protocolLooper            解析线程
     * @param mProtocolCallBack         回调
     * @param mSocketDataProducer       一般包的生产者
     * @param mLargerSocketDataProducer 超大包的生产者
     * @param callBackPoolSize          回调线程池的大小
     */
    public ProtocolMultiChannelProcessor(Looper protocolLooper,
                                         IProtocolAnalysisResult mProtocolCallBack,
                                         int protocolVersion,
                                         ISocketDataProducer mSocketDataProducer,
                                         ISocketDataProducer mLargerSocketDataProducer,
                                         int callBackPoolSize) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolMultiChannelProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolMultiChannelProcessor> protocolLooper==null . ");
        }
        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <ProtocolMultiChannelProcessor> ISocketDataProducer==null . ");
        }
        this.mSingChannelMap = new SparseArray<>(8);
        this.protocolLooper = protocolLooper;
        this.protocolVersion = protocolVersion;
        this.callBackPoolSize = callBackPoolSize;
        this.mSocketDataProducer = mSocketDataProducer;
        this.mLargerSocketDataProducer = mLargerSocketDataProducer;
        this.mProtocolCallBack = mProtocolCallBack;
    }


    @Override
    public void release() {
        synchronized (synObj) {
            SparseArray<AbsProtocolProcessor> singChannelMap = this.mSingChannelMap;
            this.mSingChannelMap = null;
            for (int i = 0; i < singChannelMap.size(); i++) {
                int i1 = singChannelMap.keyAt(i);
                AbsProtocolProcessor dataResolveQueue = singChannelMap.get(i1);
                if (dataResolveQueue != null) {
                    dataResolveQueue.release();
                }
            }
            singChannelMap.clear();
            mProtocolCallBack = null;
            protocolLooper = null;
            mSocketDataProducer = null;
            mLargerSocketDataProducer = null;
        }
    }

    @Override
    public void onInputServerData(ReceivesData mReceivesData) {
        AbsProtocolProcessor mDataResolveQueue = getDataResolveQueue(mReceivesData);
        if (mDataResolveQueue != null) {
            mDataResolveQueue.onInputServerData(mReceivesData);
        } else {
            Tlog.e(TAG, " ProtocolMultiChannelProcessor onInputServerData mDataResolveQueue=null :"
                    + String.valueOf(mReceivesData));
        }
    }

    private AbsProtocolProcessor getDataResolveQueue(ReceivesData mReceivesData) {

        int model = mReceivesData.getReceiveModel().getModel();
        return getDataResolveQueue(model);
    }

    private AbsProtocolProcessor getDataResolveQueue(int channel) {
        if (mSingChannelMap == null) {
            Tlog.e(TAG, " getDataResolveQueue mSingChannelMap=null already release");
            return null;
        }

        AbsProtocolProcessor dataResolveQueue = mSingChannelMap.get(channel);
        if (dataResolveQueue == null) {
            synchronized (synObj) {

                dataResolveQueue = mSingChannelMap.get(channel);

                if (dataResolveQueue == null) {

                    dataResolveQueue = ProtocolProcessorFactory.newSingleChannelMultiTask(protocolLooper,
                            mProtocolCallBack,
                            protocolVersion,
                            mSocketDataProducer,
                            mLargerSocketDataProducer,
                            callBackPoolSize);

                    mSingChannelMap.put(channel, dataResolveQueue);
                }

            }
        }

        return dataResolveQueue;
    }

}
