package cn.com.common.test.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collection;

import cn.com.swain.baselib.app.Tlog;
import cn.com.common.test.R;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/31 0031
 * desc :
 */
public class P2pComServerActivity extends AppCompatActivity {

    private static final String TAG = "p2pDemo";

    private final BroadcastReceiver mWiFiP2pReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Tlog.e(TAG, "P2pComServerActivity mWiFiP2pReceiver " + intent.getAction());
            assert action != null;

            switch (action) {

                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:

                    //确认WIFI-P2P是否启动
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

                        Tlog.i(TAG, " WIFI_P2P_STATE_CHANGED_ACTION wifiP2pEnabled  ");

                    } else {

                        Tlog.e(TAG, " WIFI_P2P_STATE_CHANGED_ACTION wifiP2pNotEnabled  ");
                    }
                    break;

                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            Collection<WifiP2pDevice> deviceList = peers.getDeviceList();
                            Tlog.v(TAG, " WIFI_P2P_PEERS_CHANGED_ACTION onPeersAvailable  " + deviceList.size());
//                            for (WifiP2pDevice next : deviceList) {
//                                Tlog.v(TAG, " WIFI_P2P_PEERS_CHANGED_ACTION onPeersAvailable  " + next.toString());
//                            }
                            mAdapter.addAllClear(deviceList);
                        }
                    });
                    break;

                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        Tlog.e(TAG, " WifiP2pManager.EXTRA_NETWORK_INFO is connected ");
                    } else {
                        Tlog.e(TAG, " WifiP2pManager.EXTRA_NETWORK_INFO is not connected ");
                    }
                    break;

                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                    Tlog.i(TAG, " WIFI_P2P_THIS_DEVICE_CHANGED_ACTION onDeviceInfo " + device.toString());

                    mAdapter.deviceChange(device);

                    break;

            }

        }
    };

    private WifiP2pManager mWifiP2pManager;
    public static WifiP2pManager.Channel mChannel;
    private P2pScanAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_p2p_scan);

        ListView mLstv = findViewById(R.id.discoveryLstv);
        mAdapter = new P2pScanAdapter(this.getApplicationContext());
        mLstv.setAdapter(mAdapter);
        mLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiP2pDevice item = mAdapter.getItem(position);
                if (item != null) {
                    Intent i = new Intent(P2pComServerActivity.this, P2pClientActivity.class);
                    i.putExtra("p2p", item);
                    startActivity(i);
                }
            }
        });

        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        assert mWifiP2pManager != null;
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Tlog.e(TAG, " wifiP2pManager.initialize onChannelDisconnected ");
            }
        });


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(mWiFiP2pReceiver, intentFilter);

    }

    public void discoveryPeers(View v) {
        mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Tlog.d(TAG, " discoverPeers success");
            }

            @Override
            public void onFailure(int reasonCode) {
                Tlog.e(TAG, " discoverPeers fail: " + reasonCode);
            }
        });

    }

    public void createGroup(View v) {
        startActivity(new Intent(this, P2pServerActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mWiFiP2pReceiver);

    }
}
