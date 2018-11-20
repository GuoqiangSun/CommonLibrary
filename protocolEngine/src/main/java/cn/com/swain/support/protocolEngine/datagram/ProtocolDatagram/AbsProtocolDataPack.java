package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

import cn.com.swain.support.protocolEngine.ProtocolCode;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public abstract class AbsProtocolDataPack implements IProtocolComData {

    protected byte head = ProtocolCode.STX;

    byte length_h;
    byte length_l;

    protected int token;

    protected byte version;
    protected byte seq;

    protected byte custom;
    protected byte product;
    protected byte type;
    protected byte cmd;

    protected byte[] params;

    byte crc;

    protected byte tail = ProtocolCode.ETX;

    public byte getHead() {
        return this.head;
    }

    protected void setLength(int effectiveLength) {
        this.length_h = (byte) ((effectiveLength >> 8) & 0xFF);
        this.length_l = (byte) (effectiveLength & 0xFF);
    }

    public byte getLengthH() {
        return this.length_h;
    }

    public byte getLengthL() {
        return this.length_l;
    }

    public int getLength() {
        return (length_h & 0xFF) << 8 | (length_l & 0xFF);
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getToken() {
        return token;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version & 0xFF;
    }

    public void setSeq(byte seq) {
        this.seq = seq;
    }

    public int getSeq() {
        return this.seq & 0xFF;
    }

    public void setCustom(byte custom) {
        this.custom = custom;
    }

    public byte getCustom() {
        return this.custom;
    }

    public void setProduct(byte product) {
        this.product = product;
    }

    public byte getProduct() {
        return this.product;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getCmd() {
        return this.cmd;
    }

    public void setParams(byte[] params) {
        this.params = params;
    }

    public byte[] getParams() {
        return this.params;
    }

    public byte getCrc() {
        return crc;
    }

    public byte getTail() {
        return this.tail;
    }

    /**
     * 组包
     */
    public abstract byte[] organizeProtocolData();


}
