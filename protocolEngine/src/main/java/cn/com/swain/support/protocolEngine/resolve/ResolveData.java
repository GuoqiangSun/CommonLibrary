package cn.com.swain.support.protocolEngine.resolve;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/30 0030
 * desc :
 */
class ResolveData {

    /**
     * 设备
     */
    String device;

    /**
     * 是否解析到头字节
     */
    volatile boolean hasHead = false;

    /**
     * 解析到的数据包大小
     */
    int count = 0;

    /**
     *
     */
    SocketDataArray mLastSocketDataArray;

    /**
     * 是否是超级大的包
     */
    boolean isLargerPkg;

}
