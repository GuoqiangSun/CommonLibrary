package cn.com.swain.baselib.jsInterface.IotContent.request;

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

    public DataContent() {
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

    private final AbsBusinessJson mAbsBusinessJson = new BusinessJsonBean();

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

    @Override
    public AbsBusinessJson getBusinessJsonBean() {
        return mAbsBusinessJson;
    }

    private final AbsControlJson mAbsControlJson = new ControlJsonBean();

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
        ControlContent mControlContent = new ControlContent();
        AbsControlJson controlBean = takeControlBean();
        mControlContent.setVer(controlBean.getVerByJson());
        mControlContent.setTs(controlBean.getTsByJson());
        mControlContent.setFrom(controlBean.getFromByJson());
        mControlContent.setTo(controlBean.getToByJson());
        mControlContent.setSession(controlBean.getSessionByJson());
        mControlContent.setAppid(controlBean.getAppidByJson());
        mControlContent.setMsgtw(controlBean.getMsgtwByJson());
        return mControlContent;
    }

    @Override
    public ControlContent copyControlDataFromJson(ControlContent mControlContent) {
        AbsControlJson controlBean = takeControlBean();
        mControlContent.setVer(controlBean.getVerByJson());
        mControlContent.setTs(controlBean.getTsByJson());
        mControlContent.setFrom(controlBean.getFromByJson());
        mControlContent.setTo(controlBean.getToByJson());
        mControlContent.setSession(controlBean.getSessionByJson());
        mControlContent.setAppid(controlBean.getAppidByJson());
        mControlContent.setMsgtw(controlBean.getMsgtwByJson());
        return mControlContent;
    }

    /**
     * 从json串获取业务消息并copy
     */
    @Override
    public BusinessContent copyBusinessDataFromJson() {
        BusinessContent mBusinessContent = new BusinessContent();
        AbsBusinessJson businessBean = takeBusinessBean();
        mBusinessContent.setCustom(businessBean.getCustomByJson());
        mBusinessContent.setProduct(businessBean.getProductByJson());
        mBusinessContent.setCmd(businessBean.getCmdByJson());
        return mBusinessContent;
    }

    @Override
    public BusinessContent copyBusinessDataFromJson(BusinessContent mBusinessContent) {
        AbsBusinessJson businessBean = takeBusinessBean();
        mBusinessContent.setCustom(businessBean.getCustomByJson());
        mBusinessContent.setProduct(businessBean.getProductByJson());
        mBusinessContent.setCmd(businessBean.getCmdByJson());
        return mBusinessContent;
    }


}
