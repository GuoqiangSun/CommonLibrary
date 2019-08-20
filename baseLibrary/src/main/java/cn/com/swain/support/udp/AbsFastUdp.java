package cn.com.swain.support.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import cn.com.swain.support.udp.impl.IUDPResult;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/13 0013
 * Desc:
 */
public abstract class AbsFastUdp {

    public static final String TAG = "fastUdp";

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 释放
     */
    public abstract void release();

    /**
     * 打印log
     */
    public abstract void showLog(boolean showLog);

    /**
     * 注册回调
     */
    public abstract void regUDPSocketResult(IUDPResult mResult);

    /**
     * 取消注册
     */
    public abstract void unregUDPSocketResult(IUDPResult mResult);

    /**
     * get udp socket
     */
    public abstract DatagramSocket getDatagramSocket();

    /**
     * 广播数据
     */
    public abstract void broadcast(DatagramPacket datagramPacket);

    /**
     * 广播数据
     */
    public abstract void broadcastDelay(DatagramPacket datagramPacket);

    /**
     * 广播数据
     */
    public abstract void broadcastDelay(DatagramPacket datagramPacket, long delay, long maxDelay);

    /**
     * 发送数据
     */
    public abstract void send(DatagramPacket datagramPacket);

    /**
     * 发送数据
     */
    public abstract void sendDelay(DatagramPacket datagramPacket);

    /**
     * 发送数据
     */
    public abstract void sendDelay(DatagramPacket datagramPacket, long delay, long maxDelay);
}
