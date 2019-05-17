package cn.com.swain.baselib.jsInterface.IotContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class BaseIotRequestBean extends AbsIotRequestBean {

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
    public Object getObjByRoot(String key) {
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
    public JSONArray getJSONArrayByRoot(String key) {
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
    public JSONObject getJSONObjectByRoot(String key) {
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
    public String getStringByRoot(String key) {
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
    public int getIntByRoot(String key) {
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
    public long getLongByRoot(String key) {
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
    public double getDoubleByRoot(String key) {
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
    public boolean getBooleanByRoot(String key) {
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
                ", jsonStr='" + jsonStr + '\'' +
                ", jsonObj=" + jsonObj +
                '}';
    }
}
