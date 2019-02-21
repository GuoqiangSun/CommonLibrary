package cn.com.swain.baselib.jsInterface.response;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * author: Guoqiang_Sun
 * date : 2018/8/28 0028
 * desc :
 */
public class BaseCommonJsResponseBean {

    public BaseCommonJsResponseBean() {
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    private String msgType;

    public JSONObject toJsonObj() {

        JSONObject mObj = new JSONObject();

        try {
            mObj.put(BaseCommonJsUtils.KEY_MSG_TYPE, getMsgType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mObj;

    }

    public String toJsonStr() {
        return toJsonObj().toString();
    }

}
