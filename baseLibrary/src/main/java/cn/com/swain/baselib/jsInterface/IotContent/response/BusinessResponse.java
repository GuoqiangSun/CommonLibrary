package cn.com.swain.baselib.jsInterface.IotContent.response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.BusinessContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.DataContent;

/**
 * author Guoqiang_Sun
 * date 2019/5/17
 * desc 业务层数据
 */
public class BusinessResponse extends ControlResponse {

    private final BusinessContent mBusinessContent;

    public BusinessResponse() {
        super();
        this.mBusinessContent = new BusinessContent();
    }

    /**
     * 获取缓存BusinessContent
     */
    public BusinessContent getBusinessContent() {
        return mBusinessContent;
    }

    /**
     * 拷贝 BusinessContent 内容
     */
    public void memorBusinessContent(BusinessContent tBusinessContent) {
        if (tBusinessContent == null) {
            throw new NullPointerException("BusinessContent must not be null");
        }
        mBusinessContent.setCustom(tBusinessContent.getCustom());
        mBusinessContent.setProduct(tBusinessContent.getProduct());
        mBusinessContent.setCmd(tBusinessContent.getCmd());
        mBusinessContent.setResult(tBusinessContent.getResult());
    }

    /**
     * 拷贝 ControlContent 内容
     */
    public void memorControlContent(DataContent dataContent) {
        if (dataContent == null) {
            throw new NullPointerException("DataContent must not be null");
        }
        dataContent.copyBusinessDataFromJson(mBusinessContent);
    }

    /**
     * 单单业务层的json数据对象
     *
     * @return Json字符串
     */
    public JSONObject toBusinessJsonObj() {
        JSONObject content = new JSONObject();
        try {
            content.put("custom", mBusinessContent.getCustom());
            content.put("product", mBusinessContent.getProduct());
            content.put("cmd", mBusinessContent.getCustom());
            content.put("result", mBusinessContent.getResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;

    }

    /**
     * 单单业务层的json数据字符串
     *
     * @return Json字符串
     */
    public String toBusinessJsonObjStr() {
        return toBusinessJsonObj().toString();
    }

    /**
     * 将业务层的数据对象塞到控制层中去，并返回json对象
     */
    public JSONObject toControlJsonObj() {
        return super.toControlJsonObj(toBusinessJsonObj());
    }

    /**
     * 将业务层的数据对象塞到控制层中去，并返回json字符串
     */
    public String toControlJsonStr() {
        return toControlJsonObj().toString();
    }


}
