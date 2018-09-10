package cn.com.swain.support.protocolEngine;

import android.os.Looper;

import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/9 0009
 * desc :
 */
public class ProtocolProcessorFactory {

    /**
     * 单线程解析，多线程回调
     *
     * @param protocolLooper    解析线程
     * @param mProtocolCallBack 任务回调
     * @param protocolVersion   协议版本
     * @return ProtocolProcessor
     */
    public static ProtocolProcessor newSingleThreadAnalysisMutilTask(Looper protocolLooper,
                                                                     IProtocolAnalysisResult mProtocolCallBack,
                                                                     int protocolVersion) {
        return new ProtocolProcessor(protocolLooper, mProtocolCallBack, protocolVersion);
    }

}
