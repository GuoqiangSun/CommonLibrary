package cn.com.swain.baselib.jsInterface.IotContent.request.control;

import cn.com.swain.baselib.jsInterface.IotContent.BaseIotRequestBean;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/27 0027
 * desc :
 */
public abstract class AbsControlJson extends BaseIotRequestBean {

    /**
     * 从json串获取ver
     */
    public abstract int getVerByJson();

    /**
     * 从json串获取ts
     */
    public abstract long getTsByJson();

    /**
     * 从json串获取from
     */
    public abstract long getFromByJson();

    /**
     * 从json串获取from
     */
    public abstract long getToByJson();

    /**
     * 从json串获取session
     */
    public abstract int getSessionByJson();

    /**
     * 从json串获取appid
     */
    public abstract long getAppidByJson();

    /**
     * 从json串获取msgtw
     */
    public abstract int getMsgtwByJson();

}
