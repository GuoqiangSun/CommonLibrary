package cn.com.swain.baselib.jsInterface.CommonContent.qx;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.CommonContent.BaseJsRequestBean;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class QXDataContent extends BaseJsRequestBean {

    public QXDataContent() {
        this(new QXControlContent(), new QXBusinessContent());
    }

    public QXDataContent(QXBusinessContent mQXBusinessContent) {
        this(new QXControlContent(), mQXBusinessContent);
    }

    private final QXControlContent mQXControlContent;

    private final QXBusinessContent mQXBusinessContent;

    public QXDataContent(QXControlContent mQXControlContent, QXBusinessContent mQXBusinessContent) {
        this.mQXControlContent = mQXControlContent;
        this.mQXBusinessContent = mQXBusinessContent;
    }

    public QXControlContent getQXControlContent() {
        return mQXControlContent;
    }

    public QXBusinessContent getQXBusinessContent() {
        return mQXBusinessContent;
    }

    /**
     * 从json串获取控制消息并缓存
     */
    public void takeControlDataFromJson() {
        mQXControlContent.setVer(getVerByJson());
        mQXControlContent.setTs(getTsByJson());
        mQXControlContent.setFrom(getFromByJson());
        mQXControlContent.setTo(getToByJson());
        mQXControlContent.setSession(getSessionByJson());
        mQXControlContent.setAppid(getAppidByJson());
        mQXControlContent.setMsgtw(getMsgtwByJson());
    }

    /**
     * 从json串获取控制消息并返回
     */
    public QXControlContent copyControlDataFromJson() {
        QXControlContent mQXControlContent = new QXControlContent();
        mQXControlContent.setVer(getVerByJson());
        mQXControlContent.setTs(getTsByJson());
        mQXControlContent.setFrom(getFromByJson());
        mQXControlContent.setTo(getToByJson());
        mQXControlContent.setSession(getSessionByJson());
        mQXControlContent.setAppid(getAppidByJson());
        mQXControlContent.setMsgtw(getMsgtwByJson());
        return mQXControlContent;
    }

    public void takeBusinessDataFromJson(JSONObject content) {
        if (content != null) {
            mQXBusinessContent.setCustom(getCustomByJson());
            mQXBusinessContent.setProduct(getProductByJson());
            mQXBusinessContent.setCmd(getCmdByJson());
        }
    }

    /**
     * 从json串获取业务消息并缓存
     */
    public void takeBusinessDataFromJson() {
        takeBusinessDataFromJson(getBusinessJsonData());
    }

    /**
     * 从json串获取业务消息并copy
     */
    public QXBusinessContent copyBusinessDataFromJson() {
        JSONObject businessJsonData = getBusinessJsonData();
        QXBusinessContent mQXBusinessContent = new QXBusinessContent();
        mQXBusinessContent.setCustom(getCustomByJson(businessJsonData));
        mQXBusinessContent.setProduct(getProductByJson(businessJsonData));
        mQXBusinessContent.setCmd(getCmdByJson(businessJsonData));
        return mQXBusinessContent;
    }

    /**
     * 从json串获取ver
     */
    public int getVerByJson() {
        return getIntByRoot("ver");
    }

    /**
     * 从json串获取ts
     */
    public long getTsByJson() {
        return getLongByRoot("ts");
    }

    /**
     * 从json串获取from
     */
    public long getFromByJson() {
        return getLongByRoot("from");
    }

    /**
     * 从json串获取from
     */
    public long getToByJson() {
        return getLongByRoot("to");
    }

    /**
     * 从json串获取session
     */
    public int getSessionByJson() {
        return getIntByRoot("session");
    }

    /**
     * 从json串获取appid
     */
    public long getAppidByJson() {
        return getIntByRoot("appid");
    }

    /**
     * 从json串获取msgtw
     */
    public int getMsgtwByJson() {
        return getIntByRoot("msgtw");
    }

    /**
     * 从json串获取业务JsonData
     */
    public JSONObject getBusinessJsonData() {
        return getJSONObjectByRoot("content");
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


    /**
     * 从json串获取custom
     */
    public int getCustomByJson() {
        JSONObject businessJsonData = getBusinessJsonData();
        return getCustomByJson(businessJsonData);
    }

    /**
     * 从json串获取custom
     */
    public int getCustomByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "custom");
    }

    /**
     * 从json串获取product
     */
    public int getProductByJson() {
        JSONObject businessJsonData = getBusinessJsonData();
        return getProductByJson(businessJsonData);
    }

    /**
     * 从json串获取product
     */
    public int getProductByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "product");
    }

    /**
     * 从json串获取cmd
     */
    public int getCmdByJson() {
        JSONObject businessJsonData = getBusinessJsonData();
        return getCmdByJson(businessJsonData);
    }

    /**
     * 从json串获取cmd
     */
    public int getCmdByJson(JSONObject businessJsonData) {
        return getInt(businessJsonData, "cmd");
    }

}
