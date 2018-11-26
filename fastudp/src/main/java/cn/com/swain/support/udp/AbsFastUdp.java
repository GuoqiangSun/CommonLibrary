package cn.com.swain.support.udp;

import java.net.DatagramSocket;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/13 0013
 * Desc:
 */
public abstract class AbsFastUdp {

    public abstract void init();

    public abstract void regSocketResult(ISocketResult mResult);

    public abstract void unregSocketResult(ISocketResult mResult);

    public abstract DatagramSocket getDatagramSocket();

    public abstract void release();

    public abstract void broadcast(UdpResponseMsg mResponseData);

    public abstract void broadcastDelay(UdpResponseMsg mResponseData);

    public abstract void broadcastDelay(UdpResponseMsg mResponseData, long delay, long maxDelay);

    public abstract void write(UdpResponseMsg mResponseData);

    public abstract void writeDelay(UdpResponseMsg mResponseData);

    public abstract void writeDelay(UdpResponseMsg mResponseData, long delay, long maxDelay);
}
