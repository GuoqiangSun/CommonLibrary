package cn.com.swain.support.udp;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */
@Deprecated
public interface ISocketResult {

    void onSocketInitResult(boolean result, String ip, int port);

    void onSocketReceiveData(String ip, int port, byte[] data);

    void onSocketReleaseResult(boolean result);

//    void broadc

}
