package cn.com.common.test.testBle;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.com.common.test.R;
import cn.com.common.test.global.LooperManager;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.ble.connect.AbsBleConnect;
import cn.com.swain.support.ble.connect.BleConnectEngine;
import cn.com.swain.support.ble.connect.IBleConCallBack;
import cn.com.swain.support.ble.scan.ScanBle;
import cn.com.swain.support.ble.send.AbsBleSend;
import cn.com.swain.support.ble.send.BleDataSendProduce;
import cn.com.swain.support.ble.send.SendDataQueue;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018-03-13
 * description:
 */

public class BleConActivity extends AppCompatActivity {


    private String TAG = "bleCon";
    private ScanBle item;

    @SuppressLint("HandlerLeak")
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                String msgStr = (String) msg.obj;
                mTxtNotify.append(msgStr);
            } else if (msg.what == 1) {
                mTxtBleState.setText("连接成功");
            } else if (msg.what == 2) {
                mTxtBleState.setText("订阅成功");
            } else if (msg.what == 3) {
                mTxtBleState.setText("断开连接");
            } else if (msg.what == 4) {
                mTxtBleState.setText("未连接");
            } else if (msg.what == 5) {
                mTxtBleState.setText("连接失败");
            } else if (msg.what == 6) {
                mTxtBleState.setText("订阅失败");
            }

        }
    };

    /**
     * {@link BleConnectEngine}
     */
    private AbsBleConnect mBleCon;
    private ArrayList<AbsBleSend> absBleSends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ble_con);

        item = getIntent().getParcelableExtra("ble");

        initView();


        mBleCon = new BleConnectEngine(this, LooperManager.getInstance().getWorkLooper(), new IBleConCallBack() {
            @Override
            public void onResultConnect(boolean result, ScanBle mItem) {

                if (result) {
                    Toast.makeText(getApplication(), "connect success " + mItem.address, Toast.LENGTH_SHORT).show();
                    h.sendEmptyMessage(1);
                } else {
                    Toast.makeText(getApplication(), "connect fail " + mItem.address, Toast.LENGTH_SHORT).show();
                    h.sendEmptyMessage(5);
                }

            }

            @Override
            public void onResultAlreadyConnected(ScanBle mItem) {
                Toast.makeText(getApplication(), "already connected " + mItem.address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultDisconnectPassively(boolean result, ScanBle mItem) {
                style = null;
                if (result) {
                    h.sendEmptyMessage(3);
                    Toast.makeText(getApplication(), "passively disconnect success " + mItem.address, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "passively disconnect fail " + mItem.address, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onResultDisconnectActively(ScanBle mItem) {
                style = null;
                h.sendEmptyMessage(3);
                Toast.makeText(getApplication(), "actively disconnect success " + mItem.address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultServiceOrder(boolean result, ScanBle mItem, BluetoothGatt mConGatt) {

                if (result) {
                    h.sendEmptyMessage(2);
                    Toast.makeText(getApplication(), "service order success " + mItem.address, Toast.LENGTH_SHORT).show();

                    style = BleDataSendProduce.showService(mConGatt);
                    absBleSends = BleDataSendProduce.produceBleSend(mConGatt);
                } else {
                    h.sendEmptyMessage(6);
                    Toast.makeText(getApplication(), "service order fail " + mItem.address, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPeripheralNotify(final String mac, String uuidStr, byte[] data) {

                String s = StrUtil.toString(data);
                Tlog.v(TAG, "onPeripheralNotify " + mac + ": " + s);
                h.obtainMessage(0, s).sendToTarget();

            }

            @Override
            public void onWriteDataFail(ScanBle mItem) {
                Toast.makeText(getApplication(), "msg send fail " + ": " + mItem.address, Toast.LENGTH_SHORT).show();

            }
        }
        );

        mBleCon.connect(item);

    }

    private TextView mTxtBleState;
    private EditText mEdtData;
    private TextView mTxtNotify;
    private TextView mTxtSend;

    private void initView() {

        TextView mTxtName = (TextView) findViewById(R.id.txtName);
        TextView mTxtAdd = (TextView) findViewById(R.id.txtAddress);
        TextView mTxtRssi = (TextView) findViewById(R.id.txtRssi);
        mTxtName.setText(item.name);
        mTxtAdd.setText(item.address);
        mTxtRssi.setText("rssi:" + item.rssi);

        mTxtBleState = (TextView) findViewById(R.id.txtBleState);
        mEdtData = (EditText) findViewById(R.id.edtData);
        mTxtNotify = (TextView) findViewById(R.id.txtNotify);
        mTxtSend = findViewById(R.id.txtSend);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBleCon != null) {
            mBleCon.disconnect(item);
        }
    }

    public void conBle(View v) {
        if (mBleCon != null) {
            mBleCon.connect(item);
        }
    }

    public void disconBle(View v) {
        if (mBleCon != null) {
            mBleCon.disconnect(item);
        }
        mTxtBleState.setText("未连接");
        if (absBleSends != null) {
            absBleSends.clear();
            absBleSends = null;
        }
        if (mBleSend != null) {
            mBleSend.removeMsg();
            mBleSend.closeGatt();
            mBleSend = null;
        }
    }

    SpannableStringBuilder style = null;

    public void lookService(View v) {

        new AlertDialog.Builder(this)
                .setTitle("服务")// 设置对话框标题
                .setMessage(style != null ? style : "null").show();// 设置显示的内容

    }

    public void clearRecData(View v) {
        mTxtNotify.setText("");
    }

    public void clearSendData(View v) {
        mTxtSend.setText("");
    }

    private AbsBleSend mBleSend;

    public void selectWriteService(View v) {

        ArrayList<AbsBleSend> mAbsBleSends = absBleSends;

        if (mAbsBleSends == null) {
            Toast.makeText(getApplicationContext(), " service is not order ", Toast.LENGTH_SHORT).show();
            return;
        }

        final Object[] objects = mAbsBleSends.toArray();

        int size = mAbsBleSends.size();
        final String items[] = new String[size];
        for (int i = 0; i < size; i++) {
            AbsBleSend mAbsBleSend = mAbsBleSends.get(i);
            items[i] = mAbsBleSend.getUuidStr();
        }

//        final String items[] = {"刘德华", "张柏芝", "蔡依林", "张学友"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("写特征值:");
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (mBleSend != null) {
                    mBleSend.removeMsg();
                    mBleSend = null;
                }

                AbsBleSend tBleSend = (AbsBleSend) objects[which];
                mBleSend = new SendDataQueue(LooperManager.getInstance().getProtocolLooper(), tBleSend);

                Toast.makeText(BleConActivity.this, mBleSend.getUuidStr(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public void sendData(View v) {

        if (mBleSend != null) {
            String s = mEdtData.getText().toString();
            if ("".equals(s)) {
                Toast.makeText(getApplicationContext(), "没数据", Toast.LENGTH_LONG).show();
                return;
            }

//            byte[] bytes = s.getBytes();
            byte[] bytes;

            try {
                bytes = StrUtil.toHex(s);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            mTxtSend.append(StrUtil.toString(bytes));
            mBleSend.sendData(bytes);

        } else {
            Toast.makeText(getApplicationContext(), "没选择要发送的特征值", Toast.LENGTH_LONG).show();
        }
    }


}
