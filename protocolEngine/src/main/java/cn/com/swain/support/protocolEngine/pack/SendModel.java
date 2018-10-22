package cn.com.swain.support.protocolEngine.pack;

import cn.com.swain.baselib.util.Bit;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/14 0014
 * desc : 发送类型的数据结构
 */
public class SendModel {

    private final Bit mBit;

    public SendModel() {
        mBit = new Bit(SEND_MODEL_CASUAL);
    }


    /**
     * 11111111
     * 00000000 casual
     * 00000001 onlyLan
     * 00000010 onlyWan
     * 00000011 lanWan
     */

    private static final int SEND_MODEL_CASUAL = 0x00;
    private static final int SEND_MODEL_LAN = 0x01;
    private static final int SEND_MODEL_WAN = 0x02;

    /**
     * 设置随意通信
     */
    public void setSendModelCasual() {
        mBit.fillEmpty();
    }

    // 发送模式随意
    public boolean isSendModelCasual() {
        return this.mBit.getDevice() == SEND_MODEL_CASUAL;
    }


    /**
     * 设置局域网通信
     */
    public void setSendModelIsLan() {

        mBit.add(0);

    }

    /**
     * 只用局域网通信
     */
    public boolean isSendModelOnlyLan() {
        return this.mBit.getDevice() == SEND_MODEL_LAN;
    }

    public boolean isSendModelIsLan() {
        return ((this.mBit.getDevice() & SEND_MODEL_LAN) == SEND_MODEL_LAN);
    }

    /**
     * 设置广域网通信
     */
    public void setSendModelIsWan() {

        mBit.add(1);

    }

    /**
     * 只用广域网通信
     */
    public boolean isSendModelOnlyWan() {
        return this.mBit.getDevice() == SEND_MODEL_WAN;
    }

    public boolean isSendModelIsWan() {
        return ((this.mBit.getDevice() & SEND_MODEL_WAN) == SEND_MODEL_WAN);
    }

    ///

    public void setSendModel(int bitPoint) {
        mBit.add(bitPoint);
    }

    public int getSendModel() {
        return mBit.getDevice();
    }


    @Override
    public String toString() {
        return Integer.toBinaryString(this.mBit.getDevice());
    }
}
