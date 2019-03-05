package cn.com.swain.support.protocolEngine.resolve;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/30 0030
 * desc :
 */
public class ResolveData {

    /**
     * 设备
     */
    public String device;

    /**
     * 是否解析到头字节
     */
    public  volatile boolean hasHead = false;

    /**
     * 解析到的数据包大小
     */
    public  int count = 0;

    /**
     *
     */
    public  SocketDataArray mLastSocketDataArray;

    /**
     * 是否是超级大的包
     */
    public   boolean isLargerPkg;

}
