package cn.com.swain.support.protocolEngine.datagram.ProtocolDatagram;

import java.io.Serializable;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/1 0001
 * desc :
 */
public abstract class AbsProtocolDataPack implements IProtocolComData , Serializable {

    private byte absProtocol_head;

    private int absProtocol_length;
    private byte absProtocol_length_h;
    private byte absProtocol_length_l;

    private byte absProtocol_version;
    private byte absProtocol_seq;

    private int absProtocol_token;

    private byte absProtocol_custom;
    private byte absProtocol_product;
    private byte absProtocol_type;
    private byte absProtocol_cmd;

    private byte[] absProtocol_params;

    private byte absProtocol_crc;

    private byte absProtocol_tail;

    public void setHead(byte head) {
        this.absProtocol_head = head;
    }

    public byte getHead() {
        return this.absProtocol_head;
    }

    public void setLengthH(byte lengthH) {
        this.absProtocol_length_h = lengthH;
    }

    public void setLengthL(byte lengthL) {
        this.absProtocol_length_l = lengthL;
    }

    public byte getLengthH() {
        return this.absProtocol_length_h;
    }

    public byte getLengthL() {
        return this.absProtocol_length_l;
    }

    public void setLength(int effectiveLength) {
        this.absProtocol_length = effectiveLength;
    }

    public int getLength() {
        return this.absProtocol_length;
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

    public byte getVersion() {
        return this.absProtocol_version;
    }

    public void setSeq(byte seq) {
        this.absProtocol_seq = seq;
    }

    public byte getSeq() {
        return this.absProtocol_seq;
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

    public void setCrc(byte crc) {
        this.absProtocol_crc = crc;
    }

    public byte getCrc() {
        return absProtocol_crc;
    }

    public void setTail(byte tail) {
        this.absProtocol_tail = tail;
    }

    public byte getTail() {
        return this.absProtocol_tail;
    }

    /**
     * 组包
     */
    public abstract byte[] organizeProtocolData();

}
