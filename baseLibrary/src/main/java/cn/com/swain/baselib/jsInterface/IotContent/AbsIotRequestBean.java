package cn.com.swain.baselib.jsInterface.IotContent;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public abstract class AbsIotRequestBean {

    /**
     * JS 请求的 JSON字符串
     *
     * @return JSON字符串
     */
    public abstract String getRootJsonStr();

    /**
     * JS 请求的 JSON字符串
     *
     * @param jsonStr JSON字符串
     */
    public abstract void setRootJsonStr(String jsonStr);


    /**
     * JS 请求的 JSONObject
     *
     * @return {@link JSONObject}
     */
    public abstract JSONObject getRootJsonObj();


    /**
     * JS 请求的 JSONObject
     *
     * @param jsonObj {@link JSONObject}
     */
    public abstract void setRootJsonObj(JSONObject jsonObj);


    /**
     * root JSONObject get
     *
     * @param key key
     * @return Object
     */
    public abstract Object getObjByRoot(String key);


    /**
     * root JSONObject get
     *
     * @param key key
     * @return JSONObject
     */
    public abstract JSONArray getJSONArrayByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return JSONObject
     */
    public abstract JSONObject getJSONObjectByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return String
     */
    public abstract String getStringByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return int
     */
    public abstract int getIntByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return long
     */
    public abstract long getLongByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return double
     */
    public abstract double getDoubleByRoot(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return boolean
     */
    public abstract boolean getBooleanByRoot(String key);

}
