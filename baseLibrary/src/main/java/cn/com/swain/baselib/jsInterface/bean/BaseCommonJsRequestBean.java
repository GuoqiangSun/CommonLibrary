package cn.com.swain.baselib.jsInterface.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class BaseCommonJsRequestBean extends AbsBaseCommonJsRequestBean {

    private String msgType;

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    private String jsonStr;

    @Override
    public String getRootJsonStr() {
        return jsonStr;
    }

    @Override
    public void setRootJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    private JSONObject jsonObj;

    @Override
    public JSONObject getRootJsonObj() {
        return jsonObj;
    }

    @Override
    public void setRootJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public Object getByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getStringByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BaseCommonJsRequestBean{" +
                "msgType='" + msgType + '\'' +
                ", jsonStr='" + jsonStr + '\'' +
                ", jsonObj=" + jsonObj +
                '}';
    }
}
