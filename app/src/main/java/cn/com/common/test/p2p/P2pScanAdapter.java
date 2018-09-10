package cn.com.common.test.p2p;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import cn.com.common.test.R;


public class P2pScanAdapter extends BaseAdapter {

    private Context mContext;
    private final ArrayList<WifiP2pDevice> data = new ArrayList<>();
    private Handler mUIHandler;

    public P2pScanAdapter(Context mContext) {

        this.mContext = mContext;
        this.mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                P2pScanAdapter.this.notifyDataSetChanged();
            }
        };

    }

    public synchronized void addAllClear(Collection<WifiP2pDevice> deviceList) {
        data.clear();
        data.addAll(deviceList);
        if (mUIHandler != null) {
            mUIHandler.sendEmptyMessage(0);
        }
    }


    public synchronized void deviceChange(WifiP2pDevice device) {


        for (WifiP2pDevice mP2pDevice : data) {
            if (mP2pDevice.deviceAddress.equalsIgnoreCase(device.deviceAddress)) {
                data.remove(mP2pDevice);
                break;
            }
        }

        data.add(device);

        if (mUIHandler != null) {
            mUIHandler.sendEmptyMessage(0);
        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public WifiP2pDevice getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_p2p, null);

            mHolder.name = convertView.findViewById(R.id.name);
            mHolder.address = convertView.findViewById(R.id.address);

            mHolder.primaryDeviceType = convertView.findViewById(R.id.primaryDeviceType);
            mHolder.secondaryDeviceType = convertView.findViewById(R.id.secondaryDeviceType);

            mHolder.status = convertView.findViewById(R.id.status);
            mHolder.groupOwner = convertView.findViewById(R.id.groupOwner);

            mHolder.serviceDiscoveryCapable = convertView.findViewById(R.id.serviceDiscoveryCapable);
            mHolder.wpsDisplaySupported = convertView.findViewById(R.id.wpsDisplaySupported);

            mHolder.wpsKeypadSupported = convertView.findViewById(R.id.wpsKeypadSupported);
            mHolder.wpsPbcSupported = convertView.findViewById(R.id.wpsPbcSupported);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        WifiP2pDevice mWifiP2pDevice = data.get(position);

        if (mWifiP2pDevice != null) {

            String deviceName = mWifiP2pDevice.deviceName;
            mHolder.name.setText(String.valueOf(deviceName));
            String deviceAddress = mWifiP2pDevice.deviceAddress;
            mHolder.address.setText(String.valueOf(deviceAddress));

            String primaryType = mWifiP2pDevice.primaryDeviceType;
            mHolder.primaryDeviceType.setText(String.valueOf(primaryType));
            String secondaryDeviceType = mWifiP2pDevice.secondaryDeviceType;
            mHolder.secondaryDeviceType.setText(String.valueOf(secondaryDeviceType));

            int status = mWifiP2pDevice.status;
            mHolder.status.setText(String.valueOf(status));
            boolean groupOwner = mWifiP2pDevice.isGroupOwner();
            mHolder.groupOwner.setText(String.valueOf(groupOwner));

            boolean serviceDiscoveryCapable = mWifiP2pDevice.isServiceDiscoveryCapable();
            mHolder.serviceDiscoveryCapable.setText(String.valueOf(serviceDiscoveryCapable));
            boolean wpsDisplaySupported = mWifiP2pDevice.wpsDisplaySupported();
            mHolder.wpsDisplaySupported.setText(String.valueOf(wpsDisplaySupported));

            boolean wpsKeypadSupported = mWifiP2pDevice.wpsKeypadSupported();
            mHolder.wpsKeypadSupported.setText(String.valueOf(wpsKeypadSupported));
            boolean wpsPbcSupported = mWifiP2pDevice.wpsPbcSupported();
            mHolder.wpsPbcSupported.setText(String.valueOf(wpsPbcSupported));

        }

        return convertView;
    }


    public static class ViewHolder {
        public TextView name;
        public TextView address;

        public TextView primaryDeviceType;
        public TextView secondaryDeviceType;

        public TextView status;
        public TextView groupOwner;

        public TextView serviceDiscoveryCapable;
        public TextView wpsDisplaySupported;

        public TextView wpsKeypadSupported;
        public TextView wpsPbcSupported;

    }
}
