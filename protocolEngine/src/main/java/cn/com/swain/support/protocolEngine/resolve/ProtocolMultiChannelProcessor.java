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

    private final SparseArray<AbsProtocolProcessor> mSingChannelMap = new SparseArray<>(8);
    private final Object synObj = new byte[1];

    private final int protocolVersion;
    private final Looper protocolLooper;
    private final IProtocolAnalysisResult mProtocolCallBack;
    private final int callBackPoolSize;
    private final ISocketDataProducer mSocketDataProducer;
    private final ISocketDataProducer mLargerSocketDataProducer;

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
            for (int i = 0; i < mSingChannelMap.size(); i++) {
                int i1 = mSingChannelMap.keyAt(i);
                AbsProtocolProcessor dataResolveQueue = mSingChannelMap.get(i1);
                if (dataResolveQueue != null) {
                    dataResolveQueue.release();
                }
            }
            mSingChannelMap.clear();
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
