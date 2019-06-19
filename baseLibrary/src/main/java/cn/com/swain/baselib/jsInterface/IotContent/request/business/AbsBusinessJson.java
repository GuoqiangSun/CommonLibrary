package cn.com.swain.baselib.jsInterface.IotContent.request.business;

import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.BaseIotRequestBean;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public abstract class AbsBusinessJson extends BaseIotRequestBean {

    /**
     * 从json串获取custom
     */
    public abstract int getCustomByJson();

    /**
     * 从json串获取custom
     */
    public abstract int getCustomByJson(JSONObject businessJsonData);

    /**
     * 从json串获取product
     */
    public abstract int getProductByJson();

    /**
     * 从json串获取product
     */
    public abstract int getProductByJson(JSONObject businessJsonData);

    /**
     * 从json串获取cmd
     */
    public abstract long getCmdByJson();

    /**
     * 从json串获取cmd
     */
    public abstract long getCmdByJson(JSONObject businessJsonData);

    /**
     * 从json串获取result
     */
    public abstract int getResultByJson();

    /**
     * 从json串获取result
     */
    public abstract int getResultByJson(JSONObject businessJsonData);

}
