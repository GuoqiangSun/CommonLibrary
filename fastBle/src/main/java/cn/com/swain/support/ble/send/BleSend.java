package cn.com.swain.support.ble.send;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import cn.com.swain.support.ble.connect.BleConnectEngine;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class BleSend extends AbsBleSend {

    private final BluetoothGattCharacteristic mCharacteristic;
    private final BluetoothGatt mConGatt;
    private final String mac;
    private final String uuid;

    public BleSend(BluetoothGatt mConGatt, BluetoothGattCharacteristic mCharacteristic) {
        this.mConGatt = mConGatt;
        this.mCharacteristic = mCharacteristic;
        this.mac = mConGatt.getDevice().getAddress();
        this.uuid = mCharacteristic.getUuid().toString();
    }

    @Override
    public void sendData(byte[] buf) {

        resolveData(buf);

    }

    @Override
    public String getUuidStr() {
        return this.uuid;
    }



    @Override
    public String getMac() {
        return this.mac;
    }


    @Override
    public void removeMsg() {

    }

    @Override
    public void closeGatt() {
        if (mConGatt != null) {
            Tlog.e(BleConnectEngine.TAG, " BleSend.closeGatt() mConGatt.close()");
            mConGatt.disconnect();
            mConGatt.close();
        }
    }


    public static final int MAX_LENGTH = 18;


    private void writeData(byte[] buf) {

        mCharacteristic.setValue(buf);

        //设置回复形式
//        mCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

        mConGatt.writeCharacteristic(mCharacteristic);

    }


    private void resolveData(byte[] mData) {

        if (mData == null) {
            return;
        }

        final int length = mData.length;

        // Tlog.d(TAG, " resolveAdasData length : " + length);

        if (length > MAX_LENGTH) {

            int l = length % MAX_LENGTH;

            // Tlog.d(TAG, " % : " + l);

            if (l == 0) { // 刚好

                int d = length / MAX_LENGTH;
                // Tlog.d(TAG, "%==0; d : " + d);

                for (int i = 0; i < d; i++) {

                    byte[] buf = new byte[MAX_LENGTH];
                    System.arraycopy(mData, i * MAX_LENGTH, buf, 0, MAX_LENGTH);

                    writeData(buf);

                }

            } else {
                int end = length / MAX_LENGTH;
                int d = end + 1;


                // Tlog.d(TAG, "%!=0; end : " + end + " d : " + d);

                for (int i = 0; i < d; i++) {

                    if (i == end) {

                        int len = i * MAX_LENGTH;
                        int realLen = length - len;

                        byte[] buf = new byte[realLen];
                        System.arraycopy(mData, i * MAX_LENGTH, buf, 0, realLen);

                        writeData(buf);

                    } else {

                        byte[] buf = new byte[MAX_LENGTH];
                        System.arraycopy(mData, i * MAX_LENGTH, buf, 0, MAX_LENGTH);

                        writeData(buf);
                    }

                }

            }

        } else {

            writeData(mData);
        }
    }

}
