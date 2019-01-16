package cn.com.swain.baselib.jsInterface.request.bean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 * <p>
 * js 请求实例：
 * {"msgType":"0x5221","data":3}
 */
public abstract class AbsBaseCommonJsRequestBean {

    /**
     * JS 请求的 msgType
     *
     * @return msgType
     */
    public abstract String getMsgType();

    /**
     * JS 请求的 msgType
     *
     * @param msgType msgType
     */
    public abstract void setMsgType(String msgType);


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
    public abstract Object getByRootJson(String key);


    /**
     * root JSONObject get
     *
     * @param key key
     * @return JSONObject
     */
    public abstract JSONArray getJSONArrayByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return JSONObject
     */
    public abstract JSONObject getJSONObjectByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return String
     */
    public abstract String getStringByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return int
     */
    public abstract int getIntByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return long
     */
    public abstract long getLongByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return double
     */
    public abstract double getDoubleByRootJson(String key);

    /**
     * root JSONObject get
     *
     * @param key key
     * @return boolean
     */
    public abstract boolean getBooleanByRootJson(String key);

}
