package cn.com.swain.support.protocolEngine.Repeat;

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

    /**
     * 默认重发次数
     */
    public static final int DEFAULT_REPEAT_TIMES = 1;

    /**
     * 最大重发次数
     */
    private int maxRepeatTimes = DEFAULT_REPEAT_TIMES;

    public void setMaxRepeatTimes(int maxRepeatTimes) {
        if (maxRepeatTimes < 0) {
            maxRepeatTimes = 0;
        }
        this.maxRepeatTimes = maxRepeatTimes;
        checkNeedRepeat();

    }

    public int getMaxRepeatTimes() {
        return maxRepeatTimes;
    }


    /**
     * 重发次数
     */
    private int repeatSendTimes = 0;

    public void setRepeatOnce() {
        ++repeatSendTimes;
        checkNeedRepeat();

    }

    private void checkNeedRepeat() {
        if (this.repeatSendTimes >= this.maxRepeatTimes) {
            needRepeatSend = false;
        }
    }

    public int getCurRepeastTimes() {
        return repeatSendTimes;
    }

    /**
     * 客户
     */
    private int custom;

    public int getCustom() {
        return custom;
    }

    public void setCustom(int custom) {
        this.custom = custom;
    }

    /**
     * 产品
     */
    private int product;

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
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
     * 序号
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
        return "RepeatMsgModel{" +
                "needRepeatSend=" + needRepeatSend +
                ", maxRepeatTimes=" + maxRepeatTimes +
                ", repeatSendTimes=" + repeatSendTimes +
                ", custom=" + custom +
                ", product=" + product +
                ", msgWhat=" + msgWhat +
                ", msgSeq=" + msgSeq +
                '}';
    }

}
