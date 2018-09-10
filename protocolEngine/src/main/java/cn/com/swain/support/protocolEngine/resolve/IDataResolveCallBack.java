package cn.com.swain.support.protocolEngine.resolve;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :  解析回调
 */

public interface IDataResolveCallBack {

    void onOutDataResolve(int code, SocketDataArray mSocketDataArray);

    void release();
}
