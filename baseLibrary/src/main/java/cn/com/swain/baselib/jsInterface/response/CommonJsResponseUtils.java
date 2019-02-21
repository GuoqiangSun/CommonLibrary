package cn.com.swain.baselib.jsInterface.response;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class CommonJsResponseUtils {


    public CommonJsResponseUtils() {
    }

    private static final String METHOD_RESPONSE = "javascript:dataInteractionResponse('$data')";

    public String getResponseJsMethod(String data) {
        return METHOD_RESPONSE.replace("$data", checkData(data));
    }

    private static String checkData(String data) {
        return data != null ? data : "{}";
    }

}
