package cn.com.swain.support.protocolEngine.IO;

import cn.com.swain.support.protocolEngine.pack.ResponseData;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :数据包的输出
 */

public interface IDataProtocolOutput {

    void onOutputDataToServer(ResponseData mResponseData);

    void onBroadcastDataToServer(ResponseData mResponseData);

}
