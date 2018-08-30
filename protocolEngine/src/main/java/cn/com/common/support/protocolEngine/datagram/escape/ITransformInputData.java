package cn.com.common.support.protocolEngine.datagram.escape;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public interface ITransformInputData {


    /**
     * 添加头字节
     * @param b
     */
    void onAddHead(byte b);

    /**
     * 添加尾字节
     * @param b
     */
    void onAddTail(byte b);

    /**
     * 添加一个字节并反转义
     *
     * @param b
     */
    void onAddDataReverse(byte b);

    /**
     * 添加一包并反转义
     *
     * @param
     */
    void onAddPackageReverse(byte[] pack);

    /**
     * 添加一个字节并转义
     *
     * @param b
     */
    void onAddDataEscape(byte b);

    /**
     * 添加一包并转义
     *
     * @param
     */
    void onAddPackageEscape(byte[] pack);



}
