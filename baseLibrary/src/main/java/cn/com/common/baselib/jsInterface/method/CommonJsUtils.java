package cn.com.common.baselib.jsInterface.method;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class CommonJsUtils {

    public static final String KEY_MSG_TYPE = "msgType";

    /**
     * 物理按键的响应
     */
    public static final String TYPE_RESPONSE_BACK = "0x5001";
    /**
     * 物理按键的请求
     */
    public static final String TYPE_REQUEST_BACK = "0x5002";

    /**
     * 捕获错误到后台请求
     */
    public static final String TYPE_REQUEST_JS_ERROR = "0x5011";

    /**
     * 检验是否登录请求
     */
    public static final String TYPE_REQUEST_IS_LOGIN = "0x5101";

    /**
     * 检验是否登录录返回
     */
    public static final String TYPE_RESPONSE_IS_LOGIN = "0x5102";


    /**
     * 微信登录请求
     */
    public static final String TYPE_REQUEST_WX_LOGIN = "0x5103";

    /**
     * 微信登录返回
     */
    public static final String TYPE_RESPONSE_WX_LOGIN = "0x5104";


    /**
     * 调用摄像头扫描二维码请求
     */
    public static final String TYPE_REQUEST_SCAN_OR = "0x5201";

    /**
     * 扫描二维码数据返回
     */
    public static final String TYPE_RESPONSE_SCAN_OR = "0x5202";

    /**
     * 查询设备是否在线请求
     */
    public static final String TYPE_REQUEST_QUERY_DEVICE_INFO = "0x5203";

    /**
     * 查询设备是否在线返回
     */
    public static final String TYPE_RESPONSE_QUERY_DEVICE_INFO = "0x5204";

    /**
     * 借出充电宝请求
     */
    public static final String TYPE_REQUEST_BORROW_DEVICE = "0x5205";

    /**
     * 借出充电宝返回 0x5206
     */
    public static final String TYPE_RESPONSE_BORROW_DEVICE = "0x5206";

    /**
     * 归还充电宝请求
     */
    public static final String TYPE_REQUEST_GIVEBACK_DEVICE = "0x5211";

    /**
     * 归还充电宝返回
     */
    public static final String TYPE_RESPONSE_GIVEBACK_DEVICE = "0x5212";
}
