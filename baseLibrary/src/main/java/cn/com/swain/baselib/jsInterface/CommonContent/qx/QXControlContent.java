package cn.com.swain.baselib.jsInterface.CommonContent.qx;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc @see https://docs.qq.com/doc/DY1JldFNtWHNvY0NL?opendocxfrom=admin
 */
public class QXControlContent {

    /**
     * 两个字节
     * 版本号
     */
    public int ver;

    /**
     * 六个字节
     * 时间戳，单位毫秒
     */
    public long ts;

    /**
     * 六个字节
     * 数据包来自哪里
     */
    public long from;

    /**
     * 六个字节
     * 数据包去向哪里
     */
    public long to;

    /**
     * 两个字节
     * 数据包标签
     */
    public int session;

    /**
     * 四个字节
     * 应用标识
     */
    public long appid;

    /**
     * 两个字节
     * 消息通道
     */
    public int msgtw;

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public long getAppid() {
        return appid;
    }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public int getMsgtw() {
        return msgtw;
    }

    public void setMsgtw(int msgtw) {
        this.msgtw = msgtw;
    }

    @Override
    public String toString() {
        return "QXControlContent{" +
                "ver=" + ver +
                ", ts=" + ts +
                ", from=" + from +
                ", to=" + to +
                ", session=" + session +
                ", appid=" + appid +
                ", msgtw=" + msgtw +
                '}';
    }
}
