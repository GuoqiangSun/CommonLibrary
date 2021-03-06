package cn.com.swain.baselib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/14 0014
 * desc :
 */
public class StrUtil {


    private static final String NAME_PATTERN_STR = "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
//    private static final String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";


    private static Pattern PATTERN_NAME;


    /**
     * 判断是否含有特殊字符
     *
     * @param str 字符串
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialName(String str) {
        if (str != null && !str.isEmpty()) {
            if (PATTERN_NAME == null) {
                PATTERN_NAME = Pattern.compile(NAME_PATTERN_STR);
            }
            Matcher m = PATTERN_NAME.matcher(str);
            return m.find();
        }
        return false;
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean isBlank(String str) {
        return str == null || str.length() <= 0;
    }

    /**
     * 数组转字符串显示
     */
    public static String toString(byte[] a) {
        return toHexString(a);
    }

    /**
     * 数组转十六进制字符串显示
     */
    public static String toHexString(byte[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(Integer.toHexString(a[i] & 0xFF));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    /**
     * 数组转字符显示
     */
    public static String toCharString(byte[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf((char) a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    /**
     * "ff aa bb ee" 转 0xff 0xaa 0xbb 0xee
     */
    public static byte[] toHex(String a) {

        if (a == null || a.length() <= 0) {
            return new byte[1];
        }

        String[] split = a.split(" ");
        int length = split.length;
        if (length <= 0) {
            length = 1;
        }
        byte[] buf = new byte[length];
        for (int i = 0; i < length; i++) {
            String s = split[i];
            buf[i] = (byte) Integer.parseInt(s, 16);
//            buf[i] = Byte.parseByte(s,16);
        }
        return buf;

    }

    /**
     * "ffaabbee" 转 0xff 0xaa 0xbb 0xee
     */
    public static byte[] splitHexStr(String a) {

        if (a == null || a.length() <= 0) {
            return new byte[1];
        }

        int length = a.length();
        int bufLength;
        if (length % 2 == 0) {
            bufLength = length / 2;
        } else {
            bufLength = length / 2 + 1;
        }
        byte[] buf = new byte[bufLength];

        for (int i = 0; i < bufLength; i++) {
            String substring;
            if (i == (bufLength - 1) && (i * 2 + 2 > length)) {
                substring = a.substring(i * 2);
            } else {
                substring = a.substring(i * 2, i * 2 + 2);
            }
            buf[i] = (byte) Integer.parseInt(substring, 16);
        }

        return buf;
    }


    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return "";
        }

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0;
             j < bytes.length;
             j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];

            hexChars[j * 2 + 1] = hexArray[v & 0x0F];

        }

        return new String(hexChars);
    }


}
