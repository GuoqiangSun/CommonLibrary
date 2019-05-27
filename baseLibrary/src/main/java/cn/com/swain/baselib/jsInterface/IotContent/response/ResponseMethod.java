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

    public String toJsMethod(JSONObject content) {
        String data = toControlJsonStr(content);
        return toJsMethod(data);
    }

    public String toJsMethod() {
        String data = toControlJsonStr();
        return toJsMethod(data);
    }

    public String toJsMethod(String mJsonData) {
        return ResponseMethodUtil.toJsMethod(mJsonData);
    }
}
