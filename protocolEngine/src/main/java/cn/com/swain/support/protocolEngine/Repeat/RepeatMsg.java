package cn.com.swain.support.protocolEngine.Repeat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.protocolEngine.IO.IDataProtocolOutput;
import cn.com.swain.support.protocolEngine.pack.ResponseData;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/9 0009
 * desc :
 */
public class RepeatMsg extends Handler {

    private String TAG = "RepeatMsg";

    private final String mac;
    private final IDataProtocolOutput mResponse;
    private static final int TIME_OUT = 1000 * 6;

    public RepeatMsg(String mac, Looper mLooper, IDataProtocolOutput mResponse) {
        super(mLooper);
        this.mResponse = mResponse;
        this.mac = mac;
    }

    public void recordSendMsg(ResponseData responseData) {
        recordSendMsg(responseData, TIME_OUT);
    }

    /**
     * @param responseData
     * @param timeOut      超时重发时间
     */
    public void recordSendMsg(ResponseData responseData, long timeOut) {

        RepeatMsgModel repeatMsgModel = responseData.getRepeatMsgModel();
        int msgWhat = repeatMsgModel.getMsgWhat();
        int msgSeq = repeatMsgModel.getMsgSeq();

        if (hasMessages(msgWhat)) {
            removeMessages(msgWhat);
        }

        Message message = obtainMessage(msgWhat, msgSeq, msgSeq, responseData);
        sendMessageDelayed(message, timeOut);
    }

    private int lastSeq;

    public void receiveOnePkg(int what, int seq) {

        if (hasMessages(what)) {
            removeMessages(what);
        }

        this.lastSeq = seq;

    }

    public void handleMessage(Message msg) {
        if (Tlog.isDebug()) {
            Tlog.d(TAG, " handleMessage timeout what:" + Integer.toHexString(msg.what) + " seq:" + msg.arg1);
        }

        ResponseData responseData = (ResponseData) msg.obj;

        if (responseData != null) {
            if (Tlog.isDebug()) {
                Tlog.d(TAG, " mRepeatHandler repeatSendMsg:" + responseData.toString());
            }

            if (responseData.getRepeatMsgModel().isNeedRepeatSend()) {

                responseData.getRepeatMsgModel().setRepeatOnce();

                if (mResponse != null) {
                    mResponse.onOutputDataToServer(responseData);
                }

            }

        }

    }


}
