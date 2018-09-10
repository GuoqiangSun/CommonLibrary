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
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialName(String str) {

        if (PATTERN_NAME == null) {
            PATTERN_NAME = Pattern.compile(NAME_PATTERN_STR);
        }

        Matcher m = PATTERN_NAME.matcher(str);
        return m.find();
    }

    public static String toString(byte[] a) {
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
            buf[i] = (byte) Integer.parseInt(s,16);
//            buf[i] = Byte.parseByte(s,16);
        }
        return buf;

    }

}
