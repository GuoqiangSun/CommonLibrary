package cn.com.swain.baselib.jsInterface.bean;

import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public class BaseCommonJsRequestBeanWrapper extends AbsBaseCommonJsRequestBean {

    private final BaseCommonJsRequestBean mBean;

    public BaseCommonJsRequestBeanWrapper(BaseCommonJsRequestBean mBean) {
        this.mBean = mBean;
    }

    @Override
    public String getMsgType() {
        return mBean.getMsgType();
    }

    @Override
    public void setMsgType(String msgType) {
        this.mBean.setMsgType(msgType);
    }

    @Override
    public String getRootJsonStr() {
        return mBean.getRootJsonStr();
    }

    @Override
    public void setRootJsonStr(String jsonStr) {
        this.mBean.setRootJsonStr(jsonStr);
    }

    @Override
    public JSONObject getRootJsonObj() {
        return mBean.getRootJsonObj();
    }

    @Override
    public void setRootJsonObj(JSONObject jsonObj) {
        this.mBean.setRootJsonObj(jsonObj);
    }

    @Override
    public Object getByRootJson(String key) {
        return mBean.getByRootJson(key);
    }

    @Override
    public String getStringByRootJson(String key) {
        return mBean.getStringByRootJson(key);
    }
}
