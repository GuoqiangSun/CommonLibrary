package cn.com.swain.support.udp;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */
public interface IUDPSocketResult {

    void onUDPSocketInitResult(boolean result, String ip, int port);

    void onUDPSocketReceiveData(String ip, int port, byte[] data);

    void onUDPSocketReleaseResult(boolean result);

//    void broadc

}
