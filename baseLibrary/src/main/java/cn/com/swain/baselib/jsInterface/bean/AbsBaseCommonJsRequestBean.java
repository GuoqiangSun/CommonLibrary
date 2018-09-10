package cn.com.swain.baselib.jsInterface.bean;

import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public abstract class AbsBaseCommonJsRequestBean {


    public abstract String getMsgType();

    public abstract void setMsgType(String msgType);


    public abstract String getRootJsonStr();

    public abstract void setRootJsonStr(String jsonStr);


    public abstract JSONObject getRootJsonObj();

    public abstract void setRootJsonObj(JSONObject jsonObj);


    public abstract Object getByRootJson(String key);

    public abstract String getStringByRootJson(String key);

}
