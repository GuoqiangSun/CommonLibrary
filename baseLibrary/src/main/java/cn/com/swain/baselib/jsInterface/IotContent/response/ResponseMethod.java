package cn.com.swain.baselib.jsInterface.IotContent.response;

import org.json.JSONObject;

/**
 * author Guoqiang_Sun
 * date 2019/5/17
 * desc 响应js数据结构
 */
public class ResponseMethod extends BusinessResponse {

    public ResponseMethod() {
        super();
    }

    private static final String METHOD_RESPONSE = "javascript:dataInteractionResponse('$data')";

    public String toJsMethod(JSONObject content) {
        String data = toControlJsonStr(content);
        return toJsMethod(data);
    }

    public String toJsMethod() {
        String data = toControlJsonStr();
        return toJsMethod(data);
    }

    public String toJsMethod(String data) {
        return METHOD_RESPONSE.replace("$data", checkData(data));
    }

    private static String checkData(String data) {
        return data != null ? data : "{}";
    }

}
