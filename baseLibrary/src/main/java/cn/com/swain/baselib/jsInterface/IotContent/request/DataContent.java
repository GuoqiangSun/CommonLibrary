package cn.com.swain.baselib.jsInterface.IotContent.request;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.BusinessContent;
import cn.com.swain.baselib.jsInterface.IotContent.ControlContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.AbsBusinessJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.BusinessJsonBean;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.AbsControlJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.ControlJsonBean;


/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class DataContent extends AbsDataContent {

    private final AbsBusinessJson mAbsBusinessJson;

    private final AbsControlJson mAbsControlJson;

    public DataContent() {
        mAbsBusinessJson = new BusinessJsonBean();
        mAbsControlJson = new ControlJsonBean();
    }

    @Override
    public void setRootJsonObj(JSONObject jsonObj) {
        super.setRootJsonObj(jsonObj);
        mAbsControlJson.setRootJsonObj(getRootJsonObj());
        mAbsBusinessJson.setRootJsonObj(getBusinessJSON());
    }

    @Override
    public void setRootJsonStr(String jsonStr) {
        super.setRootJsonStr(jsonStr);
        mAbsControlJson.setRootJsonStr(jsonStr);
    }

    @Override
    public void parseJson(JSONObject jsonObj) {
        setRootJsonObj(jsonObj);
    }

    @Override
    public void parseJsonStr(String jsonStr) throws JSONException {
        JSONObject obj = new JSONObject(jsonStr);
        setRootJsonObj(obj);
        setRootJsonStr(jsonStr);
    }

    public static DataContent newDataContent(JSONObject jsonObj) {
        DataContent dataContent = new DataContent();
        dataContent.parseJson(jsonObj);
        return dataContent;
    }

    public static DataContent newDataContent(String jsonStr) throws JSONException {
        DataContent dataContent = new DataContent();
        dataContent.parseJsonStr(jsonStr);
        return dataContent;
    }

    @NonNull
    @Override
    public AbsBusinessJson getBusinessJsonBean() {
        return mAbsBusinessJson;
    }

    @NonNull
    @Override
    public AbsControlJson getControlJsonBean() {
        return mAbsControlJson;
    }

    @Override
    public JSONObject getControlJSON() {
        return getRootJsonObj();
    }


    /**
     * 从json串获取业务JsonData
     */
    @Override
    public JSONObject getBusinessJSON() {
        try {
            return getControlJSON().getJSONObject("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public AbsControlJson takeControlBean() {
        final AbsControlJson mControlBean = new ControlJsonBean();
        mControlBean.setRootJsonObj(getControlJSON());
        mControlBean.setRootJsonStr(getRootJsonStr());
        return mControlBean;
    }

    @Override
    public AbsBusinessJson takeBusinessBean() {
        final BusinessJsonBean businessBean = new BusinessJsonBean();
        businessBean.setRootJsonObj(getBusinessJSON());
        return businessBean;
    }


    /**
     * 从json串获取控制消息并返回
     */
    @Override
    public ControlContent copyControlDataFromJson() {
        final ControlContent mControlContent = new ControlContent();
        copyControlDataFromJson(mControlContent);
        return mControlContent;
    }

    @Override
    public void copyControlDataFromJson(ControlContent mControlContent) {
        AbsControlJson controlBean = takeControlBean();
        mControlContent.setVer(controlBean.getVerByJson());
        mControlContent.setTs(controlBean.getTsByJson());
        mControlContent.setFrom(controlBean.getFromByJson());
        mControlContent.setTo(controlBean.getToByJson());
        mControlContent.setSession(controlBean.getSessionByJson());
        mControlContent.setAppid(controlBean.getAppidByJson());
        mControlContent.setMsgtw(controlBean.getMsgtwByJson());
    }

    /**
     * 从json串获取业务消息并copy
     */
    @Override
    public BusinessContent copyBusinessDataFromJson() {
        final BusinessContent mBusinessContent = new BusinessContent();
        copyBusinessDataFromJson(mBusinessContent);
        return mBusinessContent;
    }

    @Override
    public void copyBusinessDataFromJson(BusinessContent mBusinessContent) {
        AbsBusinessJson businessBean = takeBusinessBean();
        mBusinessContent.setCustom(businessBean.getCustomByJson());
        mBusinessContent.setProduct(businessBean.getProductByJson());
        mBusinessContent.setCmd(businessBean.getCmdByJson());
    }


}
