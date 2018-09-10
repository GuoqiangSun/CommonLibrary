package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

import cn.com.swain.support.protocolEngine.utils.ProtocolCode;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public abstract class AbsProtocolDataPack implements IProtocolComData {

    protected byte head = ProtocolCode.STX;

    byte length_h;
    byte length_l;

    protected byte reserve_0;
    protected byte reserve_1;
    protected byte reserve_2;
    protected byte reserve_3;

    protected byte version;
    protected byte seq;

    protected byte custom;
    protected byte product;
    protected byte type;
    protected byte cmd;

    protected byte[] params;

    byte crc;

    protected byte tail = ProtocolCode.ETX;

    public abstract byte getHead();

    public abstract byte getLengthH();

    public abstract byte getLengthL();

    public abstract int getLength();

    public abstract void setVersion(byte version);

    public abstract int getVersion();

    public abstract void setSeq(byte seq);

    public abstract int getSeq();

    public abstract void setCustom(byte custom);

    public abstract byte getCustom();

    public abstract void setProduct(byte product);

    public abstract byte getProduct();

    public abstract void setType(byte type);

    public abstract byte getType();

    public abstract void setCmd(byte cmd);

    public abstract byte getCmd();

    public abstract void setParams(byte[] params);

    public abstract byte[] getParams();

    public abstract byte getCrc();

    public abstract byte getTail();

    /**
     * 组包
     *
     * @return
     */
    public abstract byte[] organizeProtocolData();


}
