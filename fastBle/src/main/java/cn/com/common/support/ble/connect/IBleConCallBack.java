package cn.com.common.support.ble.connect;

import android.bluetooth.BluetoothGatt;

import cn.com.common.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/3 0003
 * desc :
 */

public interface IBleConCallBack {

    /**
     * 蓝牙连接
     *
     * @param result true连接成功
     * @param mItem
     */
    void onResultConnect(boolean result, ScanBle mItem);

    /**
     * 蓝牙已经连接
     *
     * @param mItem
     */
    void onResultAlreadyConnected(ScanBle mItem);

    /**
     * 蓝牙断开（被动）
     *
     * @param mItem
     */
    void onResultDisconnectPassively(boolean result, ScanBle mItem);

    /**
     * 蓝牙断开（主动）
     *
     * @param mItem
     */
    void onResultDisconnectActively(ScanBle mItem);

    /**
     * 服务订阅成功
     *
     * @param mConGatt
     */
    void onResultServiceOrder(boolean result, ScanBle mItem, BluetoothGatt mConGatt);

    /**
     * @param mac     mac
     * @param uuidStr 通知的uuid
     * @param data    数据
     */
    void onPeripheralNotify(String mac, String uuidStr, byte[] data);

    /**
     * @param mItem
     */
    void onWriteDataFail(ScanBle mItem);


}
