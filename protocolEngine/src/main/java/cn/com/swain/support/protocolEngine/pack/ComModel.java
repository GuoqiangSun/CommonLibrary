package cn.com.swain.support.protocolEngine.pack;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/13 0013
 * Desc:
 */
public class ComModel extends BaseModel {

    public ComModel() {
        super();
    }

    public ComModel(BaseModel model) {
        super(model);
    }

    public ComModel(int model) {
        super(model);
    }

    /**
     * 11111111
     * 00000000 casual
     * 00000001 onlyLan
     * 00000010 onlyWan
     * 00000100 onlyBle
     */

    public static final int SEND_MODEL_LAN = 0x01;
    public static final int BIT_POINT_LAN = 0;

    public static final int SEND_MODEL_WAN = 0x02;
    public static final int BIT_POINT_WAN = 1;

    public static final int SEND_MODEL_BLE = 0x04;
    public static final int BIT_POINT_BLE = 2;

    /**
     * 设置只局域网通信模式
     */
    public void setModelOnlyLan() {
        fillEmpty();
        setModel(BIT_POINT_LAN);
    }

    /**
     * 设置局域网通信模式
     */
    public void setModelIsLan() {
        setModel(BIT_POINT_LAN);
    }

    /**
     * 移除局域网通信模式
     */
    public void removeLanModel() {
        removeModel(BIT_POINT_LAN);
    }

    /**
     * 只用局域网通信模式
     */
    public boolean isModelOnlyLan() {
        return getModel() == SEND_MODEL_LAN;
    }

    /**
     * 是否局域网通信模式
     */
    public boolean isModelIsLan() {
        return ((getModel() & SEND_MODEL_LAN) == SEND_MODEL_LAN);
    }

    /**
     * 设置广域网通信模式
     */
    public void setModelIsWan() {
        setModel(BIT_POINT_WAN);
    }

    /**
     * 设置只广域网通信模式
     */
    public void setModelOnlyWan() {
        fillEmpty();
        setModel(BIT_POINT_WAN);
    }

    /**
     * 移除广域网通信模式
     */
    public void removeModelIsWan() {
        removeModel(BIT_POINT_WAN);
    }

    /**
     * 只用广域网通信模式
     */
    public boolean isModelOnlyWan() {
        return getModel() == SEND_MODEL_WAN;
    }

    /**
     * 是否广域网通信模式
     */
    public boolean isModelIsWan() {
        return ((getModel() & SEND_MODEL_WAN) == SEND_MODEL_WAN);
    }


    /**
     * 设置只Ble通信模式
     */
    public void setModelOnlyBle() {
        fillEmpty();
        setModel(BIT_POINT_BLE);
    }

    /**
     * 设置Ble通信模式
     */
    public void setModelIsBle() {
        setModel(BIT_POINT_BLE);
    }

    /**
     * 移除Ble通信模式
     */
    public void removeBleModel() {
        removeModel(BIT_POINT_BLE);
    }

    /**
     * 只用Ble通信模式
     */
    public boolean isModelOnlyBle() {
        return getModel() == SEND_MODEL_BLE;
    }

    /**
     * 是否Ble通信模式
     */
    public boolean isModelIsBle() {
        return ((getModel() & SEND_MODEL_BLE) == SEND_MODEL_BLE);
    }


}
