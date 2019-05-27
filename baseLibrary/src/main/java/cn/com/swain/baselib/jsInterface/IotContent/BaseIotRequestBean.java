package cn.com.swain.baselib.jsInterface.IotContent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Nullable
    @Override
    public String getRootJsonStr() {
        if (jsonStr == null) {
            if (jsonObj != null) {
                jsonStr = jsonObj.toString();
            }
        }
        return jsonStr;
    }

    @Override
    public void setRootJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    private JSONObject jsonObj;

    @NonNull
    @Override
    public JSONObject getRootJsonObj() {
        return jsonObj;
    }

    @Override
    public void setRootJsonObj(JSONObject jsonObj) {
        if (jsonObj == null) {
            jsonObj = new JSONObject();
        }
        this.jsonObj = jsonObj;
    }

    @Override
    public Object getObjByRoot(String key) {
        try {
            return jsonObj.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray getJSONArrayByRoot(String key) {
        try {
            return jsonObj.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getJSONObjectByRoot(String key) {
        try {
            return jsonObj.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getStringByRoot(String key) {
        try {
            return jsonObj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getIntByRoot(String key) {
        try {
            return jsonObj.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long getLongByRoot(String key) {
        try {
            return jsonObj.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public double getDoubleByRoot(String key) {
        try {
            return jsonObj.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean getBooleanByRoot(String key) {
        try {
            return jsonObj.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "BaseCommonJsRequestBean{" +
                "jsonStr='" + jsonStr + '\'' +
                ", jsonObj=" + jsonObj +
                '}';
    }
}
