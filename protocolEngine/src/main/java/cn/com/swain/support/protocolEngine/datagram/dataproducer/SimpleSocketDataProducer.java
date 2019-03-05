package cn.com.swain.support.protocolEngine.datagram.dataproducer;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/1 0001
 * Desc:
 */
public class SimpleSocketDataProducer implements ISocketDataProducer {

    private final int version;

    public SimpleSocketDataProducer(int version) {
        this.version = version;
    }

    @Override
    public void create() {
        // ignore
    }

    @Override
    public SocketDataArray produceSocketDataArray() {
        return new SocketDataArray(version);
    }

    @Override
    public void clear() {
        // ignore
    }
}
