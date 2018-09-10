package cn.com.swain.support.protocolEngine.utils;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/20 0020
 * desc :
 */
public class SEQ {

    private final String mac;

    public SEQ(String mac) {
        this.mac = mac;
        this.SEQ = 0;
    }

    private int SEQ;

    public synchronized int getSelfAddSeq() {
        final int seq = SEQ;
        SEQ++;
        return seq;
    }

    public synchronized void resetSeq() {
        this.SEQ = 0;
    }
}
