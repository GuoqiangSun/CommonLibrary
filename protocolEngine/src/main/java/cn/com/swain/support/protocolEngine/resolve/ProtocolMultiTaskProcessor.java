package cn.com.swain.support.protocolEngine.resolve;

import android.os.Looper;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.DataInspector.DataInspectorPool;
import cn.com.swain.support.protocolEngine.DataInspector.DataResolveInspector;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.UnknownVersionException;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc : 单渠道,多任务
 */

public class ProtocolMultiTaskProcessor extends AbsProtocolProcessor {

    private DataResolveQueue mDataResolveQueue;

    public ProtocolMultiTaskProcessor(Looper protocolLooper,
                                      IProtocolAnalysisResult mProtocolCallBack,
                                      int protocolVersion,
                                      int callBackPoolSize,
                                      boolean supportLargerPkg) {
        this(protocolLooper, mProtocolCallBack, protocolVersion,
                new SocketDataQueueProducer(protocolVersion),
                supportLargerPkg ? new SocketDataQueueProducer(protocolVersion) : null,
                callBackPoolSize);
    }

    /**
     * @param protocolLooper            解析线程
     * @param mProtocolCallBack         回调
     * @param mSocketDataProducer       一般包的生产者
     * @param mLargerSocketDataProducer 超大包的生产者
     * @param callBackPoolSize          回调线程池的大小
     */
    public ProtocolMultiTaskProcessor(Looper protocolLooper,
                                      IProtocolAnalysisResult mProtocolCallBack,
                                      int protocolVersion,
                                      ISocketDataProducer mSocketDataProducer,
                                      ISocketDataProducer mLargerSocketDataProducer,
                                      int callBackPoolSize) {

        if (mProtocolCallBack == null) {
            throw new NullPointerException(" <ProtocolMultiTaskProcessor> IProtocolAnalysisResult==null . ");
        }

        if (protocolLooper == null) {
            throw new NullPointerException(" <ProtocolMultiTaskProcessor> protocolLooper==null . ");
        }

        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <ProtocolMultiTaskProcessor> ISocketDataProducer==null . ");
        }


        if (ProtocolBuild.VERSION.isQXVersion(protocolVersion)) {
            this.mDataResolveQueue = new QxDataResolveQueue(protocolLooper,
                    new DataInspectorPool(new DataResolveInspector(mProtocolCallBack), callBackPoolSize),
                    mSocketDataProducer,
                    mLargerSocketDataProducer);
        } else {
            throw new UnknownVersionException(" ProtocolMultiTaskProcessor unknown version:" + protocolVersion);
        }
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
        } else {
            Tlog.e(TAG, " ProtocolMultiTaskProcessor onInputServerData mDataResolveQueue=null :"
                    + String.valueOf(mReceivesData));
        }
    }
}
