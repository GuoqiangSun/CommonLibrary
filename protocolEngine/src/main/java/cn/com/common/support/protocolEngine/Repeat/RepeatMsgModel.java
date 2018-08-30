package cn.com.common.support.protocolEngine.Repeat;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/14 0014
 * desc :重发的数据结构
 */
public class RepeatMsgModel {

    /**
     * 需要重复发送
     */

    private boolean needRepeatSend;

    public boolean isNeedRepeatSend() {
        return needRepeatSend;
    }

    public void setNeedRepeatSend(boolean needRepeatSend) {
        this.needRepeatSend = needRepeatSend;
    }

    private static final int MAX_REPEAT_TIMES = 1;


    private int repeatSendTimes = 0;

    public void setRepeatOnce() {

        if (++repeatSendTimes >= MAX_REPEAT_TIMES) {
            needRepeatSend = false;
        }

    }

    private int productType;

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    /**
     * msg
     */
    private int msgWhat;

    public int getMsgWhat() {
        return msgWhat;
    }

    public void setMsgWhat(int msgWhat) {
        this.msgWhat = msgWhat;
    }


    /**
     * 序列号
     */
    private int msgSeq;

    public int getMsgSeq() {
        return msgSeq;
    }

    public void setMsgSeq(int msgSeq) {
        this.msgSeq = msgSeq;
    }

    @Override
    public String toString() {
        return " needRepeatSend:" + needRepeatSend + "; msgWhat:" + Integer.toHexString(msgWhat) + "; msgSeq:" + msgSeq + " product:" + productType;
    }
}
