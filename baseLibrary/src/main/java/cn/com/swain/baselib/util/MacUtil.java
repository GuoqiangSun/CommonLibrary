package cn.com.swain.baselib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/6 0006
 * desc :
 */
public class MacUtil {

    private static final String MAC_ADDRESS = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

    private static Pattern MAC_COMPILE;

    public static boolean macMatches(String text) {
        // 这是真正的MAC地址；正则表达式;
        if (text != null && !text.isEmpty()) {

            if (MAC_COMPILE == null) {
                MAC_COMPILE = Pattern.compile(MAC_ADDRESS);
            }

            Matcher matcher = MAC_COMPILE.matcher(text);
            return matcher.matches();

        }
        return false;
    }


    public static String byteToMacStr(byte[] protocolParams, int start) {

        if (protocolParams == null || protocolParams.length < start + 6) {
            return "00:00:00:00:00:00";
        }

        StringBuilder sb = new StringBuilder(18);
        if ((protocolParams[start] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start] & 0xFF));
        sb.append(":");

        if ((protocolParams[start + 1] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 1] & 0xFF));
        sb.append(":");

        if ((protocolParams[start + 2] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 2] & 0xFF));
        sb.append(":");

        if ((protocolParams[start + 3] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 3] & 0xFF));
        sb.append(":");

        if ((protocolParams[start + 4] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 4] & 0xFF));
        sb.append(":");

        if ((protocolParams[start + 5] & 0xFF) <= 0x0F) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(protocolParams[start + 5] & 0xFF));
        return sb.toString().toUpperCase();

    }


}
