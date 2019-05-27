package cn.com.swain.baselib.jsInterface.IotContent.response;

/**
 * author Guoqiang_Sun
 * date 2019/5/27
 * desc
 */
public class ResponseMethodUtil {

    private static final String METHOD_RESPONSE = "javascript:dataInteractionResponse('$data')";

    public static String toJsMethod(String JsonData) {
        return METHOD_RESPONSE.replace("$data", checkData(JsonData));
    }

    private static String checkData(String data) {
        return data != null ? data : "{}";
    }
}
