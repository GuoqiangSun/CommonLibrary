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

    public static final String IP_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
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

    /**
     * 公认端口（Well Known Ports）：从0到1023，
     * 它们紧密绑定（binding）于一些服务。
     * 通常这些端口的通讯明确表明了某种服务的协议。
     * 例如：80端口实际上总是HTTP通讯。
     */
    public static boolean isWellKnownPort(int port) {
        return IpUtil.isWellKnownPort(port);
    }

    /**
     * 注册端口（Registered Ports）：从1024到49151。
     * 它们松散地绑定于一些服务。
     * 也就是说有许多服务绑定于这些端口，这些端口同样用于许多其它目的。
     * 例如：许多系统处理动态端口从1024左右开始。
     */
    public static boolean isRegisteredPort(int port) {
        return IpUtil.isRegisteredPort(port);
    }

    /**
     * 动态和/或私有端口（Dynamic and/or Private Ports）：从49152到65535。
     * 理论上，不应为服务分配这些端口。实际上，机器通常从1024起分配动态端口
     * 。但也有例外：SUN的RPC端口从32768开始。
     */
    public static boolean isPrivatePort(int port) {
        return IpUtil.isPrivatePort(port);
    }

    /**
     * 有效端口： 从0到65535.
     */
    public static boolean isAvailablePort(int port) {
        return IpUtil.isAvailablePort(port);
    }

    public static final String CHAR_PATTERN_STR =
            "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
//    private static final String regEx =
//    "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";


    /**
     * 判断是否含有特殊字符
     *
     * @param text 字符串
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialStr(String text) {
        if (text != null && !text.isEmpty()) {
            return text.matches(CHAR_PATTERN_STR);
        }
        return false;
    }

}
