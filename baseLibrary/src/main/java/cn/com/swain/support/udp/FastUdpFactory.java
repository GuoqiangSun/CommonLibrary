package cn.com.swain.support.udp;

import android.os.Looper;

import java.net.InetAddress;

import cn.com.swain.support.udp.impl.BuildUDPParams;
import cn.com.swain.support.udp.impl.UDPWrapper;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/24 0024
 * Desc:
 */
public class FastUdpFactory {

    /**
     * 创建一个udp
     *
     * @param mLooper 写线程
     * @param mParams 参数的{@link BuildUDPParams#groupInet}不是null,
     *                则创建多播udp,否则创建单播udp
     * @return {@link UDPWrapper}
     */
    public static AbsFastUdp newFastUdp(Looper mLooper, BuildUDPParams mParams) {
        return new UDPWrapper(mLooper, mParams);
    }

    /**
     * 单播udp
     *
     * @param mLooper 写线程
     * @return {@link UDPWrapper}
     */
    public static AbsFastUdp newFastUniUdp(Looper mLooper) {
        return new UDPWrapper(mLooper, new BuildUDPParams());
    }

    /**
     * 单播udp
     *
     * @param mLooper      写线程
     * @param bindPort     绑定端口
     * @param reuseAddress 端口重用
     * @return {@link UDPWrapper}
     */
    public static AbsFastUdp newFastUniUdp(Looper mLooper, int bindPort, boolean reuseAddress) {
        BuildUDPParams mParams = new BuildUDPParams();
        mParams.bindPort = bindPort;
        mParams.isReuseAddress = reuseAddress;
        return new UDPWrapper(mLooper, mParams);
    }

    /**
     * 多播udp
     *
     * @param mLooper 写线程
     * @param group   组播ip
     * @return {@link UDPWrapper}
     */
    public static AbsFastUdp newFastMultiUdp(Looper mLooper, InetAddress group) {
        BuildUDPParams mParams = new BuildUDPParams();
        mParams.groupInet = group;
        return new UDPWrapper(mLooper, mParams);
    }

    /**
     * 多播udp
     *
     * @param mLooper      写线程
     * @param group        组播ip
     * @param bindPort     绑定端口
     * @param reuseAddress 端口重用
     * @return {@link UDPWrapper}
     */
    public static AbsFastUdp newFastMultiUdp(Looper mLooper, InetAddress group, int bindPort, boolean reuseAddress) {
        BuildUDPParams mParams = new BuildUDPParams();
        mParams.groupInet = group;
        mParams.bindPort = bindPort;
        mParams.isReuseAddress = reuseAddress;
        return new UDPWrapper(mLooper, mParams);
    }

}
