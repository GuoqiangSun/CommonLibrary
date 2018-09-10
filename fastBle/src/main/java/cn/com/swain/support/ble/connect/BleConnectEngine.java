package cn.com.swain.support.ble.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public class BleConnectEngine extends AbsBleConnect {

    public static final String TAG = "BleCon";

    private MGattCallBack mGattCallBack = new MGattCallBack();

    private BleConHandler mBleConHandler;
    private IBleConCallBack mBleConCallBack;

    private Context mCtx;
    private BluetoothAdapter mBluetoothAdapter;

    public BleConnectEngine(Context mCtx, Looper mWorkLooper, IBleConCallBack mBleConCallBack) {

        this.mCtx = mCtx;

        BluetoothManager mBluetoothManager = (BluetoothManager) mCtx.getSystemService(Context.BLUETOOTH_SERVICE);
        assert mBluetoothManager != null;
        this.mBluetoothAdapter = mBluetoothManager.getAdapter();

        this.mBleConCallBack = mBleConCallBack;
        this.mBleConHandler = new BleConHandler(this, mWorkLooper);
    }

    // 连接的任务
    private List<BleConnecter> mConTaskLst = Collections.synchronizedList(new ArrayList<BleConnecter>(12));

    //连接成功的集合
    private List<String> mConSuccessLst = Collections.synchronizedList(new ArrayList<String>(12));

    // 手动断开的集合
    private List<String> mDisConActivelyLst = Collections.synchronizedList(new ArrayList<String>(12));

    private synchronized boolean getActivelyDisconAddress(String address) {


        if (address == null) {
            return false;
        }

        if (mDisConActivelyLst.size() <= 0) {
            return false;
        }

        for (String disconAddress : mDisConActivelyLst) {
            if (address.equalsIgnoreCase(disconAddress)) {
                return true;
            }
        }

        return false;
    }

    private synchronized boolean getConSuccessAddress(String address) {
        if (address == null) {
            return false;
        }
        if (mConSuccessLst.size() > 0) {
            for (String mac : mConSuccessLst) {
                if (address.equalsIgnoreCase(mac)) {
                    return true;
                }
            }
        }

        return false;
    }

    private synchronized void removeBleConnecter(ScanBle scanBle) {

        if (scanBle == null) {
            return;
        }

        BleConnecter tmpBleConnecter = getBleConnecter(scanBle.address);

        if (tmpBleConnecter != null) {
            mConTaskLst.remove(tmpBleConnecter);
        }
    }

    private synchronized BleConnecter getBleConnecter(String address) {

        if (address == null) {
            return null;
        }

        BleConnecter mTmpBleConnecter = null;

        if (mConTaskLst.size() > 0) {

            for (BleConnecter mBleConnecter : mConTaskLst) {
                if (mBleConnecter.getScanBle().address.equalsIgnoreCase(address)) {
                    mTmpBleConnecter = mBleConnecter;
                    break;
                }
            }
        }

        return mTmpBleConnecter;
    }

    @Override
    public void connect(ScanBle mScanBle) {
        final BleConnecter mBleConnecter = new BleConnecter(mBluetoothAdapter, mScanBle);
        mBleConHandler.obtainMessage(MSG_WHAT_CONNECT_BLE, mBleConnecter).sendToTarget();
    }

    @Override
    public void disconnect(ScanBle mScanBle) {
        BleConnecter mBleConnecter = getBleConnecter(mScanBle.address);
        if (mBleConnecter == null) {
            Tlog.e(TAG, " disconnect() " + mScanBle.address + "  BleConnecter==null ...");
            mBleConnecter = new BleConnecter(mBluetoothAdapter, mScanBle);
        }
        mBleConHandler.obtainMessage(MSG_WHAT_DISCONNECT_BLE, mBleConnecter).sendToTarget();
    }

    @Override
    public void checkConnectResult(boolean removeLast, BleConnectResult mResult) {

        if (removeLast) {
            removeCheckConnectResult();
        }

        Message message = mBleConHandler.obtainMessage(MSG_WHAT_CHECK_CONNECT_RESULT, mResult);
        mBleConHandler.sendMessageDelayed(message, mResult.delay);
    }

    @Override
    public void removeCheckConnectResult() {
        if (mBleConHandler.hasMessages(MSG_WHAT_CHECK_CONNECT_RESULT)) {
            mBleConHandler.removeMessages(MSG_WHAT_CHECK_CONNECT_RESULT);
        }
    }

    @Override
    public void release() {

        if (mConTaskLst.size() > 0) {
            for (BleConnecter mBleConnecter : mConTaskLst) {
                if (mBleConnecter != null) {
                    mBleConnecter.activeDisconnectBle();
                }
            }
        }
        mConTaskLst.clear();
        mConSuccessLst.clear();
        mDisConActivelyLst.clear();
    }

    /**
     * 连接设备
     */
    private static final int MSG_WHAT_CONNECT_BLE = 0x00;
    /**
     * 断开连接
     */
    private static final int MSG_WHAT_DISCONNECT_BLE = 0x01;


    /**
     * 连接成功
     */
    private static final int MSG_WHAT_CONNECT_SUCCESS = 0x02;
    /**
     * 断开连接成功
     */
    private static final int MSG_WHAT_DISCONNECT_SUCCESS = 0x03;

    /**
     * 连接失败
     */
    private static final int MSG_WHAT_CONNECT_FAIL = 0x04;
    /**
     * 断开连接失败
     */
    private static final int MSG_WHAT_DISCONNECT_FAIL = 0x05;


    /**
     * 扫描设备服务
     */
    private static final int MSG_WHAT_DISCOVER_SERVICE = 0x06;
    /**
     * 扫描到设备服务
     */
    private static final int MSG_WHAT_SERVICE_DISCOVERIES = 0x07;


    /**
     * 订阅服务
     */
    private static final int MSG_WHAT_ORDER_SERVICE = 0x08;
    /**
     * 订阅服务成功
     */
    private static final int MSG_WHAT_SERVICE_ORDER_SUCCESS = 0x09;

    /**
     * 订阅服务失败
     */
    private static final int MSG_WHAT_SERVICE_ORDER_FAIL = 0x0A;


    /**
     * 扫描设备信号强度
     */
    private static final int MSG_WHAT_OBTAIN_RSSI = 0x0B;

    /**
     * 数据写失败
     */
    private static final int MSG_WHAT_WRITE_FAIL = 0x0C;

    /**
     * 检测连接状态
     */
    private static final int MSG_WHAT_CHECK_CONNECT_RESULT = 0x0D;


    private void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_CONNECT_BLE:

                // 连接ble

                BleConnecter mBleConnecter0 = ((BleConnecter) msg.obj);

                if (mBleConnecter0.isValid()) {
                    mDisConActivelyLst.remove(mBleConnecter0.getScanBle().address);
                    mConTaskLst.add(mBleConnecter0);
                    mBleConnecter0.connectBle(mCtx, mGattCallBack);
                } else {
                    Tlog.e(TAG, " connected fail .mBleConnecter0.isInvalid()");
                    if (mBleConCallBack != null) {
                        mBleConCallBack.onResultConnect(false, mBleConnecter0.getScanBle());
                    } else {
                        Tlog.e(TAG, " <BleConnectEngine> connectSuccess() mConnectedCallBack == null ");
                    }
                }


                break;

            case MSG_WHAT_DISCONNECT_BLE:

                // 断开ble
                BleConnecter mBleConnecter1 = (BleConnecter) msg.obj;
                mBleConnecter1.activeDisconnectBle();

                ScanBle scanBle = mBleConnecter1.getScanBle();
                removeBleConnecter(scanBle);
                mConSuccessLst.remove(scanBle.address);
                mDisConActivelyLst.add(scanBle.address);

                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultDisconnectActively(scanBle);
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> connectSuccess() mConnectedCallBack == null ");
                }

                break;

            /*************/
            case MSG_WHAT_CONNECT_SUCCESS:
                // 连接成功

                BleConnecter mBleConnecter2 = (BleConnecter) msg.obj;
                ScanBle scanBle2 = mBleConnecter2.getScanBle();
                Tlog.e(TAG, scanBle2.address + " connected success");

                if (getConSuccessAddress(scanBle2.address)) {
                    Tlog.e(TAG, " this mac has already connected [break] ;");
                    if (mBleConCallBack != null) {
                        mBleConCallBack.onResultAlreadyConnected(scanBle2);
                    }
                    return;
                }

                mConSuccessLst.add(scanBle2.address);

                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultConnect(true, scanBle2);
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> connectSuccess() mConnectedCallBack == null ");
                }

                Message message = mBleConHandler.obtainMessage(MSG_WHAT_DISCOVER_SERVICE, mBleConnecter2);
                mBleConHandler.sendMessageDelayed(message, 500);

                break;
            case MSG_WHAT_DISCONNECT_SUCCESS:
                // 断开连接成功

                BleConnecter mBleConnecter3 = (BleConnecter) msg.obj;
                mBleConnecter3.passiveDisconnectBle();
                ScanBle scanBle3 = mBleConnecter3.getScanBle();
                mConTaskLst.remove(mBleConnecter3);
                mConSuccessLst.remove(scanBle3.address);

                Tlog.e(TAG, scanBle3.address + " disconnected success ");
                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultDisconnectPassively(true, mBleConnecter3.getScanBle());
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> disconnectSuccess() mConnectedCallBack == null ");
                }

                break;

            case MSG_WHAT_CONNECT_FAIL:

                BleConnecter BleConnecter4 = ((BleConnecter) msg.obj);
                BleConnecter4.passiveDisconnectBle();
                ScanBle scanBle4 = BleConnecter4.getScanBle();
                mConTaskLst.remove(BleConnecter4);
                mConSuccessLst.remove(scanBle4.address);

                Tlog.e(TAG, scanBle4.address + " connected fail ");
                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultConnect(false, scanBle4);
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> connectSuccess() mConnectedCallBack == null ");
                }

                break;

            case MSG_WHAT_DISCONNECT_FAIL:

                BleConnecter BleConnecter5 = ((BleConnecter) msg.obj);
                BleConnecter5.passiveDisconnectBle();
                ScanBle scanBle5 = BleConnecter5.getScanBle();
                mConTaskLst.remove(BleConnecter5);
                mConSuccessLst.remove(scanBle5.address);

                Tlog.e(TAG, scanBle5.address + " disconnected fail ");
                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultDisconnectPassively(false, scanBle5);
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> disconnectSuccess() mConnectedCallBack == null ");
                }

                break;

            /*************/
            case MSG_WHAT_DISCOVER_SERVICE:
                //扫描服务

                try {

                    ((BleConnecter) msg.obj).discoverService();

                } catch (Exception e) {

                    Tlog.e(TAG, "discoverService:" + e);

                    BleConnecter mBleConnecter6 = (BleConnecter) msg.obj;
                    Message message6 = mBleConHandler.obtainMessage(MSG_WHAT_SERVICE_ORDER_FAIL, mBleConnecter6);
                    mBleConHandler.sendMessageDelayed(message6, 100);
                }

                break;

            case MSG_WHAT_SERVICE_DISCOVERIES:
                // 扫描到服务

                Message message5 = mBleConHandler.obtainMessage(MSG_WHAT_ORDER_SERVICE, msg.obj);
                mBleConHandler.sendMessageDelayed(message5, 500);

                break;
            case MSG_WHAT_ORDER_SERVICE:
                // 订阅服务
                BleConnecter mBleConnecter6 = (BleConnecter) msg.obj;
                boolean result = mBleConnecter6.orderService();
                Message message6 = mBleConHandler.obtainMessage(result ? MSG_WHAT_SERVICE_ORDER_SUCCESS : MSG_WHAT_SERVICE_ORDER_FAIL, mBleConnecter6);
                mBleConHandler.sendMessageDelayed(message6, 1000);
                break;
            case MSG_WHAT_SERVICE_ORDER_SUCCESS:

                Tlog.v(TAG, " <BleConnectEngine> order service success ");
                BleConnecter mBleConnecter8 = ((BleConnecter) msg.obj);
                mBleConnecter8.readRssi();
                if (mBleConCallBack != null) {
                    mBleConCallBack.onResultServiceOrder(true, mBleConnecter8.getScanBle(), mBleConnecter8.getConGatt());
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> serviceOrderSuccess() mConnectedCallBack == null ");
                }

                break;

            case MSG_WHAT_SERVICE_ORDER_FAIL:

                Tlog.e(TAG, " <BleConnectEngine> order service fail ");
                if (mBleConCallBack != null) {
                    BleConnecter mBleConnecter9 = ((BleConnecter) msg.obj);
                    mBleConCallBack.onResultServiceOrder(false, mBleConnecter9.getScanBle(), mBleConnecter9.getConGatt());
                } else {
                    Tlog.e(TAG, " <BleConnectEngine> serviceOrderSuccess() mConnectedCallBack == null ");
                }

                break;

            case MSG_WHAT_OBTAIN_RSSI:
                // 获取设备rssi
                Tlog.v(TAG, " rssi : " + msg.arg1);

                break;

            case MSG_WHAT_WRITE_FAIL:

                ScanBle mScanBle = (ScanBle) msg.obj;
                mBleConCallBack.onWriteDataFail(mScanBle);

                break;
            case MSG_WHAT_CHECK_CONNECT_RESULT:

                BleConnectResult mResult = (BleConnectResult) msg.obj;
                if (mResult.mCallBack != null) {
                    mResult.mCallBack.OnBleConnectResult(mResult);
                }

                break;

        }
    }

    /**************************/

    private class MGattCallBack extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            final String address = gatt.getDevice().getAddress();
            final String name = gatt.getDevice().getName();

            String resultStr = status + ((status == BluetoothGatt.GATT_SUCCESS) ? "-SUCCESS" : "-FAIL");
            String stateStr = newState + ((newState == BluetoothProfile.STATE_CONNECTED) ? "-CON" : (newState == BluetoothProfile.STATE_DISCONNECTED) ? "-DISCON" : String.valueOf(newState));
            Tlog.v(TAG, " onConnectionStateChange status: " + resultStr + ";newState:" + stateStr + "; name:" + name + ";address:" + address);

            if (name == null) {
                Tlog.e(TAG, " onConnectionStateChange()  name==null ; disconnect gatt");
                gatt.disconnect();
                gatt.close();
                return;
            }

            BleConnecter mBleConnecter = getBleConnecter(address);

            if (mBleConnecter == null) {

                boolean hasDiscon = getActivelyDisconAddress(address);

                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED && !hasDiscon) {
                    Tlog.e(TAG, " onConnectionStateChange()  BleConnecter==null ; add BleConnecter");

                    final ScanBle mBle = new ScanBle();
                    mBle.name = name;
                    mBle.address = address;
                    mBleConnecter = new BleConnecter(mBluetoothAdapter, mBle);
                    mConTaskLst.add(mBleConnecter);

                } else {
                    // 连接的gatt已经被移除。直接断开。
                    Tlog.e(TAG, " onConnectionStateChange()  " +
                            (hasDiscon ? "already cancel connect " : "BleConnecter==null") + "; disconnect gatt");
                    gatt.disconnect();
                    gatt.close();
                    return;
                }

            }

            mBleConnecter.setBleConState(newState);
            mBleConnecter.setConBluetoothGatt(gatt);

            if (newState == BluetoothProfile.STATE_CONNECTED) {

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // onConnect
                    mBleConHandler.obtainMessage(MSG_WHAT_CONNECT_SUCCESS, mBleConnecter).sendToTarget();

                } else {
                    mBleConHandler.obtainMessage(MSG_WHAT_CONNECT_FAIL, mBleConnecter).sendToTarget();
                }

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                // onDisconnect
                if (status == BluetoothGatt.GATT_SUCCESS || status == 133) {
                    mBleConHandler.obtainMessage(MSG_WHAT_DISCONNECT_SUCCESS, mBleConnecter).sendToTarget();
                } else {
                    mBleConHandler.obtainMessage(MSG_WHAT_DISCONNECT_FAIL, mBleConnecter).sendToTarget();
                }

            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            String address = gatt.getDevice().getAddress();
            BleConnecter mBleConnecter = getBleConnecter(address);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onServicesDiscovered success ");
                mBleConHandler.obtainMessage(MSG_WHAT_SERVICE_DISCOVERIES, mBleConnecter).sendToTarget();
            } else {
                Tlog.e(TAG, "onServicesDiscovered error :" + status);
                Message message6 = mBleConHandler.obtainMessage(MSG_WHAT_SERVICE_ORDER_FAIL, mBleConnecter);
                mBleConHandler.sendMessageDelayed(message6, 500);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onCharacteristicRead success");
            } else {
                Tlog.e(TAG, "onCharacteristicRead error:" + status);
            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onCharacteristicWrite success");
            } else {
                Tlog.e(TAG, "onCharacteristicWrite error:" + status);

                final String address = gatt.getDevice().getAddress();
                BleConnecter mBleConnecter = getBleConnecter(address);
                if (mBleConnecter != null) {
                    ScanBle scanBle = mBleConnecter.getScanBle();
                    if (scanBle != null) {
                        mBleConHandler.obtainMessage(MSG_WHAT_WRITE_FAIL, scanBle).sendToTarget();
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            String uuidStr = characteristic.getUuid().toString();
            byte[] value = characteristic.getValue();

            if (mBleConCallBack != null) {
                mBleConCallBack.onPeripheralNotify(gatt.getDevice().getAddress(), uuidStr, value);
            } else {
                Tlog.e(TAG, "onCharacteristicChanged mBleConCallBack == null ");
            }

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onDescriptorRead success ");
            } else {
                Tlog.e(TAG, "onDescriptorRead error :" + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onDescriptorWrite success ; BluetoothGattDescriptor uuid: " + descriptor.getUuid().toString());
//                Tlog.v(TAG, "onDescriptorWrite getCharacteristic'uuid: " + descriptor.getCharacteristic().getUuid().toString());
            } else {
                Tlog.e(TAG, "onDescriptorWrite error :" + status);
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onReadRemoteRssi success rssi: " + rssi);

                final String address = gatt.getDevice().getAddress();
                BleConnecter mBleConnecter = getBleConnecter(address);
                if (mBleConnecter != null) {
                    ScanBle scanBle = mBleConnecter.getScanBle();
                    if (scanBle != null) {
                        scanBle.rssi = rssi;
                    }
                }

                mBleConHandler.obtainMessage(MSG_WHAT_OBTAIN_RSSI, rssi, rssi).sendToTarget();
            } else {
                Tlog.e(TAG, "onReadRemoteRssi error :" + status);
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Tlog.v(TAG, "onMtuChanged success ");
            } else {
                Tlog.e(TAG, "onMtuChanged error :" + status);
            }
        }
    }

    private static class BleConHandler extends Handler {
        private final WeakReference<BleConnectEngine> wr;

        public BleConHandler(BleConnectEngine mBleConnecter, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<BleConnectEngine>(mBleConnecter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BleConnectEngine bleConController;
            if (wr != null && (bleConController = wr.get()) != null) {
                bleConController.handleMessage(msg);
            }
        }
    }

}
