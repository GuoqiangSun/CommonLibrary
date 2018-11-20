package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

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
 * <p>
 * // 第二份协议
 * <p>
 * header(uint8_t)
 * len_h（uint8_t）	len_l（uint8_t）
 * version（uint8_t）
 * sequence（uint8_t）
 * reserve4（uint8_t）	reserve3（uint8_t）	reserve2（uint8_t）	reserve1（uint8_t）
 * custom_h（uint8_t）	custom_l（uint8_t）
 * type（uint8_t）	cmd（uint8_t）
 * data[...]（uint8_t）
 * CRC8（uint8_t）
 * tail（uint8_t）
 */

public interface IProtocolComData {

    /**
     * 获取协议头部字节
     *
     * @return int8 head
     */
    byte getProtocolHead();

    /**
     * 获取有效数据长度
     *
     * @return int16 length
     */
    int getProtocolValidLength();

    /**
     * 获取协议版本
     *
     * @return int8 version
     */
    int getProtocolVersion();

    /**
     * 获取包的序列
     *
     * @return int8 sequence
     */
    int getProtocolSequence();

    /**
     * 获取token
     *
     * @return int8 reserve
     */
    int getProtocolToken(int point);

    /**
     * 获取token
     *
     * @return int32 reserve
     */
    int getProtocolToken();


    /**
     * 获取custom
     *
     * @return int8 custom
     */
    int getProtocolCustom();


    /**
     * 获取product
     *
     * @return int8 product
     */
    int getProtocolProduct();


    /**
     * 获取协议类型
     *
     * @return int8 type
     */
    byte getProtocolType();

    /**
     * 获取协议指令
     *
     * @return int8 cmd
     */
    byte getProtocolCmd();

    /**
     * 获取参数数据长度
     *
     * @return int16 param length
     */
    int getProtocolParamsLength();

    /**
     * 获取参数
     *
     * @return params
     */
    byte[] getProtocolParams();


    /**
     * 获取需要校验的数据
     *
     * @return crc data
     */
    byte[] getProtocolNeedCheckData();


    /**
     * 获取协议校验字节
     *
     * @return int8 crc
     */
    byte getProtocolCrc8();

    /**
     * 获取协议尾部字节
     *
     * @return int tail
     */
    byte getProtocolTail();


}
