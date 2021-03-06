package cn.com.swain.baselib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/6 0006
 * desc :
 */
public class MacUtil {

    private static final String MAC_ADDRESS = MatchUtils.MAC_ADDRESS;

    private static Pattern MAC_COMPILE;

    /**
     * 判断MAC地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean macMatches(String text) {
        if (text != null && !text.isEmpty()) {

            if (MAC_COMPILE == null) {
                MAC_COMPILE = Pattern.compile(MAC_ADDRESS);
            }

            Matcher matcher = MAC_COMPILE.matcher(text);
            return matcher.matches();

        }
        return false;
    }

    public static boolean macLenAvaliable(byte[] protocolParams, int start) {
        return protocolParams != null
                && protocolParams.length >= 6
                && protocolParams.length > start + 5;
    }

    public static String byteToMacStr(byte[] protocolParams, int start) {

        if (protocolParams == null || protocolParams.length <= start) {
            return "00:00:00:00:00:00";
        }

        StringBuilder sb = new StringBuilder(18);
        if ((protocolParams[start] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start] & 0xFF));
        sb.append(":");


        if (protocolParams.length <= start + 1) {
            sb.append("00:00:00:00:00");
            return sb.toString().toUpperCase();
        }
        if ((protocolParams[start + 1] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 1] & 0xFF));
        sb.append(":");


        if (protocolParams.length <= start + 2) {
            sb.append("00:00:00:00");
            return sb.toString().toUpperCase();
        }
        if ((protocolParams[start + 2] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 2] & 0xFF));
        sb.append(":");


        if (protocolParams.length <= start + 3) {
            sb.append("00:00:00");
            return sb.toString().toUpperCase();
        }
        if ((protocolParams[start + 3] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 3] & 0xFF));
        sb.append(":");


        if (protocolParams.length <= start + 4) {
            sb.append("00:00");
            return sb.toString().toUpperCase();
        }
        if ((protocolParams[start + 4] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 4] & 0xFF));
        sb.append(":");


        if (protocolParams.length <= start + 5) {
            sb.append("00");
            return sb.toString().toUpperCase();
        }
        if ((protocolParams[start + 5] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 5] & 0xFF));
        return sb.toString().toUpperCase();

    }

    public static void main(String[] args) {
        byte[] a = new byte[6];
        a[0] = 1;
        a[1] = 2;
        a[2] = 3;
        a[3] = 4;
        a[4] = 5;
        a[5] = 6;
        String s = byteToMacStr(a, 0);
        System.out.println("mac:" + s);
    }


}
