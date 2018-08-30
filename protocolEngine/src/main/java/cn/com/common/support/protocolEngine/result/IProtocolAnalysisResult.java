package cn.com.common.support.protocolEngine.result;

import cn.com.common.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public interface IProtocolAnalysisResult {

    /**
     *
     */
    void onFailReceiveDataNull(int errorCode);

    /**
     * 数据包文太长
     */
    void onFailLengthTooLong(int errorCode);

    /**
     * 有头无尾部
     */
    void onFailHasHeadNoTail(int errorCode);

    /**
     * 收到尾部数据包但没收到头部数据包
     */
    void onFailHasTailNoHead(int errorCode);

    /**
     * 数据报文空指针
     */
    void onPackNullError(int errorCode);

    /**
     * 数据报文没头
     */
    void onPackNoHeadError(int errorCode, SocketDataArray mSocketDataArray);


    /**
     * 数据报文crc校验错误
     */
    void onPackCrcError(int errorCode, SocketDataArray mSocketDataArray);


    /**
     * 数据报文没尾
     */
    void onPackNoTailError(int errorCode, SocketDataArray mSocketDataArray);

    void onSuccess(SocketDataArray mSocketDataArray);

}
