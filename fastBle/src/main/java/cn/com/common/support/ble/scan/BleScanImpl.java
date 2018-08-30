package cn.com.common.support.ble.scan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;

import java.util.List;

import cn.com.common.baselib.app.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

class BleScanImpl {

    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private final AbsBleScan mOnLeScan;

    private String TAG = BleScanAuto.TAG;


    private final BluetoothAdapter.LeScanCallback mBleScanCallBack;
    private final ScanCallback mScannerCallBack;


    public BleScanImpl(BluetoothAdapter mBluetoothAdapter, AbsBleScan mOnLeScan) {

        Tlog.v(TAG, " Build.VERSION.SDK_INT  " + Build.VERSION.SDK_INT);

        this.mOnLeScan = mOnLeScan;
        this.mBluetoothAdapter = mBluetoothAdapter;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            this.mScannerCallBack = new BleScanCBL();
            this.mBleScanCallBack = null;
        } else {
            this.mBleScanCallBack = new BleScanImpl.BleScanCB();
            this.bluetoothLeScanner = null;
            this.mScannerCallBack = null;
        }
    }

    private boolean mBTEnable;

    void setBTState(boolean on) {
        this.mBTEnable = on;
    }

    void checkBluetoothLeScanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (this.bluetoothLeScanner == null) {
                this.bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            }
        }

    }


    boolean startScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return startLeScanner();
        }
        return startLeScan();
    }

    void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopLeScanner();
        } else {
            stopLeScan();
        }
    }

    private boolean startLeScan() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.startLeScan(mBleScanCallBack);
        }
        return false;
    }

    private void stopLeScan() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mBleScanCallBack);
        }
    }

    /*************/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean startLeScanner() {

        if (!mBTEnable) {
            return false;
        }

        if (bluetoothLeScanner == null) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }

        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.startScan(mScannerCallBack);
            return true;
        } else {
            Tlog.e(TAG, " startScan bluetoothLeScanner==null ");
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopLeScanner() {
        if (!mBTEnable) {
            return;
        }
        if (bluetoothLeScanner == null) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(mScannerCallBack);
        } else {
            Tlog.e(TAG, " stopScan bluetoothLeScanner==null ");
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class BleScanCBL extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            ScanRecord scanRecord = result.getScanRecord();
            if (scanRecord == null) {
                Tlog.e(TAG, " scanRecord==null ");
                return;
            }
            final List<ParcelUuid> serviceUuids = scanRecord.getServiceUuids();
            final int rssi = result.getRssi();
            final String address = result.getDevice().getAddress();
            final String name = result.getDevice().getName();
            mOnLeScan.leScan(name, address, rssi, serviceUuids);

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Tlog.v(TAG, " onBatchScanResults ");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Tlog.v(TAG, " onScanFailed ");
        }
    }

    private class BleScanCB implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            MyScanRecord myScanRecord = MyScanRecord.parseFromBytes(scanRecord);
            if (myScanRecord == null) {
                Tlog.e(TAG, " myScanRecord==null ");
                return;
            }
            final String name = device.getName();
            final List<ParcelUuid> serviceUuids = myScanRecord.getServiceUuids();
            final String address = device.getAddress();
            mOnLeScan.leScan(name, address, rssi, serviceUuids);

        }
    }

}
