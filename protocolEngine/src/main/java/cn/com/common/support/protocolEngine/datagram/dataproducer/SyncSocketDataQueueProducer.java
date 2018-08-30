package cn.com.common.support.protocolEngine.datagram.dataproducer;

import cn.com.common.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/3 0003
 * desc :
 */
public class SyncSocketDataQueueProducer implements ISocketDataProducer {

    private final ISocketDataProducer mSocketDataQueueProducer;

    public SyncSocketDataQueueProducer(int version) {
        mSocketDataQueueProducer = new SocketDataQueueProducer(version);
    }

    @Override
    public synchronized SocketDataArray produceSocketDataArray() {
        return mSocketDataQueueProducer.produceSocketDataArray();
    }

    @Override
    public synchronized void clear() {
        mSocketDataQueueProducer.clear();
    }
}
