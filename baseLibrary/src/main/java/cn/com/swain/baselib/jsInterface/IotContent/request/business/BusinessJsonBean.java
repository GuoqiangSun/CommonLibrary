package cn.com.swain.baselib.jsInterface.IotContent.request.business;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public class BusinessJsonBean extends AbsBusinessJson {

    /**
     * 从json串获取custom
     */
    @Override
    public int getCustomByJson() {
        return getIntByRoot("custom");
    }

    /**
     * 从json串获取custom
     */
    @Override
    public int getCustomByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "custom");
    }

    /**
     * 从json串获取product
     */
    @Override
    public int getProductByJson() {
        return getIntByRoot("product");
    }

    /**
     * 从json串获取product
     */
    @Override
    public int getProductByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "product");
    }

    /**
     * 从json串获取cmd
     */
    @Override
    public int getCmdByJson() {
        return getIntByRoot("cmd");
    }

    /**
     * 从json串获取cmd
     */
    @Override
    public int getCmdByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "cmd");
    }

    /**
     * 从json串获取result
     */
    @Override
    public int getResultByJson() {
        return getIntByRoot("result");
    }

    /**
     * 从json串获取result
     */
    @Override
    public int getResultByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "result");
    }


    private int getInt(JSONObject object, String key) {
        if (object == null) {
            return 0;
        }
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
