package cn.com.swain.support.protocolEngine;

import android.os.Looper;

import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.resolve.ProtocolMultiChannelProcessor;
import cn.com.swain.support.protocolEngine.resolve.ProtocolMultiTaskProcessor;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/9 0009
 * desc :
 */
public class ProtocolProcessorFactory {

    /**
     * 单线程 单渠道 解析，单线程回调,
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 解析回调
     * @param protocolVersion   协议版本
     * @param supportLargerPkg  true
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *                          false
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newSingleChannelSingleTask(Looper protocolLooper,
                                                                  IProtocolAnalysisResult mProtocolCallBack,
                                                                  int protocolVersion,
                                                                  boolean supportLargerPkg) {
        return newSingleChannelMultiTask(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                1,
                supportLargerPkg);
    }


    /**
     * 单线程 单渠道 解析，多线程回调,
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 解析回调
     * @param protocolVersion   协议版本
     * @param callBackPollSize  回调线程池的大小
     * @param supportLargerPkg  是否支持超大包
     *                          true
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *                          false
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newSingleChannelMultiTask(Looper protocolLooper,
                                                                 IProtocolAnalysisResult mProtocolCallBack,
                                                                 int protocolVersion,
                                                                 int callBackPollSize,
                                                                 boolean supportLargerPkg) {
        return new ProtocolMultiTaskProcessor(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                new SocketDataQueueProducer(protocolVersion),
                supportLargerPkg ? new SocketDataQueueProducer(protocolVersion) : null,
                callBackPollSize);
    }

    /**
     * 单线程 单渠道 解析，多线程回调,
     *
     * @param protocolLooper            解析线程
     * @param mProtocolCallBack         解析回调
     * @param protocolVersion           协议版本
     * @param mSocketDataProducer       一般包的生产者
     * @param mLargerSocketDataProducer 超大包的生产者
     * @param callBackPoolSize          回调线程池的大小
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newSingleChannelMultiTask(Looper protocolLooper,
                                                                 IProtocolAnalysisResult mProtocolCallBack,
                                                                 int protocolVersion,
                                                                 ISocketDataProducer mSocketDataProducer,
                                                                 ISocketDataProducer mLargerSocketDataProducer,
                                                                 int callBackPoolSize) {
        return new ProtocolMultiTaskProcessor(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                mSocketDataProducer,
                mLargerSocketDataProducer,
                callBackPoolSize);
    }


    /**
     * 单线程 多渠道 解析，单线程回调
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 解析回调
     * @param protocolVersion   协议版本
     * @param supportLargerPkg  是否支持超大包
     *                          true
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *                          false
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newMultiChannelSingleTask(Looper protocolLooper,
                                                                 IProtocolAnalysisResult mProtocolCallBack,
                                                                 int protocolVersion,
                                                                 boolean supportLargerPkg) {
        return newMultiChannelMultiTask(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                1,
                supportLargerPkg);
    }


    /**
     * 单线程 多渠道 解析，多线程回调
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 解析回调
     * @param protocolVersion   协议版本
     * @param supportLargerPkg  是否支持超大包
     *                          true
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *                          false
     *                          解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newMultiChannelMultiTask(Looper protocolLooper,
                                                                IProtocolAnalysisResult mProtocolCallBack,
                                                                int protocolVersion,
                                                                int callBackPollSize,
                                                                boolean supportLargerPkg) {
        return new ProtocolMultiChannelProcessor(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                new SocketDataQueueProducer(protocolVersion),
                supportLargerPkg ? new SocketDataQueueProducer(protocolVersion) : null,
                callBackPollSize);
    }

    /**
     * 单线程 多渠道 解析，多线程回调
     *
     * @param protocolLooper            解析线程
     * @param mProtocolCallBack         解析回调
     * @param protocolVersion           协议版本
     * @param mSocketDataProducer       一般包的生产者
     * @param mLargerSocketDataProducer 超大包的生产者
     * @param callBackPoolSize          回调线程池的大小
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newMultiChannelMultiTask(Looper protocolLooper,
                                                                IProtocolAnalysisResult mProtocolCallBack,
                                                                int protocolVersion,
                                                                ISocketDataProducer mSocketDataProducer,
                                                                ISocketDataProducer mLargerSocketDataProducer,
                                                                int callBackPoolSize) {
        return new ProtocolMultiChannelProcessor(protocolLooper,
                mProtocolCallBack,
                protocolVersion,
                mSocketDataProducer,
                mLargerSocketDataProducer,
                callBackPoolSize);
    }

}
