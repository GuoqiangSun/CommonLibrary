package cn.com.common.baselib.jsInterface.method;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public class JSErrorCode {

    /**
     * 二维码错误 未知
     */
    public static final String ERROR_CODE_SCAN_UNKNOWN = "0xAA0100";
    /**
     * 用户取消扫描
     */
    public static final String ERROR_CODE_SCAN_CANCEL = "0xAA0101";
    /**
     * 扫描到的二维码是格式不对
     */
    public static final String ERROR_CODE_ERROR_URL = "0xAA0102";


    /**
     * wx登录错误 unknown
     */
    public static final String ERROR_CODE_WX_LOGIN_UNKNOWN = "0x830399";

    /**
     * wx登录没有客户端
     */
    public static final String ERROR_CODE_WX_LOGIN_NO_CLIENT = "0x830398";

    /**
     * wx登录用户取消
     */
    public static final String ERROR_CODE_WX_LOGIN_USER_CANCEL = "0x830397";

    /**
     * wx登录用户拒接
     */
    public static final String ERROR_CODE_WX_LOGIN_USER_REJECTION = "0x830396";

}
