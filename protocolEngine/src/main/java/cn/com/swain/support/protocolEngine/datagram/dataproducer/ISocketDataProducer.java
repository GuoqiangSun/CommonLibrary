package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public interface ISocketDataProducer {

    void create();

    SocketDataArray produceSocketDataArray();

    void clear();

}
