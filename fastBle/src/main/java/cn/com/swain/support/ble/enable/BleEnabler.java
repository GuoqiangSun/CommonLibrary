package cn.com.swain.support.ble.enable;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

import java.lang.ref.WeakReference;

import cn.com.swain.baselib.app.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/4 0004
 * desc :
 */

public class BleEnabler extends AbsBleEnable {

    private static final String TAG = "BleEnable";

    private BluetoothAdapter mBluetoothAdapter;

    private BleEnableHandler mBleEnableHandler;

    public BleEnabler(Context mCtx, Looper mWorkLooper) {
        BluetoothManager mBluetoothManager = (BluetoothManager) mCtx.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
        init(mWorkLooper, mBluetoothAdapter);
    }

    private void init(Looper mWorkLooper, BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mBleEnableHandler = new BleEnableHandler(this, mWorkLooper);
    }

    @Override
    public int beGetBleState() {
        return mBluetoothAdapter.getState();
    }

    @Override
    public boolean beIsBleEnable() {

        return (mBluetoothAdapter.isEnabled());
    }


    @Override
    public void skipBleSetting(Context mCtx) {
        mCtx.startActivity(new
                Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
    }

    @Override
    public boolean beEnableBle() {
        return beIsBleEnable() || mBluetoothAdapter.enable();
    }

    @Override
    public void checkBleState(BleStateResult mResult) {
        if (!mResult.checkFinish() && mResult.isValidDelay()) {
            mResult.updateTimes();
            Message message = mBleEnableHandler.obtainMessage(MSG_WHAT_CHECK_BLE_STATE, mResult);
            mBleEnableHandler.sendMessageDelayed(message, mResult.getDelay());
        } else {
            Tlog.v(TAG, " check finish()  cru:" + mResult.getCurTimes() + " total:" + mResult.getTotalTimes() + " delay:" + mResult.getDelay());
        }
    }

    private static final int MSG_WHAT_CHECK_BLE_STATE = 0x01;

    private void handleMessage(Message msg) {

        if (msg.what == MSG_WHAT_CHECK_BLE_STATE) {

            BleStateResult mResult = (BleStateResult) msg.obj;
            IBleStateCallBack mBleStateCallBack;

            if (mResult != null && (mBleStateCallBack = mResult.mBleStateCallBack) != null) {

                Tlog.v(TAG, " checkBleState : mResult times:" + mResult.getCurTimes());

                if (mBluetoothAdapter.isEnabled()) {
                    mResult.setEnabled();
                } else {
                    mResult.setDisEnabled();
                }

                mBleStateCallBack.onBleState(mResult);
                checkBleState(mResult);

            } else {
                Tlog.e(TAG, " checkBleState : mResult==null");
            }


        }

    }

    private static class BleEnableHandler extends Handler {
        private final WeakReference<BleEnabler> wr;

        public BleEnableHandler(BleEnabler mBleEnabler, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<BleEnabler>(mBleEnabler);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BleEnabler mBleEnabler = wr.get();

            if (mBleEnabler == null) {
                Tlog.e(TAG, "<BleEnableHandler> BleEnabler == null");
                return;
            }

            mBleEnabler.handleMessage(msg);

        }
    }

}
