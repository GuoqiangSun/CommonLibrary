package cn.com.swain.support.udp.impl;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/6 0006
 * Desc:
 */
public interface IUDPResult {

    /**
     * UDP init
     *
     * @param result init result
     * @param ip     init ip
     * @param port   init port
     */
    void onUDPInitResult(boolean result, String ip, int port);

    /**
     * udp receive data
     *
     * @param ip   receive data ip
     * @param port receive data port
     * @param data receive data
     */
    void onUDPReceiveData(String ip, int port, byte[] data);

    /**
     * udp release
     *
     * @param result release result
     */
    void onUDPReleaseResult(boolean result);

}
