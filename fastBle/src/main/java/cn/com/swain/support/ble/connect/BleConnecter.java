package cn.com.swain.support.ble.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/3 0003
 * desc :
 */

class BleConnecter implements Serializable {

    private String TAG = BleConnectEngine.TAG;

    private final BluetoothAdapter mAdapter;
    private final ScanBle mItem;

    BleConnecter(BluetoothAdapter mAdapter, ScanBle mItem) {
        this.mAdapter = mAdapter;
        this.mItem = mItem;
    }

    public ScanBle getScanBle() {
        return mItem;
    }


    private volatile BluetoothGatt mConGatt;

    /**********************/

    final boolean isValid() {
        return (mItem != null) && mItem.isValid();
    }

    final BluetoothGatt getConGatt() {
        return mConGatt;
    }

    final synchronized void setConBluetoothGatt(BluetoothGatt mConGatt) {
        this.mConGatt = mConGatt;
    }

    private volatile int mBleConState;

    synchronized void setBleConState(int bleConState) {
        this.mBleConState = bleConState;
        if (BluetoothProfile.STATE_DISCONNECTED == mBleConState
                || BluetoothProfile.STATE_DISCONNECTING == mBleConState) {
            discon = true;
            this.mItem.setDisconnected();

        } else if (BluetoothProfile.STATE_CONNECTED == mBleConState
                || BluetoothProfile.STATE_CONNECTING == mBleConState) {
            discon = false;
            this.mItem.setConnected();

        }
    }

    /**
     * true 连接 false 未连接
     *
     * @return
     */
    private synchronized boolean checkState() {
        return BluetoothProfile.STATE_DISCONNECTED != mBleConState
                && BluetoothProfile.STATE_DISCONNECTING != mBleConState
                && mConGatt != null;
    }

    synchronized void connectBle(Context mCtx, BluetoothGattCallback mGattCallBack) {

        if (mItem.address == null) {
            Tlog.e(TAG, " connectBle() , address==null ");
            return;
        }

        Tlog.e(TAG, " connectBle() , address: " + mItem.address);

        // 连接ble
        BluetoothDevice remoteDevice = mAdapter.getRemoteDevice(mItem.address);
        remoteDevice.connectGatt(mCtx, false,
                mGattCallBack);
    }

    private boolean discon = false;

    synchronized void passiveDisconnectBle() {
        Tlog.e(TAG, " passiveDisconnectBle() close mConGatt ...");

        discon = true;
        closeGatt();

        if (this.mItem != null) {
            this.mItem.setDisconnected();
        }

        hasDiscoverService = false;
    }

    synchronized void activeDisconnectBle() {
        ScanBle scanBle = getScanBle();
        String address = scanBle != null ? scanBle.address : "null";
        Tlog.e(TAG, " activeDisconnectBle() " + address);

        discon = true;
        closeGatt();

        if (this.mItem != null) {
            this.mItem.setDisconnected();
        }

        hasDiscoverService = false;
    }

    private synchronized void closeGatt() {
        if (mConGatt != null) {
            int i = 0;
            do {
                Tlog.e(TAG, " BleConnecter BluetoothGatt.close() " + i);
                try {
                    mConGatt.disconnect();
                    mConGatt.close();
                } catch (Exception e) {
                    Tlog.e(TAG, " closeGatt ", e);
                }
            } while (++i < 3);
            mConGatt = null;
        } else {
            Tlog.e(TAG, " activeDisconnectBle(), but mConGatt==null ");
        }
    }

    void readRssi() {
        if (mConGatt != null) {
            mConGatt.readRemoteRssi();
        }
    }

    private boolean hasDiscoverService;

    synchronized void discoverService() {
        if (checkState() && !discon) {
            if (mConGatt != null) {
                if (!hasDiscoverService) {
                    Tlog.e(TAG, " discoverService ");
                    hasDiscoverService = mConGatt.discoverServices();
                } else {
                    Tlog.w(TAG, " discoverServices(),  hasDiscoverService ");
                }
            } else {
                Tlog.w(TAG, " discoverServices(),  mConGatt==null ");
            }
        } else {
            Tlog.w(TAG, " discoverService(),  mConGatt already disconnect; discon:"+discon);
        }
    }

    synchronized boolean orderService() {

        if (mConGatt == null) {
            Tlog.e(TAG, " orderService(), mConGatt = null ");
            return false;
        }

        BluetoothGatt mGatt = mConGatt;
        boolean result = true;

        //得到所有Service
        List<BluetoothGattService> supportedGattServices = mGatt.getServices();


        if (supportedGattServices != null && supportedGattServices.size() > 0) {

            for (BluetoothGattService gattService : supportedGattServices) {

                //得到每个Service的Characteristics
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

                if (gattCharacteristics != null && gattCharacteristics.size() > 0) {

                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {


                        UUID uuid = gattCharacteristic.getUuid();
                        //所有Characteristics按属性分类
                        int charaProp = gattCharacteristic.getProperties();
                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            Tlog.d(TAG, uuid + " PROPERTY_READ ");
                        }

//                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
//                                || (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
//                            Log.d(TAG, "gattCharacteristic的属性为:  可写");
//                        }

                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            Tlog.d(TAG, uuid + " PROPERTY_WRITE ");
                        }

                        // 可通知，可指示
//                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
//                                || (charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
//                            Log.d(TAG, "gattCharacteristic的属性为:  具备通知属性");
//
//                        }

                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            Tlog.d(TAG, uuid + " PROPERTY_NOTIFY ");
                            result = writeDescriptor(mGatt, gattCharacteristic);

                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean writeDescriptor(BluetoothGatt mGatt, BluetoothGattCharacteristic gattCharacteristic) {

        if (discon) {
            Tlog.e(TAG, "when setCharacteristicNotification ,gatt discon Already");
            return false;
        }

        if (!mGatt.setCharacteristicNotification(gattCharacteristic, true)) {
            Tlog.e(TAG, "setCharacteristicNotification result fail ");
            return false;
        }

        List<BluetoothGattDescriptor> descriptorList = gattCharacteristic.getDescriptors();

        if (descriptorList != null && descriptorList.size() > 0) {

            boolean writeResult = false;

            for (BluetoothGattDescriptor descriptor : descriptorList) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (discon) {
                    Tlog.e(TAG, "writeDescriptor , gatt discon Already");
                    return false;
                }

                if (!(writeResult |= mGatt.writeDescriptor(descriptor))) {
                    Tlog.e(TAG, "writeDescriptor result fail ");
                }

            }

            return writeResult;
        }

        return false;
    }

}
