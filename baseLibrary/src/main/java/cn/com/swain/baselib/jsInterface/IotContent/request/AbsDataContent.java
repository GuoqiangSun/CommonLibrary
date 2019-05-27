package cn.com.swain.baselib.jsInterface.IotContent.request;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.BaseIotRequestBean;
import cn.com.swain.baselib.jsInterface.IotContent.BusinessContent;
import cn.com.swain.baselib.jsInterface.IotContent.ControlContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.AbsBusinessJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.AbsControlJson;

/**
 * author Guoqiang_Sun
 * date 2019/5/16
 * desc
 */
public abstract class AbsDataContent extends BaseIotRequestBean {

    public abstract void parseJson(JSONObject jsonObj);

    public abstract void parseJsonStr(String jsonStr) throws JSONException;

    public abstract AbsBusinessJson getBusinessJsonBean();

    public abstract AbsControlJson getControlJsonBean();


    /**
     * 从json串获取控制层JsonData
     */
    public abstract JSONObject getControlJSON();

    /**
     * 从json串获取业务JsonData
     */
    public abstract JSONObject getBusinessJSON();


    /**
     * 从json串获取业务AbsBusinessBean
     */
    public abstract AbsBusinessJson takeBusinessBean();


    /**
     * 从json串获取控制层AbsBusinessBean
     */
    public abstract AbsControlJson takeControlBean();


    /**
     * 从json串获取控制消息并返回
     */
    public abstract ControlContent copyControlDataFromJson();


    /**
     * 从json串获取控制消息并返回
     */
    public abstract void copyControlDataFromJson(ControlContent mControlContent);

    /**
     * 从json串获取业务消息并copy
     */
    public abstract BusinessContent copyBusinessDataFromJson();

    /**
     * 从json串获取控制消息并返回
     */
    public abstract void copyBusinessDataFromJson(BusinessContent mBusinessContent);

}
