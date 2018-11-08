package cn.com.swain.support.protocolEngine;

import android.os.Looper;

import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.resolve.ProtocolLargerProcessor;
import cn.com.swain.support.protocolEngine.resolve.ProtocolMaxProcessor;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/9 0009
 * desc :
 */
public class ProtocolProcessorFactory {


    /**
     * 单线程解析，单线程回调
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newSingleTaskMaxPkg(Looper protocolLooper,
                                                           IProtocolAnalysisResult mProtocolCallBack,
                                                           int protocolVersion) {
        return new ProtocolMaxProcessor(protocolLooper, mProtocolCallBack, protocolVersion);
    }


    /**
     * 单线程解析，多线程回调
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @param callBackPollSize  回调线程池的大小
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newMutilTaskMaxPkg(Looper protocolLooper,
                                                          IProtocolAnalysisResult mProtocolCallBack,
                                                          int protocolVersion, int callBackPollSize) {
        return new ProtocolMaxProcessor(protocolLooper, mProtocolCallBack, protocolVersion, callBackPollSize);
    }


    /**
     * 单线程解析，单线程回调,
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newSingleTaskLargerPkg(Looper protocolLooper,
                                                              IProtocolAnalysisResult mProtocolCallBack,
                                                              int protocolVersion) {
        return new ProtocolLargerProcessor(protocolLooper, mProtocolCallBack, protocolVersion);
    }


    /**
     * 单线程解析，多线程回调,
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#LARGER_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @param callBackPollSize  回调线程池的大小
     * @return ProtocolProcessor
     */
    public static AbsProtocolProcessor newMutilTaskLargerPkg(Looper protocolLooper,
                                                             IProtocolAnalysisResult mProtocolCallBack,
                                                             int protocolVersion, int callBackPollSize) {
        return new ProtocolLargerProcessor(protocolLooper, mProtocolCallBack, protocolVersion, callBackPollSize);
    }




    /**
     * 单线程解析，多线程回调
     * 解析包的大小不超过{@link cn.com.swain.support.protocolEngine.resolve.DataResolveQueue#MAX_COUNT}
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @return ProtocolProcessor
     */
    @Deprecated
    public static ProtocolProcessor newSingleThreadAnalysisMutilTask(Looper protocolLooper,
                                                                     IProtocolAnalysisResult mProtocolCallBack,
                                                                     int protocolVersion) {
        return new ProtocolProcessor(protocolLooper, mProtocolCallBack, protocolVersion);
    }

}
