package cn.com.common.baselib.jsInterface.bean;

import org.json.JSONObject;

import cn.com.common.baselib.jsInterface.bean.BaseCommonJsRequestBean;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public class BaseCommonJsRequestBeanWrapper extends BaseCommonJsRequestBean {

    private final BaseCommonJsRequestBean mBean;

    public BaseCommonJsRequestBeanWrapper(BaseCommonJsRequestBean mBean) {
        this.mBean = mBean;
    }

    public String getMsgType() {
        return mBean.getMsgType();
    }

    public void setMsgType(String msgType) {
        this.mBean.setMsgType(msgType);
    }

    public String getJsonStr() {
        return mBean.getJsonStr();
    }

    public void setJsonStr(String jsonStr) {
        this.mBean.setJsonStr(jsonStr);
    }


    public JSONObject getJsonObj() {
        return mBean.getJsonObj();
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.mBean.setJsonObj(jsonObj);
    }

}
