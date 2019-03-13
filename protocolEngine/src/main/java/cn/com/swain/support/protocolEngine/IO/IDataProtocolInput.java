package cn.com.swain.support.protocolEngine.IO;

import cn.com.swain.support.protocolEngine.pack.ReceivesData;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc : 数据包的输入
 */

public interface IDataProtocolInput {

    /**
     * 输入一包socket协议数据,解析后,实现业务功能
     *
     * @param mReceivesData 数据
     */
    void onInputProtocolData(ReceivesData mReceivesData);

}
