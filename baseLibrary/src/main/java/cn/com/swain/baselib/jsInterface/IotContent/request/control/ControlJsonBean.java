package cn.com.swain.baselib.jsInterface.IotContent.request.control;

/**
 * author Guoqiang_Sun
 * date 2019/5/16
 * desc
 */
public class ControlJsonBean extends AbsControlJson {

    /**
     * 从json串获取ver
     */
    @Override
    public int getVerByJson() {
        return getIntByRoot("ver");
    }

    /**
     * 从json串获取ts
     */
    @Override
    public long getTsByJson() {
        return getLongByRoot("ts");
    }

    /**
     * 从json串获取from
     */
    @Override
    public long getFromByJson() {
        return getLongByRoot("from");
    }

    /**
     * 从json串获取from
     */
    @Override
    public long getToByJson() {
        return getLongByRoot("to");
    }

    /**
     * 从json串获取session
     */
    @Override
    public int getSessionByJson() {
        return getIntByRoot("session");
    }

    /**
     * 从json串获取appid
     */
    @Override
    public long getAppidByJson() {
        return getIntByRoot("appid");
    }

    /**
     * 从json串获取msgtw
     */
    @Override
    public int getMsgtwByJson() {
        return getIntByRoot("msgtw");
    }


}
