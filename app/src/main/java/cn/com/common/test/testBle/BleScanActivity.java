package cn.com.common.test.testBle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.common.test.R;
import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.permission.PermissionHelper;
import cn.com.swain.baselib.permission.PermissionRequest;
import cn.com.swain.baselib.permission.PermissionSingleton;
import cn.com.swain.support.ble.enable.AbsBleEnable;
import cn.com.swain.support.ble.enable.BleEnabler;
import cn.com.swain.support.ble.scan.AbsBleScan;
import cn.com.swain.support.ble.scan.BleScanAuto;
import cn.com.swain.support.ble.scan.IBleScanObserver;
import cn.com.swain.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date: 2018-03-12
 * description:
 */

public class BleScanActivity extends AppCompatActivity {

    private String TAG = "fastBle";
    private ProgressBar progress;
    private BleScanAdapter mLstvAdapter;
    private TextView mTxtBleState;

    private IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    private BleReceiver mBleReceiver = new BleReceiver();

    private static final int MSG_WHAT_HIDE = 0x01;
    private static final int MSG_WHAT_BLE_STATE = 0x02;

    @SuppressLint("HandlerLeak")
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_WHAT_HIDE) {
                progress.setVisibility(ProgressBar.INVISIBLE);
            } else if (msg.what == MSG_WHAT_BLE_STATE) {
                regBleState(msg.arg1);
            }
        }
    };

    private void regBleState(int bleState) {
        switch (bleState) {
            case BluetoothAdapter.STATE_OFF:
                Tlog.d(TAG, "STATE_OFF 手机蓝牙关闭");
                mTxtBleState.setText("蓝牙关闭");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Tlog.d(TAG, "STATE_TURNING_OFF 手机蓝牙正在关闭");
                mTxtBleState.setText("蓝牙正在关闭");
                break;
            case BluetoothAdapter.STATE_ON:
                Tlog.d(TAG, "STATE_ON 手机蓝牙开启");
                mTxtBleState.setText("蓝牙开启");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Tlog.d(TAG, "STATE_TURNING_ON 手机蓝牙正在开启");
                mTxtBleState.setText("蓝牙正在开启");
                break;
        }
    }

    private AbsBleScan mBleScan;
    private AbsBleEnable mBleEnabler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan);


        String[] permissionArray = new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission_group.LOCATION
        };

        progress = (ProgressBar) findViewById(R.id.progressBar1);
        mTxtBleState = (TextView) findViewById(R.id.bt_state);
        ListView mLstv = (ListView) findViewById(R.id.scanLstv);
        mLstvAdapter = new BleScanAdapter(this);
        mLstv.setAdapter(mLstvAdapter);
        mLstv.setOnItemClickListener(new BleItemClick());


        Looper workLooper = LooperManager.getInstance().getWorkLooper();

        mBleEnabler = new BleEnabler(getApplication(), workLooper);

        mBleScan = new BleScanAuto(getApplication(), workLooper, new IBleScanObserver() {
            @Override
            public void onBsStartScan() {

            }

            @Override
            public void onBsStopScan() {

            }

            @Override
            public void onResultBsGattScan(ScanBle mBle) {
                mBle.calculateAccuracy();
                mLstvAdapter.onBleScan(mBle);
            }
        });

        PermissionSingleton.getInstance().requestPermissions(this, new PermissionRequest.OnPermissionResult() {

            @Override
            public boolean onPermissionRequestResult(String permission, boolean granted) {
                mBleEnabler.beEnableBle();
                regBleState(mBleEnabler.beGetBleState());
                if (granted) {
                    mBleScan.bsScanBleOnce();
                }
                return false;
            }
        }, permissionArray);


        h.sendEmptyMessageDelayed(MSG_WHAT_HIDE, 3000);
    }

    public void forceScan(View v) {
        mBleScan.bsForceAutoScan();
        mLstvAdapter.clearData();
        progress.setVisibility(ProgressBar.VISIBLE);
        h.sendEmptyMessageDelayed(MSG_WHAT_HIDE, 3000);
    }

    public void stopScan(View v) {
        mBleScan.bsStopScan();
        progress.setVisibility(ProgressBar.INVISIBLE);
    }

    public void enableBle(View view) {
        if (mBleEnabler.beIsBleEnable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth has been activated", Toast.LENGTH_SHORT).show();
        } else {
            mBleEnabler.beEnableBle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mBleReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBleReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleScan.bsStopScan();
        PermissionSingleton.getInstance().release(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionSingleton.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private class BleItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if (mLstvAdapter.getCount() >= position) {
                ScanBle item = mLstvAdapter.getItem(position);

                if (item != null) {

                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), BleConActivity.class);
                    i.putExtra("cn/com/swain/support/ble", item);
                    startActivity(i);

                }
            }
        }
    }


    private class BleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                h.obtainMessage(MSG_WHAT_BLE_STATE, state, state).sendToTarget();
            }
        }

    }
}
