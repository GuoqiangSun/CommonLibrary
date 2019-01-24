package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

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

    public SyncSocketDataQueueProducer(int version, int normalSize) {
        mSocketDataQueueProducer = new SocketDataQueueProducer(version, normalSize);
    }

    @Override
    public synchronized void create() {
        mSocketDataQueueProducer.create();
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
