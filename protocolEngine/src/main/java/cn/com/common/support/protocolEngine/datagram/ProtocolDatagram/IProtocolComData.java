package cn.com.common.support.protocolEngine.datagram.ProtocolDatagram;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 * <p>
 * * 0xff（帧头） + 有效数据长度 + 保留 + 命令（类型 + 命令） + 参数（变长）+ 校验(CRC8) + 0xee（帧尾）
 * <p>
 * header(uint_8)
 * <p>
 * len_h（uint8_t）  	len_l（uint_8）
 * <p>
 * reserve_h（uint_8）	reserve_l（uint_8）
 * <p>
 * type(uint_8)	       cmd(uint_8)
 * <p>
 * data[...]（uint_8）
 * <p>
 * CRC8(uint_8)
 * <p>
 * tail(uint_8)
 */

public interface IProtocolComData {

    /**
     * 获取协议头部字节
     *
     * @return
     */
    byte getProtocolHead();

    /**
     * 获取有效数据长度
     *
     * @return
     */
    int getProtocolValidLength();

    /**
     * 获取协议版本
     *
     * @return
     */
    int getProtocolVersion();

    /**
     * 获取包的序列
     *
     * @return
     */
    int getProtocolSequence();


    /**
     * 获取协议类型
     *
     * @return
     */
    byte getProtocolType();

    /**
     * 获取协议指令
     *
     * @return
     */
    byte getProtocolCmd();

    /**
     * 获取参数数据长度
     *
     * @return
     */
    int getProtocolParamsLength();

    /**
     * 获取参数
     *
     * @return
     */
    byte[] getProtocolParams();


    /**
     * 获取需要校验的数据
     *
     * @return
     */
    byte[] getProtocolNeedCheckData();


    /**
     * 获取协议校验字节
     *
     * @return
     */
    byte getProtocolCrc8();

    /**
     * 获取协议尾部字节
     *
     * @return
     */
    byte getProtocolTail();


}
