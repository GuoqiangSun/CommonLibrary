package cn.com.swain.support.protocolEngine.datagram.escape;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */
public interface ITransformInputData {


    /**
     * 添加头字节
     *
     * @param b
     */
    void onAddHead(byte b);

    /**
     * 是否是头字节
     *
     * @param b 需要检验的数据
     * @return true 是头部字节,false不是头部字节
     */
    boolean isHeadByte(byte b);

    /**
     * 添加尾字节
     *
     * @param b
     */
    void onAddTail(byte b);

    /**
     * 是否是尾部字节
     *
     * @param b 需要检验的数据
     * @return true 是尾部字节,false不是尾部字节
     */
    boolean isTailByte(byte b);

    /**
     * 是否是转义字符
     *
     * @param b 需要检验的数据
     * @return true 是尾部字节,false不是尾部字节
     */
    boolean isEscapeByte(byte b);

    /**
     * 添加一个字节并反转义
     *
     * @param b
     */
    void onAddDataReverse(byte b);

    /**
     * 添加一包并反转义
     *
     * @param pack
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
     * @param pack
     */
    void onAddPackageEscape(byte[] pack);

    /**
     * 监测是否是特殊字符,特殊字符是需要转义的
     *
     * @param b 数据
     * @return true ,是不需要转义的字节,false 是需要转义的字节
     */
    boolean checkIsSpecialByte(byte b);

}
