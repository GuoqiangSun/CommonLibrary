package cn.com.swain.baselib.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/4 0004
 * desc :
 */
public class IpUtil {


//    private static final String IP_REGEX = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";// error

    private static final String IP_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";


    private static Pattern IP_COMPILE;

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipMatches(String text) {
        // 这是真正的MAC地址；正则表达式;
        if (text != null && !text.isEmpty()) {

            if (IP_COMPILE == null) {
                IP_COMPILE = Pattern.compile(IP_REGEX);
            }

            Matcher matcher = IP_COMPILE.matcher(text);
            return matcher.matches();

        }
        return false;
    }


    public static final String BROAD_IP_BOUND = "255.255.255.255";

    /**
     * 获取全域广播地址
     * （有些路由器会屏蔽这个广播地址,慎用）
     */
    public static InetAddress getBoundBroadcast() {

        try {
            return InetAddress.getByName(BROAD_IP_BOUND);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取wifiIP的广播地址
     * <p>
     * 根据wifi的ip 子网掩码 算出广播地址
     */
    public static InetAddress getWiFiBroadcastAddress(Context mContext) {
        WifiManager myWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (myWifiManager == null) {
            return null;
        }
        DhcpInfo myDhcpInfo = myWifiManager.getDhcpInfo();
        if (myDhcpInfo == null) {
            return null;
        }
        int broadcast = (myDhcpInfo.ipAddress & myDhcpInfo.netmask)
                | ~myDhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        try {
            return InetAddress.getByAddress(quads);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取本机广播地址
     * 通过Android接口获取
     */
    public static InetAddress getLocalBroadcastAddress() {
        try {
            Enumeration<NetworkInterface> eni = NetworkInterface
                    .getNetworkInterfaces();
            while (eni.hasMoreElements()) {

                NetworkInterface networkCard = eni.nextElement();
                List<InterfaceAddress> ncAddrList = networkCard
                        .getInterfaceAddresses();
                Iterator<InterfaceAddress> ncAddrIterator = ncAddrList.iterator();
                while (ncAddrIterator.hasNext()) {
                    InterfaceAddress networkCardAddress = ncAddrIterator.next();
                    InetAddress address = networkCardAddress.getAddress();
                    if (!address.isLoopbackAddress()) {
                        String hostAddress = address.getHostAddress();

                        if (hostAddress.indexOf(":") > 0) {
                            // case : ipv6
                            continue;
                        } else {
                            // case : ipv4
//                            String maskAddress = calcMaskByPrefixLength(networkCardAddress.getNetworkPrefixLength());
//                            String subnetAddress = calcSubnetAddress(hostAddress, maskAddress);
                            String broadcastAddress = networkCardAddress.getBroadcast().getHostAddress();

                            try {
                                return InetAddress.getByName(broadcastAddress);
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    } else {
//                        String loopback = networkCardAddress.getAddress().getHostAddress();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String calcMaskByPrefixLength(int length) {
        int mask = -1 << (32 - length);
        int partsNum = 4;
        int bitsOfPart = 8;
        int maskParts[] = new int[partsNum];
        int selector = 0x000000ff;

        for (int i = 0; i < maskParts.length; i++) {
            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }

        String result = "";
        result = result + maskParts[0];
        for (int i = 1; i < maskParts.length; i++) {
            result = result + "." + maskParts[i];
        }
        return result;
    }

    public static String calcSubnetAddress(String ip, String mask) {
        String result = "";
        try {
            // calc sub-net IP
            InetAddress ipAddress = InetAddress.getByName(ip);
            InetAddress maskAddress = InetAddress.getByName(mask);

            byte[] ipRaw = ipAddress.getAddress();
            byte[] maskRaw = maskAddress.getAddress();

            int unsignedByteFilter = 0x000000ff;
            int[] resultRaw = new int[ipRaw.length];
            for (int i = 0; i < resultRaw.length; i++) {
                resultRaw[i] = (ipRaw[i] & maskRaw[i] & unsignedByteFilter);
            }

            // make result string
            result = result + resultRaw[0];
            for (int i = 1; i < resultRaw.length; i++) {
                result = result + "." + resultRaw[i];
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 获取本机广播地址
     * <p>
     * 先从wifi里面获取，获取不到，再从本地网络配置表里获取，否则返回全域广播地址
     */
    public static InetAddress getBroadcastAddress(Context app) {
        InetAddress address = getWiFiBroadcastAddress(app);
        if (address == null || BROAD_IP_BOUND.equalsIgnoreCase(address.getHostAddress())) {
            address = getLocalBroadcastAddress();
            if (address == null) {
                address = getBoundBroadcast();
            }
        }
        return address;
    }

    /**
     * 获取本机Ipv4地址
     */
    public static String getLocalIpV4Address() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    /**
     * DatagramPacket 转字符串
     */
    public static String valueOf(DatagramPacket sendMsg) {
        if (sendMsg == null) {
            return "DatagramPacket is null";
        }
        return " ip " + sendMsg.getAddress().getHostAddress() + ":" + sendMsg.getPort() + " " + StrUtil.toString(sendMsg.getData());
    }

}