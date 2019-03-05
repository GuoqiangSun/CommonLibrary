package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.QX;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.CrcUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram.AbsProtocolDataPack;
import cn.com.swain.support.protocolEngine.datagram.escape.IEscapeDataArray;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 * <p>
 * <p>
 * *  startAI通信包协议格式
 * 0xff（帧头）(1) + 有效数据长度(2) + 版本(1)+序号（1）+token(4)
 * + custom(1)+product(1) + 命令（类型 + 命令）(2) + 参数（变长）+ 校验(CRC8)(1) + 0xee（帧尾）(1)
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

public class QXProtocolComData2 extends AbsProtocolDataPack {

    private final IEscapeDataArray mComDataArray;

    public QXProtocolComData2(IEscapeDataArray mComDataArray) {
        this.mComDataArray = mComDataArray;
        setVersion((byte) ProtocolBuild.QX.QX_VERSION_SEQ);
        setHead(ProtocolBuild.QX.STX);
        setTail(ProtocolBuild.QX.ETX);
    }

    public static final int LENGTH_BASE_VERSION = 15;

    public static final int POINT_HEAD = 0;

    public static final int POINT_LENGTH_START = 1;
    public static final int POINT_LENGTH_END = 2;

    public static final int CRC_CHECK_START = 3;

    public static final int POINT_VERSION = 3;
    public static final int POINT_SEQUENCE = 4;

    public static final int TOKEN_0 = 5;
    public static final int TOKEN_1 = 6;
    public static final int TOKEN_2 = 7;
    public static final int TOKEN_3 = 8;


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
        byte[] params = getParams();
        final int paramsLength = params != null ? params.length : 0;
        final int allLength = LENGTH_BASE_VERSION + paramsLength;
        final int effectiveLength = allLength - 5;
        final byte[] buf = new byte[allLength];

        buf[POINT_HEAD] = getHead();
        buf[POINT_LENGTH_START] = (byte) ((effectiveLength >> 8) & 0xFF);
        buf[POINT_LENGTH_END] = (byte) (effectiveLength & 0xFF);
        setLength(effectiveLength);

        buf[POINT_VERSION] = getVersion();
        buf[POINT_SEQUENCE] = getSeq();

        int token = getToken();
        buf[TOKEN_0] = (byte) ((token >> 24) & 0xFF);
        buf[TOKEN_1] = (byte) ((token >> 16) & 0xFF);
        buf[TOKEN_2] = (byte) ((token >> 8) & 0xFF);
        buf[TOKEN_3] = (byte) (token & 0xFF);

        buf[POINT_CUSTOM] = getCustom();
        buf[POINT_PRODUCT] = getProduct();

        buf[POINT_TYPE] = getType();
        buf[POINT_CMD] = getCmd();

        if (paramsLength > 0) {
            System.arraycopy(params, 0, buf, POINT_PARAMS_START, paramsLength);
        }

        final int crcPoint = QXProtocolComData2.getCRCPoint(paramsLength);

        //        final int crcPoint =allLength - 2;

        final byte crc = CrcUtil.CRC8(buf, CRC_CHECK_START, crcPoint);
        buf[crcPoint] = crc;
        setCrc(crc);

        final int tailPoint = QXProtocolComData2.getTailPoint(crcPoint);
        buf[tailPoint] = getTail();

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
    public int getProtocolToken(int point) {
        switch (point) {
            case 0:
                return mComDataArray.getByte(TOKEN_0);
            case 1:
                return mComDataArray.getByte(TOKEN_1);
            case 2:
                return mComDataArray.getByte(TOKEN_2);
            case 3:
                return mComDataArray.getByte(TOKEN_3);
            default:
                return getProtocolToken();
        }

    }

    @Override
    public int getProtocolToken() {
        return (mComDataArray.getByte(TOKEN_0) & 0xFF) << 24
                | (mComDataArray.getByte(TOKEN_1) & 0xFF) << 16
                | (mComDataArray.getByte(TOKEN_2) & 0xFF) << 8
                | (mComDataArray.getByte(TOKEN_3) & 0xFF);
    }

    @Override
    public int getProtocolCustom() {
        return mComDataArray.getByte(POINT_CUSTOM);
    }

    @Override
    public int getProtocolProduct() {
        return mComDataArray.getByte(POINT_PRODUCT);
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
    public byte getNeedCheckDataCrc() {

        byte[] checkField;
        try {
            checkField = getProtocolNeedCheckData();
        } catch (Exception e) {

            Tlog.e(AbsProtocolProcessor.TAG, " getNeedCheckDataCrc() Exception:", e);

            return 0x00;
        }

        if (checkField == null) {
            return 0x00;
        }

        return CrcUtil.CRC8(checkField);

    }


    @Override
    public byte getProtocolCrc8() {
        return mComDataArray.getByte(getCrcPoint());

    }


    @Override
    public boolean hasProtocolHead() {
        return (getProtocolHead() == ProtocolBuild.QX.STX);
    }

    @Override
    public boolean checkProtocolCrc() {

        byte[] checkField;
        try {
            checkField = getProtocolNeedCheckData();
        } catch (Exception e) {

            Tlog.e(AbsProtocolProcessor.TAG, " checkProtocolCrc() Exception:", e);

            return false;
        }

        if (checkField == null) {
            return false;
        }

        byte b = CrcUtil.CRC8(checkField);

        byte protocolCrc8 = getProtocolCrc8();

        boolean crcResult = b == protocolCrc8;

        if (!crcResult && Tlog.isDebug()) {
            Tlog.e(AbsProtocolProcessor.TAG, "protocolCrc:" + Integer.toHexString(protocolCrc8)
                    + " calCrc:" + Integer.toHexString(b) + " calData:" + StrUtil.toString(checkField));
        }

        return crcResult;
    }

    @Override
    public boolean hasProtocolTail() {
        return (getProtocolTail() == ProtocolBuild.QX.ETX);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(" QXProtocolComData:");
        sb.append(" DATA_STATE : ").append(mComDataArray.getStateStr()).append(" , ");
        sb.append(" HEAD : ").append(Integer.toHexString(getProtocolHead() & 0xFF)).append(" , ");
        sb.append(" VALID_LENGTH : ").append(getProtocolValidLength()).append(" , ");
        sb.append(" TYPE : ").append(Integer.toHexString(getProtocolType() & 0xFF)).append(" , ");
        sb.append(" CMD : ").append(Integer.toHexString(getProtocolCmd() & 0xFF)).append(" , ");
        sb.append(" CRC8 : ").append(Integer.toHexString(getProtocolCrc8() & 0xFF)).append(" , ");

        byte[] protocolParams = getProtocolParams();

        if (protocolParams != null && protocolParams.length > 0) {
            sb.append(" PARAMS : ");
            for (byte b : protocolParams) {
                sb.append(Integer.toHexString(b & 0xFF)).append(" , ");
            }
        } else {
            sb.append(" PARAMS IS NULL ");
        }

        sb.append(" TAIL : ").append(Integer.toHexString(getProtocolTail() & 0xFF));
        sb.append(" : END. ");

        return sb.toString();
    }

}
