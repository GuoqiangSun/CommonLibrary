package cn.com.swain.baselib.jsInterface.IotContent.request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.BusinessContent;
import cn.com.swain.baselib.jsInterface.IotContent.ControlContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.AbsBusinessJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.AbsControlJson;


/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class DataContentWrapper extends AbsDataContent {

    private final AbsDataContent mDataContent;

    public DataContentWrapper(AbsDataContent mDataContent) {
        this.mDataContent = mDataContent;
    }


    @Override
    public JSONObject getBusinessJSON() {
        return mDataContent.getBusinessJSON();
    }


    @Override
    public AbsBusinessJson takeBusinessBean() {
        return mDataContent.takeBusinessBean();
    }

    @Override
    public void parseJson(JSONObject jsonObj) {
        mDataContent.parseJson(jsonObj);
    }

    @Override
    public void parseJsonStr(String jsonStr) throws JSONException {
        mDataContent.parseJsonStr(jsonStr);
    }

    @Override
    public AbsBusinessJson getBusinessJsonBean() {
        return mDataContent.getBusinessJsonBean();
    }

    @Override
    public AbsControlJson getControlJsonBean() {
        return mDataContent.getControlJsonBean();
    }

    @Override
    public JSONObject getControlJSON() {
        return mDataContent.getControlJSON();
    }


    @Override
    public AbsControlJson takeControlBean() {
        return mDataContent.takeControlBean();
    }

    @Override
    public ControlContent copyControlDataFromJson() {
        return mDataContent.copyControlDataFromJson();
    }

    @Override
    public void copyControlDataFromJson(ControlContent mControlContent) {
         mDataContent.copyControlDataFromJson(mControlContent);
    }

    @Override
    public BusinessContent copyBusinessDataFromJson() {
        return mDataContent.copyBusinessDataFromJson();
    }

    @Override
    public void copyBusinessDataFromJson(BusinessContent mBusinessContent) {
         mDataContent.copyBusinessDataFromJson(mBusinessContent);
    }
}
