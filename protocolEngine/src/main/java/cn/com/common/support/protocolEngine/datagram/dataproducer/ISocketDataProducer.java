package cn.com.common.support.protocolEngine.datagram.dataproducer;

import cn.com.common.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public interface ISocketDataProducer {

    SocketDataArray produceSocketDataArray();

    void clear();

}
