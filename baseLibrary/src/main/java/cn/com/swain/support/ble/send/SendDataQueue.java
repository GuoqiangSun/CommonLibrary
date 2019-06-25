package cn.com.swain.support.ble.send;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/11 0011
 * desc :
 */

public class SendDataQueue extends AbsBleSend {

    private final AbsBleSend mBleSend;
    private final SendHandler mSendHandler;
    private final long DELAY;


    public SendDataQueue(Looper mLooper, AbsBleSend mBleSend) {
        this(mLooper, mBleSend, 300);
    }

    public SendDataQueue(Looper mLooper, AbsBleSend mBleSend, long delay) {
        this.mBleSend = mBleSend;
        if (delay <= 0) {
            delay = 100;
        }
        this.DELAY = delay;
        this.mSendHandler = new SendHandler(mLooper, this);
    }


    private long mLastSendMillis = System.currentTimeMillis();

    private int mSendTimes = 0;

    private static final long MAX_DELAY = 1000 * 3L;

    @Override
    public void setResolveDataLength(int length) {
        mBleSend.setResolveDataLength(length);
    }

    @Override
    public void setPrintData(boolean print) {
        mBleSend.setPrintData(print);
    }

    @Override
    public void sendData(byte[] data) {

        long currentTimeMillis = System.currentTimeMillis();

        long delay = 10L;

        if (currentTimeMillis - mLastSendMillis <= 300) {
            delay = mSendTimes * DELAY;

            if (delay > MAX_DELAY) {
                delay = MAX_DELAY;
            } else if (delay <= 0) {
                delay = DELAY;
            }

        }
        mLastSendMillis = currentTimeMillis;
        mSendTimes++;

        Message message = mSendHandler.obtainMessage(MSG_SEND, data);
        mSendHandler.sendMessageDelayed(message, delay);

    }

    @Override
    public void sendData(byte[] data, long delay) {

        mSendHandler.obtainMessage(MSG_SEND_DELAY, (int) delay, (int) delay, data).sendToTarget();

    }

    @Override
    public String getUuidStr() {
        return mBleSend.getUuidStr();
    }

    @Override
    public void removeMsg() {
        mBleSend.removeMsg();
        mSendHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void closeGatt() {
        mBleSend.closeGatt();
    }

    @Override
    public String getMac() {
        return mBleSend.getMac();
    }

    private static final int MSG_SEND = 0x01;
    private static final int MSG_SEND_DELAY = 0x02;

    private void handleMessage(Message msg) {

        if (msg.what == MSG_SEND) {
            mBleSend.sendData((byte[]) msg.obj);
            mSendTimes--;
        } else if (msg.what == MSG_SEND_DELAY) {
            mBleSend.sendData((byte[]) msg.obj, msg.arg1);
        }
    }


    private static class SendHandler extends Handler {
        private final WeakReference<SendDataQueue> wr;

        public SendHandler(Looper mLooper, SendDataQueue mSendDataQueue) {
            super(mLooper);
            wr = new WeakReference<SendDataQueue>(mSendDataQueue);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SendDataQueue sendDataQueue;

            if (wr != null && (sendDataQueue = wr.get()) != null) {
                sendDataQueue.handleMessage(msg);
            }

        }
    }


}
