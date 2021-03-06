package cn.com.swain.support.protocolEngine;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class ProtocolCode {

    /**
     * 内部出现空数据
     */
    public static final int ERROR_CODE_INTERNAL_RECEIVE_NULL = 0x01;
    /**
     * 没有头字节
     */
    public static final int ERROR_CODE_NO_HEAD = 0x02;

    /**
     * 没有尾字节
     */
    public static final int ERROR_CODE_NO_TAIL = 0x03;


    /**
     * crc8校验错误
     */
    public static final int ERROR_CODE_CRC = 0x04;

    /**
     * 内部出现空包
     */
    public static final int ERROR_CODE_INTERNAL_PKG_NULL = 0x05;

    /**
     * 没有尾字节
     */
    public static final int ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL = 0x06;
    /**
     * 没有尾字节
     */
    public static final int ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD = 0x07;

    /**
     * 数据太长
     */
    public static final int ERROR_CODE_RESOLVE_MORE_LENGTH = 0x08;

    /**
     * 解析不到这个TYPE
     */
    public static final int ERROR_CODE_RESOLVE_TYPE = 0x09;

    /**
     * 解析不到这个cmd
     */
    public static final int ERROR_CODE_RESOLVE_CMD = 0x0A;

    /**
     * 解析成功
     */
    public static final int SUCCESS_CODE_RESOLVE = 0x7F;


}
