package cn.com.swain.support.protocolEngine.resolve;

import android.os.Looper;
import android.util.SparseArray;

import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;
import cn.com.swain.baselib.log.Tlog;

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
    private final boolean supportLargerPkg;
    private final IProtocolAnalysisResult mProtocolCallBack;
    private final int callBackPoolSize;

    public ProtocolMultiChannelProcessor(Looper protocolLooper,
                                         IProtocolAnalysisResult mProtocolCallBack,
                                         int protocolVersion,
                                         int callBackPoolSize,
                                         boolean supportLargerPkg) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolProcessor> protocolLooper==null . ");
        }

        this.protocolLooper = protocolLooper;
        this.protocolVersion = protocolVersion;
        this.callBackPoolSize = callBackPoolSize;
        this.mProtocolCallBack = mProtocolCallBack;
        this.supportLargerPkg = supportLargerPkg;
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
                            callBackPoolSize,
                            supportLargerPkg);

                    mSingChannelMap.put(channel, dataResolveQueue);
                }

            }
        }

        return dataResolveQueue;
    }

}
