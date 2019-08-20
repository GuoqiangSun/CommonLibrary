package cn.com.swain.support.udp.impl;

import java.net.InetAddress;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/6 0006
 * Desc:
 */
public class BuildUDPParams {

    /**
     * 地址重用
     */
    public boolean isReuseAddress = false;

    /**
     * bind 端口
     */
    public int bindPort = -1;

    /**
     * 加入组
     */
    public InetAddress groupInet = null;

    /**
     * 接收包的缓冲大小
     */
    public int recPkgLength = 1024;

}
