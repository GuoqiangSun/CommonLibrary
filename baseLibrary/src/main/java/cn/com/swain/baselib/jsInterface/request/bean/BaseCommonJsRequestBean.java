package cn.com.swain.baselib.jsInterface.request.bean;

import org.json.JSONArray;
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
    public JSONArray getJSONArrayByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getJSONArray(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JSONObject getJSONObjectByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getJSONObject(key);
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
    public int getIntByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public long getLongByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getLong(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public double getDoubleByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean getBooleanByRootJson(String key) {
        if (jsonObj != null) {
            try {
                return jsonObj.getBoolean(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
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
