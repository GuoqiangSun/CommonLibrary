package cn.com.swain.support.ble.send;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.ble.connect.BleConnectEngine;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class BleSend extends AbsBleSend {

    public static final int MAX_LENGTH = 18;

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

    private int mResolveLength = MAX_LENGTH;

    @Override
    public void setResolveDataLength(int length) {
        if (length > 0 && length < MAX_LENGTH) {
            mResolveLength = length;
        }
    }

    private boolean printData = false;

    @Override
    public void setPrintData(boolean print) {
        printData = print;
    }

    @Override
    public void sendData(byte[] buf) {

        resolveData(buf, 300);

    }

    @Override
    public void sendData(byte[] data, long delay) {

        resolveData(data, delay);

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

    private void writeData(byte[] buf) {

        if (printData) {
            Tlog.d(BleConnectEngine.TAG, " BluetoothGatt writeValue : " + StrUtil.toHexString(buf));
        }

        mCharacteristic.setValue(buf);

        //设置回复形式
//        mCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

        mConGatt.writeCharacteristic(mCharacteristic);

    }


    private void resolveData(byte[] mData, long delay) {

        if (mData == null) {
            return;
        }

        final int length = mData.length;

        // Tlog.d(TAG, " resolveData length : " + length);

        if (length > mResolveLength) {

            int l = length % mResolveLength;
            int d = length / mResolveLength;
            if (l != 0) {
                d += 1;
            }
            int onePkgLength = length / d;
            int end = d-1;

            for (int i = 0; i < d; i++) {

                byte[] buf;

                if (i == end) {
                    int len = i * onePkgLength;
                    int realLen = length - len;

                    buf = new byte[realLen];
                    System.arraycopy(mData, i * onePkgLength, buf, 0, realLen);

                } else {
                    buf = new byte[onePkgLength];
                    System.arraycopy(mData, i * onePkgLength, buf, 0, onePkgLength);

                }

                writeData(buf);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {

            writeData(mData);
        }
    }

}
