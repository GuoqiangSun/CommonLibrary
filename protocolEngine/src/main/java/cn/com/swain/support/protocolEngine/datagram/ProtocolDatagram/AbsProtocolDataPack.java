package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public abstract class AbsProtocolDataPack implements IProtocolComData {

    protected byte absProtocol_head;

    byte absProtocol_length_h;
    byte absProtocol_length_l;

    protected int absProtocol_token;

    protected byte absProtocol_version;
    protected byte absProtocol_seq;

    protected byte absProtocol_custom;
    protected byte absProtocol_product;
    protected byte absProtocol_type;
    protected byte absProtocol_cmd;

    protected byte[] absProtocol_params;

    byte absProtocol_crc;

    protected byte absProtocol_tail;

    public byte getHead() {
        return this.absProtocol_head;
    }

    protected void setLength(int effectiveLength) {
        this.absProtocol_length_h = (byte) ((effectiveLength >> 8) & 0xFF);
        this.absProtocol_length_l = (byte) (effectiveLength & 0xFF);
    }

    public byte getLengthH() {
        return this.absProtocol_length_h;
    }

    public byte getLengthL() {
        return this.absProtocol_length_l;
    }

    public int getLength() {
        return (absProtocol_length_h & 0xFF) << 8 | (absProtocol_length_l & 0xFF);
    }

    public void setToken(int token) {
        this.absProtocol_token = token;
    }

    public int getToken() {
        return absProtocol_token;
    }

    public void setVersion(byte version) {
        this.absProtocol_version = version;
    }

    public int getVersion() {
        return this.absProtocol_version & 0xFF;
    }

    public void setSeq(byte seq) {
        this.absProtocol_seq = seq;
    }

    public int getSeq() {
        return this.absProtocol_seq & 0xFF;
    }

    public void setCustom(byte custom) {
        this.absProtocol_custom = custom;
    }

    public byte getCustom() {
        return this.absProtocol_custom;
    }

    public void setProduct(byte product) {
        this.absProtocol_product = product;
    }

    public byte getProduct() {
        return this.absProtocol_product;
    }

    public void setType(byte type) {
        this.absProtocol_type = type;
    }

    public byte getType() {
        return this.absProtocol_type;
    }

    public void setCmd(byte cmd) {
        this.absProtocol_cmd = cmd;
    }

    public byte getCmd() {
        return this.absProtocol_cmd;
    }

    public void setParams(byte[] params) {
        this.absProtocol_params = params;
    }

    public byte[] getParams() {
        return this.absProtocol_params;
    }

    public byte getCrc() {
        return absProtocol_crc;
    }

    public byte getTail() {
        return this.absProtocol_tail;
    }

    /**
     * 组包
     */
    public abstract byte[] organizeProtocolData();

}
