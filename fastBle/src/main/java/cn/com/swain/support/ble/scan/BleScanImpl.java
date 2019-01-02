package cn.com.swain.support.ble.scan;

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

import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

class BleScanImpl {

    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    private boolean checkBluetoothEnabled() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private BluetoothLeScanner getBluetoothLeScanner() {

        if (mBluetoothLeScanner == null) {
            if (checkBluetoothEnabled()) {
                Tlog.w(TAG, "getBluetoothLeScanner() new BluetoothLeScanner. ");
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            } else {
                Tlog.w(TAG, "getBluetoothLeScanner() BT not enable");
            }
        }
        return mBluetoothLeScanner;
    }

    private final AbsBleScan mOnLeScan;

    private String TAG = BleScanAuto.TAG;


    private final BluetoothAdapter.LeScanCallback mBleScanCallBack;
    private final ScanCallback mScannerCallBack;


    public BleScanImpl(BluetoothAdapter mBluetoothAdapter, AbsBleScan mOnLeScan) {

        Tlog.v(TAG, "BleScanImpl Build.VERSION.SDK_INT  " + Build.VERSION.SDK_INT);

        this.mOnLeScan = mOnLeScan;
        this.mBluetoothAdapter = mBluetoothAdapter;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (checkBluetoothEnabled()) {
                this.mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            } else {
                mBluetoothLeScanner = null;
                Tlog.w(TAG, " BleScanImpl checkBluetoothEnabled() BT not enable ");
            }
            this.mScannerCallBack = new BleScanCBL();
            this.mBleScanCallBack = null;
        } else {
            this.mBleScanCallBack = new BleScanImpl.BleScanCB();
            this.mBluetoothLeScanner = null;
            this.mScannerCallBack = null;
        }
    }

    private volatile boolean mBTEnable;

    void setBTState(boolean on) {

        if (mBTEnable != on) {
            Tlog.w(TAG, " BleScanImpl BT state change ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (on) {
                    getBluetoothLeScanner();
                } else {
                    mBluetoothLeScanner = null;
                }
            }
        }

        this.mBTEnable = on;
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
        if (checkBluetoothEnabled()) {
            return mBluetoothAdapter.startLeScan(mBleScanCallBack);
        }
        return false;
    }

    private void stopLeScan() {
        if (checkBluetoothEnabled()) {
            mBluetoothAdapter.stopLeScan(mBleScanCallBack);
        }
    }

    /*************/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean startLeScanner() {
        BluetoothLeScanner bluetoothLeScanner = getBluetoothLeScanner();

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
        BluetoothLeScanner bluetoothLeScanner = getBluetoothLeScanner();
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
