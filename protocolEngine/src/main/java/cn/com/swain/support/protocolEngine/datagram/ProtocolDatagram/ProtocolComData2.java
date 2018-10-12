package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;
import cn.com.swain.support.protocolEngine.utils.CrcUtil;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 * <p>
 * <p>
 * *  startAI通信包协议格式
 * 0xff（帧头）(1) + 有效数据长度(2) + 保留(6)+ 保留(2) + 命令（类型 + 命令）(2) + 参数（变长）+ 校验(CRC8)(1) + 0xee（帧尾）(1)
 * <p>
 * header(uint_8)
 * len_h（uint8_t）  	len_l（uint_8）
 * reserve_h（uint_8）	reserve_l（uint_8）
 * type(uint_8)	       cmd(uint_8)
 * data[...]（uint_8）
 * CRC8(uint_8)
 * tail(uint_8)
 * <p>
 * <p>
 * <p>
 * 有效数据 : 包括保留字段、命令字段、参数字段
 * 保留字段 : 后续蓝牙、socket等通信需要再定义，串口默认为0
 * 命令字段 : 主动发送方命令字段为奇数，回复方命令字节+1
 * 参数字段 : 由uint_8类型数组组成，当数据包为回复类型时，data[0]表示ack结果，为0表示成功，>0 表示出错类型
 * 校验字段 : 有效字段[ 保留 + 命令（类型 + 命令） + 参数（变长）]作CRC8校验
 * <p>
 * <p>
 * STX - 0xff（帧头）	STX 转成 ESC 和 0xaa
 * ETX - 0xee（帧尾）	 ETX 转成 ESC 和 0x99
 * ESC - 0x55（转义符）	ESC 转成 ESC 和 0x00
 */

public class ProtocolComData2 extends ProtocolDataPackImpl {

    private final IEscapeDataArray mComDataArray;

    public ProtocolComData2() {
        this(null);
    }

    public ProtocolComData2(IEscapeDataArray mComDataArray) {
        this.mComDataArray = mComDataArray;
        this.version = ProtocolBuild.VERSION.VERSION_SEQ;
    }

    public static final int LENGTH_BASE_VERSION = 15;

    public static final int POINT_HEAD = 0;

    public static final int POINT_LENGTH_START = 1;
    public static final int POINT_LENGTH_END = 2;

    public static final int CRC_CHECK_START = 3;

    public static final int POINT_VERSION = 3;
    public static final int POINT_SEQUENCE = 4;

    public static final int POINT_RESERVE_0 = 5;
    public static final int POINT_RESERVE_1 = 6;
    public static final int POINT_RESERVE_2 = 7;
    public static final int POINT_RESERVE_3 = 8;


    public static final int POINT_CUSTOM = 9;
    public static final int POINT_PRODUCT = 10;

    public static final int POINT_TYPE = 11;
    public static final int POINT_CMD = 12;

    public static final int POINT_PARAMS_START = 13;

    private int getCrcPoint() {

        return getCRCPoint(getProtocolParamsLength());
    }

    private static int getCRCPoint(int paramsLength) {
        return POINT_PARAMS_START + paramsLength;
    }

    private int getTailPoint() {
        return getTailPoint(getCrcPoint());
    }

    private static int getTailPoint(int crcPoint) {
        return crcPoint + 1;
    }

    @Override
    public byte getProtocolHead() {
        return mComDataArray.getByte(POINT_HEAD);
    }

    @Override
    public byte getProtocolTail() {
        return mComDataArray.getByte(getTailPoint());
    }

    @Override
    public byte[] organizeProtocolData() {

        final int paramsLength = params != null ? params.length : 0;
        final int allLength = LENGTH_BASE_VERSION + paramsLength;
        final int effectiveLength = allLength - 5;
        final byte[] buf = new byte[allLength];

        buf[POINT_HEAD] = head;
        buf[POINT_LENGTH_START] = this.length_h = (byte) ((effectiveLength >> 8) & 0xFF);
        buf[POINT_LENGTH_END] = this.length_l = (byte) (effectiveLength & 0xFF);

        buf[POINT_VERSION] = version;
        buf[POINT_SEQUENCE] = seq;

        buf[POINT_RESERVE_0] = reserve_0;
        buf[POINT_RESERVE_1] = reserve_1;
        buf[POINT_RESERVE_2] = reserve_2;
        buf[POINT_RESERVE_3] = reserve_3;

        buf[POINT_CUSTOM] = custom;
        buf[POINT_PRODUCT] = product;

        buf[POINT_TYPE] = type;
        buf[POINT_CMD] = cmd;

        if (paramsLength > 0) {
            System.arraycopy(params, 0, buf, POINT_PARAMS_START, paramsLength);
        }

        final int crcPoint = ProtocolComData2.getCRCPoint(paramsLength);

        //        final int crcPoint =allLength - 2;

        buf[crcPoint] = this.crc = CrcUtil.CRC8(buf, CRC_CHECK_START, crcPoint);

        final int tailPoint = ProtocolComData2.getTailPoint(crcPoint);
        buf[tailPoint] = tail;

        return buf;
    }

    @Override
    public int getProtocolVersion() {
        return mComDataArray.getByte(POINT_VERSION) & 0xFF;
    }

    @Override
    public int getProtocolSequence() {
        return mComDataArray.getByte(POINT_SEQUENCE) & 0xFF;
    }

    @Override
    public int getProtocolValidLength() {
        return (mComDataArray.getByte(POINT_LENGTH_START) & 0xFF) << 8 | (mComDataArray.getByte(POINT_LENGTH_END) & 0xFF);
    }

    @Override
    public int getProtocolParamsLength() {
        return (getProtocolValidLength() - 10);
    }

    @Override
    public byte getProtocolType() {
        return mComDataArray.getByte(POINT_TYPE);
    }

    @Override
    public byte getProtocolCmd() {
        return mComDataArray.getByte(POINT_CMD);
    }

    @Override
    public byte[] getProtocolNeedCheckData() {

        return mComDataArray.toArray(CRC_CHECK_START, getProtocolValidLength());

    }

    @Override
    public byte[] getProtocolParams() {

        int length = getProtocolParamsLength();

        if (length <= 0) {
            return null;
        }

        return mComDataArray.toArray(POINT_PARAMS_START, length);

    }


    @Override
    public byte getProtocolCrc8() {
        return mComDataArray.getByte(getCrcPoint());

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(" ProtocolComData:");
        sb.append(" DATA_STATE : " + mComDataArray.getStateStr() + " , ");
        sb.append(" HEAD : " + Integer.toHexString(getProtocolHead() & 0xFF) + " , ");
        sb.append(" VALID_LENGTH : " + getProtocolValidLength() + " , ");
        sb.append(" TYPE : " + Integer.toHexString(getProtocolType() & 0xFF) + " , ");
        sb.append(" CMD : " + Integer.toHexString(getProtocolCmd() & 0xFF) + " , ");
        sb.append(" CRC8 : " + Integer.toHexString(getProtocolCrc8() & 0xFF) + " , ");

        byte[] protocolParams = getProtocolParams();

        if (protocolParams != null && protocolParams.length > 0) {
            sb.append(" PARAMS : ");
            for (byte b : protocolParams) {
                sb.append(Integer.toHexString(b & 0xFF) + " , ");
            }
        } else {
            sb.append(" PARAMS IS NULL ");
        }

        sb.append(" TAIL : " + Integer.toHexString(getProtocolTail() & 0xFF));
        sb.append(" : END. ");

        return sb.toString();
    }

}