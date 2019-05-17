package cn.com.swain.baselib.jsInterface.IotContent.response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.ControlContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.DataContent;

/**
 * author Guoqiang_Sun
 * date 2019/5/17
 * desc 控制层数据
 */
public class ControlResponse {

    private final ControlContent mControlContent;

    public ControlResponse() {
        mControlContent = new ControlContent();
    }

    /**
     * 获取缓存ControlContent
     */
    public ControlContent getControlContent() {
        return mControlContent;
    }

    /**
     * 拷贝 ControlContent 内容
     */
    public void memorControlContent(ControlContent tControlContent) {
        if (tControlContent == null) {
            throw new NullPointerException("ControlContent must not be null");
        }
        mControlContent.setVer(tControlContent.getVer());
        mControlContent.setTs(tControlContent.getTs());
        mControlContent.setFrom(tControlContent.getFrom());
        mControlContent.setTo(tControlContent.getTo());
        mControlContent.setSession(tControlContent.getSession());
        mControlContent.setAppid(tControlContent.getAppid());
        mControlContent.setMsgtw(tControlContent.getMsgtw());
    }

    /**
     * 拷贝 ControlContent 内容
     */
    public void memorControlContent(String jsonStr) throws JSONException {
        if (jsonStr == null) {
            throw new NullPointerException("jsonStr must not be null");
        }
        DataContent dataContent = DataContent.newDataContent(jsonStr);
        dataContent.copyControlDataFromJson(mControlContent);
    }

    /**
     * 拷贝 ControlContent 内容
     */
    public void memorControlContent(JSONObject jsonObj) {
        if (jsonObj == null) {
            throw new NullPointerException("jsonData must not be null");
        }
        DataContent dataContent = DataContent.newDataContent(jsonObj);
        dataContent.copyControlDataFromJson(mControlContent);
    }

    /**
     * 拷贝 ControlContent 内容
     */
    public void memorControlContent(DataContent dataContent) {
        if (dataContent == null) {
            throw new NullPointerException("DataContent must not be null");
        }
        dataContent.copyControlDataFromJson(mControlContent);
    }

    /**
     * 控制层的json数据字符串
     *
     * @param content 业务层的数据结构
     * @return Json字符串
     */
    public String toControlJsonStr(JSONObject content) {
        return toControlJsonObj(content).toString();
    }

    /**
     * 控制层的json数据对象
     *
     * @param content 业务层的数据结构
     * @return Json对象
     */
    public JSONObject toControlJsonObj(JSONObject content) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ver", mControlContent.getVer());
            obj.put("ts", mControlContent.getTs());
            obj.put("from", mControlContent.getFrom());
            obj.put("to", mControlContent.getTo());
            obj.put("session", mControlContent.getSession());
            obj.put("appid", mControlContent.getAppid());
            obj.put("msgtw", mControlContent.getMsgtw());
            obj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
