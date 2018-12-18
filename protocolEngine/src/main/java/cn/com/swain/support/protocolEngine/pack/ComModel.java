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
     * <p>
     * 00000001 onlyLan
     * 00000010 onlyWan
     * 00000100 onlyBle
     */

    public static final int MODEL_CASUAL = EMPTY;

    public static final int MODEL_LAN = 0x01;
    public static final int BIT_POINT_LAN = 0;

    public static final int MODEL_WAN = 0x02;
    public static final int BIT_POINT_WAN = 1;

    public static final int MODEL_BLE = 0x04;
    public static final int BIT_POINT_BLE = 2;

    /**
     * 设置随意模式
     */
    public ComModel setModelCasual() {
        fillEmpty();
        return this;
    }

    /**
     * 是否随意通信模式
     */
    public boolean isCasualModel() {
        return getModel() == MODEL_CASUAL;
    }

    /**
     * 设置局域网通信模式
     */
    public ComModel setModelIsLan() {
        setModel(BIT_POINT_LAN);
        return this;
    }

    /**
     * 设置只局域网通信模式
     */
    public ComModel setModelOnlyLan() {
        fillEmpty();
        return setModelIsLan();
    }


    /**
     * 移除局域网通信模式
     */
    public ComModel removeLanModel() {
        removeModel(BIT_POINT_LAN);
        return this;
    }

    /**
     * 只用局域网通信模式
     */
    public boolean isOnlyLanModel() {
        return getModel() == MODEL_LAN;
    }

    /**
     * 是否局域网通信模式
     */
    public boolean hasLanModel() {
        return ((getModel() & MODEL_LAN) == MODEL_LAN);
    }

    /**
     * 设置广域网通信模式
     */
    public ComModel setModelIsWan() {
        setModel(BIT_POINT_WAN);
        return this;
    }

    /**
     * 设置只广域网通信模式
     */
    public ComModel setModelOnlyWan() {
        fillEmpty();
        return setModelIsWan();
    }

    /**
     * 移除广域网通信模式
     */
    public ComModel removeModelIsWan() {
        removeModel(BIT_POINT_WAN);
        return this;
    }

    /**
     * 只用广域网通信模式
     */
    public boolean isOnlyWanModel() {
        return getModel() == MODEL_WAN;
    }

    /**
     * 是否广域网通信模式
     */
    public boolean hasWanModel() {
        return ((getModel() & MODEL_WAN) == MODEL_WAN);
    }


    /**
     * 设置Ble通信模式
     */
    public ComModel setModelIsBle() {
        setModel(BIT_POINT_BLE);
        return this;
    }

    /**
     * 设置只Ble通信模式
     */
    public ComModel setModelOnlyBle() {
        fillEmpty();
        return setModelIsBle();
    }

    /**
     * 移除Ble通信模式
     */
    public ComModel removeBleModel() {
        removeModel(BIT_POINT_BLE);
        return this;
    }

    /**
     * 只用Ble通信模式
     */
    public boolean isOnlyBleModel() {
        return getModel() == MODEL_BLE;
    }

    /**
     * 是否Ble通信模式
     */
    public boolean hasBleModel() {
        return ((getModel() & MODEL_BLE) == MODEL_BLE);
    }


}
