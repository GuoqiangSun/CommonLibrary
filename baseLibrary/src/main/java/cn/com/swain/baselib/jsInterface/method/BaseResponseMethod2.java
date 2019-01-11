package cn.com.swain.baselib.jsInterface.method;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/28 0028
 * desc :
 */
public class BaseResponseMethod2 extends BaseResponseMethod {

    public BaseResponseMethod2(String msgType) {
        super(msgType);
    }

    @Override
    public void releaseCache() {
        super.releaseCache();
        setResult(false);
        setErrorCode(null);
    }

    private boolean result;

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    private String errorCode;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String toMethod(JSONObject data) {

        JSONObject jsonObject = mBean.toJsonObj();

        try {
            jsonObject.put("result", result);

            if (!result) {
                if (data == null) {
                    data = new JSONObject();
                }
                data.put("errorCode", errorCode);
            }

            jsonObject.put("data", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mResponse.getResponseJsMethod(jsonObject.toString());

    }
}
