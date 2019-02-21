package cn.com.swain.baselib.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.List;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/9 0009
 * desc :
 */
public class WiFiUtil {

    /**
     * 判断是否连接着wifi
     *
     * @param applicationContext {@link Application}
     * @return boolean
     */
    public static boolean isWiFiConnected(Application applicationContext) {
        if (applicationContext == null) {
            return false;
        }
        WifiManager mWiFiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        return isWiFiConnected(mWiFiManager);
    }

    /**
     * 判断是否连接着wifi
     *
     * @param mWiFiManager {@link WifiManager}
     * @return boolean
     */
    public static boolean isWiFiConnected(WifiManager mWiFiManager) {
        if (mWiFiManager == null) {
            return false;
        }
        if (!mWiFiManager.isWifiEnabled()) {
            return false;
        }

        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
//        String ssid = connectionInfo != null ? connectionInfo.getSSID() : null;
        int conNetworkID = connectionInfo != null ? connectionInfo.getNetworkId() : -1;

        return (conNetworkID != -1);
    }


    /**
     * 获取连接WiFi的SSID
     *
     * @param applicationContext {@link Application}
     * @return SSID
     */
    public static String getConnectedWiFiSSID(Application applicationContext) {
        if (applicationContext == null) {
            return "unknown";
        }
        WifiManager mWiFiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        return getConnectedWiFiSSID(mWiFiManager);
    }

    /**
     * 获取连接WiFi的SSID
     *
     * @param mWiFiManager {@link WifiManager}
     * @return SSID
     */
    public static String getConnectedWiFiSSID(WifiManager mWiFiManager) {
        if (mWiFiManager == null) {
            return "unknown";
        }
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
        String ssid = connectionInfo != null ? connectionInfo.getSSID() : "unknown";
        int conNetworkID = connectionInfo != null ? connectionInfo.getNetworkId() : -1;

        // 有些手机获取不到连接的ssid
        if (conNetworkID != -1) {
            List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
            if (configuredNetworks != null && configuredNetworks.size() > 0) {
                for (WifiConfiguration mWifiConfiguration : configuredNetworks) {
                    if (mWifiConfiguration.networkId == conNetworkID) {
//                        String pwd = mWifiConfiguration.preSharedKey;
                        ssid = mWifiConfiguration.SSID;
                        break;
                    }
                }
            }
        }

        if (conNetworkID == -1) {
            return "unknown";
        }

        ssid = ssid.replaceAll("\"", "");
        return ssid;
    }


    /**
     * 是否是5G频段
     *
     * @param mWiFiManager {@link WifiManager}
     * @return 是5G
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean is5GHz(WifiManager mWiFiManager) {
        if (null == mWiFiManager) {
            return false;
        }
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
        if (connectionInfo.getNetworkId() == -1) {
            return false;
        }
        int frequency = connectionInfo.getFrequency();
        return is5GHz(frequency);
    }

    /**
     * 是否是5G频段
     *
     * @param freq 频段
     * @return 是5G
     */
    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }


    /**
     * 获取WiFi的BSSID
     *
     * @param ssid               ssid
     * @param applicationContext {@link Application}
     * @return BSSID
     */
    public static String getWiFiBSSID(String ssid, Application applicationContext) {
        if (null == applicationContext) {
            return "";
        }
        WifiManager mWiFiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        return getWiFiBSSID(ssid, mWiFiManager);
    }

    /**
     * 获取WiFi的BSSID
     *
     * @param ssid         ssid
     * @param mWiFiManager {@link WifiManager}
     * @return BSSID
     */
    public static String getWiFiBSSID(String ssid, WifiManager mWiFiManager) {

        String connectWiFiBSSID = getConnectWiFiBSSID(ssid, mWiFiManager);

        if ("".equals(connectWiFiBSSID) || null == connectWiFiBSSID) {
            int networkIDByConfig = getNetworkIDByConfig(ssid, mWiFiManager);

            if (networkIDByConfig == -1) {
                return "";
            }

            return getWiFiBSSIDByConfig(networkIDByConfig, mWiFiManager);
        }

        return connectWiFiBSSID;
    }

    /**
     * 获取连接WiFi的BSSID
     *
     * @param applicationContext {@link Application}
     * @return BSSID
     */
    public static String getConnectWiFiBSSID(Application applicationContext) {
        if (applicationContext == null) {
            return "";
        }

        WifiManager mWiFiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);

        return getConnectWiFiBSSID(mWiFiManager);
    }

    /**
     * 获取连接WiFi的BSSID
     *
     * @param mWiFiManager {@link WifiManager}
     * @return BSSID
     */
    public static String getConnectWiFiBSSID(WifiManager mWiFiManager) {
        if (mWiFiManager == null) {
            return "";
        }
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();

        if (connectionInfo.getNetworkId() == -1) {
            return "";
        }

        return connectionInfo.getBSSID();
    }


    /**
     * 获取连接WiFi的BSSID
     *
     * @param ssid         SSID
     * @param mWiFiManager {@link WifiManager}
     * @return BSSID
     */
    public static String getConnectWiFiBSSID(String ssid, WifiManager mWiFiManager) {

        if (mWiFiManager == null) {
            return "";
        }

        String conSSID = checkWiFiName(ssid);

        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();

        if (connectionInfo.getSSID().equals(conSSID) &&
                connectionInfo.getNetworkId() != -1) {
            return connectionInfo.getBSSID();
        }

        return "";
    }

    /**
     * 根据ID,获取BSSID
     *
     * @param networkID    id
     * @param mWiFiManager {@link WifiManager}
     * @return BSSID
     */
    public static String getWiFiBSSIDByConfig(int networkID, WifiManager mWiFiManager) {
        if (mWiFiManager == null) {
            return "";
        }
        List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration mWiFiConfig : configuredNetworks) {
                if (mWiFiConfig.networkId == networkID) {
                    if (mWiFiConfig.BSSID != null) {
                        return mWiFiConfig.BSSID;
                    }
                    break;
                }
            }
        }

        return "";
    }

    /**
     * 获取NetworkID by SSID
     *
     * @param ssid         ssid
     * @param mWiFiManager {@link WifiManager}
     * @return networkID
     */
    public static int getNetworkIDByConfig(String ssid, WifiManager mWiFiManager) {
        if (null == ssid) {
            return -1;
        }
        if (mWiFiManager == null) {
            return -1;
        }
        ssid = checkWiFiName(ssid);
        List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
        if (configuredNetworks != null && configuredNetworks.size() > 0) {
            for (WifiConfiguration mWiFiConfig : configuredNetworks) {
                if (mWiFiConfig.SSID.equals(ssid)) {
                    return mWiFiConfig.networkId;
                }
            }
        }
        return -1;
    }

    /**
     * 获取链接的networkID
     *
     * @param applicationContext {@link Application}
     * @return networkID
     */
    public static int getConnectedNetworkID(Application applicationContext) {
        if (applicationContext == null) {
            return -1;
        }
        WifiManager mWiFiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        if (mWiFiManager == null) {
            return -1;
        }
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
        return connectionInfo != null ? connectionInfo.getNetworkId() : -1;
    }

    public static String checkWiFiName(String ssid) {

        if (ssid == null) {
            return null;
        }

        String conSSID;

        if (!ssid.startsWith("\"")) {
            conSSID = "\"" + ssid;
        } else {
            conSSID = ssid;
        }

        if (!conSSID.endsWith("\"")) {
            conSSID += "\"";
        }

        return conSSID;
    }


    /**
     * 无加密方式
     */
    public static final int SECURITY_NONE = 0;

    /**
     * WEP(安全较差)
     */
    public static final int SECURITY_WEP = 1;

    /**
     * EAP(迄今最安全的)
     */
    public static final int SECURITY_EAP = 2;

    /**
     * WPA-PSK/WPA2-PSK(目前最安全家用加密)
     */
    public static final int SECURITY_PSK = 3;
    /**
     * 未知
     */
    public static final int SECURITY_UNKNOWN = -1;

    /**
     * 获取此SSID的传输加密方式
     *
     * @param ssid         ssid
     * @param mWiFiManager {@link WifiManager}
     * @return {@link #SECURITY_EAP  #SECURITY_WEP  #SECURITY_PSK  #SECURITY_EAP  #SECURITY_UNKNOWN }
     */
    public static int getSecurity(String ssid, WifiManager mWiFiManager) {
        if (null == ssid) {
            return -1;
        }
        ssid = checkWiFiName(ssid);
        List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
        if (configuredNetworks != null && configuredNetworks.size() > 0) {
            for (WifiConfiguration mWiFiConfig : configuredNetworks) {
                if (mWiFiConfig.SSID.equals(ssid)) {
                    return getSecurity(mWiFiConfig);
                }
            }
        }
        return SECURITY_UNKNOWN;
    }

    /**
     * 获取链接的传输加密方式
     *
     * @param mWiFiManager {@link WifiManager}
     * @return {@link #SECURITY_EAP  #SECURITY_WEP  #SECURITY_PSK  #SECURITY_EAP  #SECURITY_UNKNOWN }
     */
    public static int getSecurity(WifiManager mWiFiManager) {
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
        if (connectionInfo.getNetworkId() == -1) {
            return SECURITY_UNKNOWN;
        }

        List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
        if (configuredNetworks != null && configuredNetworks.size() > 0) {
            for (WifiConfiguration mWiFiConfig : configuredNetworks) {
                if (mWiFiConfig.networkId == connectionInfo.getNetworkId()) {
                    return getSecurity(mWiFiConfig);
                }
            }
        }
        return SECURITY_UNKNOWN;
    }

    /**
     * 获取配置的传输加密方式
     *
     * @param config {@link WifiConfiguration}
     * @return {@link #SECURITY_EAP  #SECURITY_WEP  #SECURITY_PSK  #SECURITY_EAP  #SECURITY_UNKNOWN }
     */
    public static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)
                || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }
}
