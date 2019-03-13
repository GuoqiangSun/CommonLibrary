package cn.com.swain.support.protocolEngine.IO;

import cn.com.swain.support.protocolEngine.pack.ResponseData;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :数据包的输出
 */

public interface IDataProtocolOutput {

    /**
     * 输出一包socket协议数据
     *
     * @param mResponseData 数据
     */
    void onOutputProtocolData(ResponseData mResponseData);

    /**
     * 广播一包socket协议数据
     *
     * @param mResponseData 数据
     */
    void onBroadcastProtocolData(ResponseData mResponseData);

}
