package cn.com.swain.baselib.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipMatches(String text) {
        if (text != null && !text.isEmpty()) {
            return text.matches(IP_REGEX);
        }
        return false;
    }


    public static final String BROAD_IP_BOUND = "255.255.255.255";

    public static InetAddress getBoundBroadcast() {

        try {
            return InetAddress.getByName(BROAD_IP_BOUND);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }


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
                        System.out.println("address        =   " + hostAddress);

                        if (hostAddress.indexOf(":") > 0) {
                            // case : ipv6
                            continue;
                        } else {
                            // case : ipv4
                            String maskAddress = calcMaskByPrefixLength(networkCardAddress.getNetworkPrefixLength());
                            String subnetAddress = calcSubnetAddress(hostAddress, maskAddress);
                            String broadcastAddress = networkCardAddress.getBroadcast().getHostAddress();

                            System.out.println("subnetmask     =   " + maskAddress);
                            System.out.println("subnet         =   " + subnetAddress);
                            System.out.println("broadcast      =   " + broadcastAddress + "\n");

                            try {
                                return InetAddress.getByName(broadcastAddress);
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    } else {
                        String loopback = networkCardAddress.getAddress().getHostAddress();
                        System.out.println("loopback addr  =   " + loopback + "\n");
                    }
                }
                System.out.println("----- NetworkInterface  Separator ----\n\n");

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

}