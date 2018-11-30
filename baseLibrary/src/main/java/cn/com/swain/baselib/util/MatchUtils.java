package cn.com.swain.baselib.util;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/27 0027
 * Desc:
 */
public class MatchUtils {

    public static final String REGEX_NUMBER_PWD = "^[0-9]{0,4}$";

    /**
     * 密码验证 正则表达式 （只能输入4位数字）
     */
    public static boolean matchNum4Pwd(String str) {
        if (str != null && !str.isEmpty()) {
            return str.matches(REGEX_NUMBER_PWD);
        }
        return false;
    }

    public static final String REGEX_NUM = "^[0-9]*$";

    /**
     * 只能输入数字
     */
    public static boolean matchNum(String str) {

        if (str != null && !str.isEmpty()) {
            return str.matches(REGEX_NUM);
        }
        return false;
    }

    public static final String REGEX_NUM_SYMBOL = "^[0-9+-]*$";

    /**
     * 匹配 数字 和 加减 符号
     */
    public static boolean matchNumSymbol(String str) {

        if (str != null && !str.isEmpty()) {
            return str.matches(REGEX_NUM_SYMBOL);
        }
        return false;
    }

    public static final String REGEX_WIFI_PWD = "[\\da-zA-Z_]{8}";

    /**
     * WiFi密码 验证
     */
    public static boolean matchWiFiPwd(String str) {

        if (str != null && !str.isEmpty()) {
            return str.matches(REGEX_WIFI_PWD);
        }
        return false;
    }

    public static final String REGEX_NAME = "[0-9a-zA-Z\u4e00-\u9fa5_][a-zA-Z0-9\u4e00-\u9fa5_]+";

    /**
     * 匹配用户名 （ 数字，字母，中文）
     */
    public static boolean matchName(String str) {
        if (str != null && !str.isEmpty()) {
            return str.matches(REGEX_NAME);
        }
        return false;
    }

    public static final String MAC_ADDRESS = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

    /**
     * 判断MAC地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean matchMac(String text) {
        if (text != null && !text.isEmpty()) {
            return text.matches(MAC_ADDRESS);
        }
        return false;
    }

    private static final String IP_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean matchIP(String text) {
        if (text != null && !text.isEmpty()) {
            return text.matches(IP_REGEX);
        }
        return false;
    }

}
