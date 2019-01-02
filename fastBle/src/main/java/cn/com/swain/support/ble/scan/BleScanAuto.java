package cn.com.swain.support.ble.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018-03-12
 * description:
 */

public class BleScanAuto extends AbsBleScan {

    public static final String TAG = "fastBle";

    private BleScanImpl mBaseBleScan;

    private IBleScanObserver mBleScanObserver;
    private BleScanHandler mBleScanHandler;


    public BleScanAuto(Context mCtx, Looper mWorkLooper, IBleScanObserver mBleScanObserver) {

        BluetoothManager mBluetoothManager = (BluetoothManager) mCtx.getSystemService(Context.BLUETOOTH_SERVICE);
        assert mBluetoothManager != null;
        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();

        this.mBleScanHandler = new BleScanHandler(this, mWorkLooper);
        this.mBaseBleScan = new BleScanImpl(mBluetoothAdapter, this);
        this.mBleScanObserver = mBleScanObserver;

        if (mBluetoothAdapter != null) {
            this.mBaseBleScan.setBTState(mBluetoothAdapter.isEnabled());
        }
    }

    @Override
    public void bsAutoScanBle() {
        if (mBleScanHandler.hasMessages(MSG_AUTO_SCAN_START)) {
            mBleScanHandler.removeMessages(MSG_AUTO_SCAN_START);
        }
        if (mBleScanHandler.hasMessages(MSG_AUTO_SCAN_STOP)) {
            mBleScanHandler.removeMessages(MSG_AUTO_SCAN_STOP);
        }
        mBleScanHandler.sendEmptyMessageDelayed(MSG_AUTO_SCAN_START, 1000);
    }

    @Override
    public void bsForceAutoScan() {
        mBleScanHandler.removeMessages(MSG_START_SCAN);
        mBleScanHandler.removeMessages(MSG_STOP_SCAN);
        mBleScanHandler.removeMessages(MSG_AUTO_SCAN_START);
        mBleScanHandler.removeMessages(MSG_AUTO_SCAN_STOP);
        mBleScanHandler.sendEmptyMessageDelayed(MSG_AUTO_SCAN_START, 1000);
    }

    @Override
    public void bsStopScan() {
        mBleScanHandler.removeMessages(MSG_START_SCAN);
        mBleScanHandler.removeMessages(MSG_STOP_SCAN);
        mBleScanHandler.removeMessages(MSG_AUTO_SCAN_START);
        mBleScanHandler.removeMessages(MSG_AUTO_SCAN_STOP);
        mBleScanHandler.sendEmptyMessage(MSG_STOP_SCAN);
    }

    @Override
    public void checkBleScanResult(boolean removeLast, BleScanResult mBleScanResult) {

        if (removeLast) {
            removeCheckScanResult();
        }

        if (mBleScanResult != null) {
            Message message = mBleScanHandler.obtainMessage(MSG_WHAT_CHECK_SCAN_RESULT, mBleScanResult);
            mBleScanHandler.sendMessageDelayed(message, mBleScanResult.getDelay());
        }

    }

    @Override
    public void removeCheckScanResult() {
        if (mBleScanHandler.hasMessages(MSG_WHAT_CHECK_SCAN_RESULT)) {
            Tlog.v(TAG, "mBleScanHandler. hasMessages  MSG_WHAT_CHECK_SCAN_RESULT remove");
            mBleScanHandler.removeMessages(MSG_WHAT_CHECK_SCAN_RESULT);
        }
    }

    @Override
    public void bsBleStateChange(boolean on) {
        this.mBaseBleScan.setBTState(on);
    }

    @Override
    public void bsScanBleOnce() {
        if (mBleScanHandler.hasMessages(MSG_START_SCAN)) {
            mBleScanHandler.removeMessages(MSG_START_SCAN);
        }
        if (mBleScanHandler.hasMessages(MSG_STOP_SCAN)) {
            mBleScanHandler.removeMessages(MSG_STOP_SCAN);
        }
        mBleScanHandler.sendEmptyMessage(MSG_START_SCAN);
    }

    @Override
    void leScan(String name, String address, int rssi, List<ParcelUuid> serviceUuids) {
        final ScanBle bleData = new ScanBle();
        bleData.setAddress(address);
        bleData.setName(name);
        bleData.setRssi(rssi);
        bleData.setBroadServiceUuids(serviceUuids);
        mBleScanHandler.obtainMessage(MSG_ON_BLE_SCAN, bleData).sendToTarget();

    }

    private boolean isScan;

    private void scanBle(boolean on) {

        if (on) {
            Tlog.v(TAG, " startLeScan. ");

            if (!isScan) {
                try {
                    isScan = mBaseBleScan.startScan();
                } catch (Exception e) {
                    Tlog.e(TAG, "startLeScan Exception", e);
                }

                if (!isScan) {
                    Tlog.e(TAG, "startLeScan fail see androidLog");
                }

                if (mBleScanObserver != null) {
                    mBleScanObserver.onBsStartScan();
                } else {
                    Tlog.e(TAG, " scanBle mBleScanObserver==null . ");
                }

            } else {
                Tlog.e(TAG, " le is scanning ... ");
            }
        } else {

            Tlog.v(TAG, "stopLeScan. ");
            if (isScan) {
                isScan = false;
                try {
                    mBaseBleScan.stopScan();
                } catch (Exception e) {
                    Tlog.e(TAG, "stopScan Exception", e);
                }
                if (mBleScanObserver != null) {
                    mBleScanObserver.onBsStopScan();
                } else {
                    Tlog.e(TAG, " scanBle mBleScanObserver==null . ");
                }

            } else {
                Tlog.e(TAG, " le not scanning ... ");
            }
        }

    }

    private void autoScan(boolean on) {
        scanBle(on);
        if (on) {
            mBleScanHandler.sendEmptyMessageDelayed(MSG_AUTO_SCAN_STOP, MSG_DELAY_AUTO_SCAN);
        } else {
            mBleScanHandler.sendEmptyMessageDelayed(MSG_AUTO_SCAN_START, MSG_STOP_SCAN_SLEEP);
        }
    }

    private void onBleScan(ScanBle mScanBle) {
        if (mBleScanObserver != null) {
            mBleScanObserver.onResultBsGattScan(mScanBle);
        } else {
            Tlog.e(TAG, " onBleScan mBleScanObserver==null . ");
        }
    }


    private static final int MSG_START_SCAN = 0x03;
    private static final int MSG_STOP_SCAN = 0x04;

    private static final int MSG_ON_BLE_SCAN = 0x05;

    private static final int MSG_AUTO_SCAN_START = 0x06;
    private static final int MSG_AUTO_SCAN_STOP = 0x07;

    /**
     * 自动扫描扫描时间
     */
    private static final int MSG_DELAY_AUTO_SCAN = 6000;

    /**
     * 自动扫描休眠时间
     */
    private static final int MSG_STOP_SCAN_SLEEP = 3000;


    private static final int MSG_WHAT_CHECK_SCAN_RESULT = 0x08;

    private void handleMessage(Message msg) {

        switch (msg.what) {

            case MSG_START_SCAN:
                scanBle(true);
                break;
            case MSG_STOP_SCAN:
                scanBle(false);
                break;
            case MSG_ON_BLE_SCAN:
                onBleScan((ScanBle) msg.obj);
                break;
            case MSG_AUTO_SCAN_START:
                autoScan(true);
                break;
            case MSG_AUTO_SCAN_STOP:
                autoScan(false);
                break;
            case MSG_WHAT_CHECK_SCAN_RESULT:

                BleScanResult mBleScanResult = (BleScanResult) msg.obj;
                mBleScanResult.checkIsNull();
                IBleScanCheckResult mIBleScanCheckResult = mBleScanResult.mBleScanCheckResult;
                if (mIBleScanCheckResult != null) {
                    mIBleScanCheckResult.OnBleScanResult(mBleScanResult);
                }

                break;
        }
    }

    private static class BleScanHandler extends Handler {
        private final WeakReference<BleScanAuto> wr;

        public BleScanHandler(BleScanAuto mBleScanAuto, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<BleScanAuto>(mBleScanAuto);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BleScanAuto mBleScanAuto;

            if (wr != null && (mBleScanAuto = wr.get()) != null) {
                mBleScanAuto.handleMessage(msg);
            } else {
                Tlog.e(TAG, "<BleScanHandler> BleScanAuto == null");
            }
        }
    }

}
