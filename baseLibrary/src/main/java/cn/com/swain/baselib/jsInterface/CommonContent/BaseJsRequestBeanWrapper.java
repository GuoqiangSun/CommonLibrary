package cn.com.swain.baselib.jsInterface.CommonContent;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/29 0029
 * desc :
 */
public class BaseJsRequestBeanWrapper extends AbsJsRequestBean {

    private final BaseJsRequestBean mBean;

    public BaseJsRequestBeanWrapper(BaseJsRequestBean mBean) {
        this.mBean = mBean;
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
    public Object getObjByRoot(String key) {
        return mBean.getObjByRoot(key);
    }

    @Override
    public JSONArray getJSONArrayByRoot(String key) {
        return mBean.getJSONArrayByRoot(key);
    }

    @Override
    public JSONObject getJSONObjectByRoot(String key) {
        return mBean.getJSONObjectByRoot(key);
    }

    @Override
    public String getStringByRoot(String key) {
        return mBean.getStringByRoot(key);
    }

    @Override
    public int getIntByRoot(String key) {
        return mBean.getIntByRoot(key);
    }

    @Override
    public long getLongByRoot(String key) {
        return mBean.getLongByRoot(key);
    }

    @Override
    public double getDoubleByRoot(String key) {
        return mBean.getDoubleByRoot(key);
    }

    @Override
    public boolean getBooleanByRoot(String key) {
        return mBean.getBooleanByRoot(key);
    }
}
