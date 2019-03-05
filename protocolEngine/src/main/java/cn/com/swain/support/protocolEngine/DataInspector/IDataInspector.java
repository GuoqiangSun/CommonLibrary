package cn.com.swain.support.protocolEngine.DataInspector;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :  数据质量检测员
 */

public interface IDataInspector {

    /**
     * 解析出来的数据
     *
     * @param code             返回码 {@link cn.com.swain.support.protocolEngine.ProtocolCode}
     * @param mSocketDataArray 数据类
     */
    void inspectData(int code, SocketDataArray mSocketDataArray);

    /**
     * 释放
     */
    void release();
}
