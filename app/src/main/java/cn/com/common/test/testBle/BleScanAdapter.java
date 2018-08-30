package cn.com.common.test.testBle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.com.common.support.ble.scan.ScanBle;
import cn.com.common.test.R;


public class BleScanAdapter extends BaseAdapter {

    private Context mContext;
    private final ArrayList<ScanBle> data = new ArrayList<ScanBle>();
    private LeSort mBleSort = new LeSort();
    private Handler mUIHandler;

    public BleScanAdapter(Context mContext) {

        this.mContext = mContext;
        this.mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                BleScanAdapter.this.notifyDataSetChanged();
            }
        };

    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void onBleScan(ScanBle mScanBle) {
        boolean add = true;
        if (data.size() > 0) {
            for (ScanBle ble : data) {
                if (ble.address.equalsIgnoreCase(mScanBle.address)) {
                    add = false;
                    break;
                }
            }
        }
        if (add) {
            data.add(mScanBle);
        }
        Collections.sort(data, mBleSort);

        if (mUIHandler != null) {
            mUIHandler.sendEmptyMessage(0);
        }
    }

    private class LeSort implements Comparator<ScanBle> {

        @Override
        public int compare(ScanBle lhs, ScanBle rhs) {
            // TODO Auto-generated method stub

            if (lhs.rssi > rhs.rssi) {
                return -1;
            } else if (lhs.rssi < rhs.rssi) {
                return 1;
            }

            return 0;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public ScanBle getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder mHolder = null;

        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_ble, null);
            mHolder.mNameTxt = (TextView) convertView.findViewById(R.id.name);
            mHolder.mAddressTxt = (TextView) convertView.findViewById(R.id.address);
            mHolder.mRssiTxt = (TextView) convertView.findViewById(R.id.rssi);
            mHolder.scanRecordTxt = (TextView) convertView.findViewById(R.id.scanRecord);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        ScanBle scanBle = data.get(position);

        if (scanBle != null) {
            mHolder.mNameTxt.setText(scanBle.name);
            mHolder.mAddressTxt.setText(scanBle.address);
            mHolder.mRssiTxt.setText(String.valueOf(scanBle.rssi));
            mHolder.scanRecordTxt.setText(scanBle.getFirstBroadUUID());
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView mNameTxt;
        public TextView mAddressTxt;
        public TextView mRssiTxt;
        public TextView scanRecordTxt;
    }
}
