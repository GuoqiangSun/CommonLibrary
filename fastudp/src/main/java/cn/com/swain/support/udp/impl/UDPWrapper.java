package cn.com.swain.support.udp.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/6 0006
 * Desc:
 */
public class UDPWrapper extends AbsFastUdp implements IUDPResult {

    private UDP mUDP;
    private WriteHandler mHandler;

    private static final long MAX_DELAY = 1000 * 3L;
    private static final long DELAY = 500L;

    public UDPWrapper(Looper mLooper, BuildUDPParams mBuildUDPParams) {
        this.mUDP = new UDP(mBuildUDPParams, this);
        this.mHandler = new WriteHandler(this, mLooper);
    }

    @Override
    public void onUDPInitResult(boolean result, String ip, int port) {
        if (mResult != null) {
            mResult.onUDPInitResult(result, ip, port);
        }
    }

    @Override
    public void onUDPReceiveData(String ip, int port, byte[] data) {
        if (mResult != null) {
            mResult.onUDPReceiveData(ip, port, data);
        }
    }

    @Override
    public void onUDPReleaseResult(boolean result) {
        if (mResult != null) {
            mResult.onUDPReleaseResult(result);
        }
    }

    @Override
    public void init() {
        if (mUDP != null) {
            mUDP.exe();
        } else {
            if (mResult != null) {
                mResult.onUDPInitResult(false, "0.0.0.0", -1);
            }
        }
    }

    @Override
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.clear();
        }
        if (mUDP != null) {
            mUDP.release();
            mUDP = null;
        } else {
            if (mResult != null) {
                mResult.onUDPReleaseResult(true);
            }
        }
    }

    private boolean showLog = false;

    private DateFormat dateFormat;

    @Override
    public void showLog(boolean showLog) {
        this.showLog = showLog;
        if (showLog && this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        }
    }

    private IUDPResult mResult;

    @Override
    public void regUDPSocketResult(IUDPResult mResult) {
        this.mResult = mResult;
    }

    @Override
    public void unregUDPSocketResult(IUDPResult mResult) {
        if (this.mResult != null && this.mResult == mResult) {
            this.mResult = null;
        }
    }

    @Override
    public DatagramSocket getDatagramSocket() {
        if (mUDP != null) {
            return mUDP.getDatagramSocket();
        }
        return null;
    }

    @Override
    public void broadcast(DatagramPacket datagramPacket) {
        if (mHandler != null) {
            mHandler.obtainMessage(MSG_WHAT_BROAD_OBJ, datagramPacket).sendToTarget();
        }
    }

    private long aLastBroadMillis = System.currentTimeMillis();
    private long aLastBroadDelay = MIN_DELAY;

    @Override
    public void broadcastDelay(DatagramPacket datagramPacket) {
        broadcastDelay(datagramPacket, DELAY, MAX_DELAY);
    }

    @Override
    public void broadcastDelay(DatagramPacket datagramPacket, long delay, long maxDelay) {

        long currentTimeMillis = System.currentTimeMillis();
        long mLastBroadMillis = aLastBroadMillis;
        long mLastDelay = aLastBroadDelay;

        long mDelay;

        long diff = currentTimeMillis - mLastBroadMillis;

        if (diff > 0 && diff < delay) {
            mDelay = delay - diff;
        } else if (diff < 0) {
            mDelay = mLastDelay + delay;
        } else {
            mDelay = MIN_DELAY;
        }

        if (mDelay > maxDelay) {
            if (mLastDelay > maxDelay) {
                mDelay = mLastDelay + MIN_DELAY;
            } else {
                mDelay = maxDelay + MIN_DELAY;
            }
        }

        Message message = mHandler.obtainMessage(MSG_WHAT_BROAD_OBJ_DELAY, datagramPacket);
        mHandler.sendMessageDelayed(message, mDelay);

        aLastBroadMillis = currentTimeMillis + mDelay;
        aLastBroadDelay = mDelay;
    }

    @Override
    public void send(DatagramPacket datagramPacket) {
        mHandler.obtainMessage(MSG_WHAT_WRITE_OBJ, datagramPacket).sendToTarget();
    }

    private long aLastSendMillis = System.currentTimeMillis();
    private long aLastSendDelay = MIN_DELAY;

    private static final long MIN_DELAY = 50L;

    @Override
    public void sendDelay(DatagramPacket datagramPacket) {
        sendDelay(datagramPacket, DELAY, MAX_DELAY);
    }

    @Override
    public void sendDelay(DatagramPacket datagramPacket, long delay, long maxDelay) {

        long currentTimeMillis = System.currentTimeMillis();
        long mLastSendMillis = aLastSendMillis;
        long mLastDelay = aLastSendDelay;

        long mDelay;

        long diff = currentTimeMillis - mLastSendMillis;

        if (diff > 0 && diff < delay) {
            mDelay = delay - diff;
        } else if (diff < 0) {
            mDelay = mLastDelay + delay;
        } else {
            mDelay = MIN_DELAY;
        }

        if (mDelay > maxDelay) {
            if (mLastDelay > maxDelay) {
                mDelay = mLastDelay + MIN_DELAY;
            } else {
                mDelay = maxDelay + MIN_DELAY;
            }
        }

        Message message = mHandler.obtainMessage(MSG_WHAT_WRITE_OBJ_DELAY, datagramPacket);
        mHandler.sendMessageDelayed(message, mDelay);

        aLastSendMillis = currentTimeMillis + mDelay;
        aLastSendDelay = mDelay;

        if (showLog && Tlog.isDebug()) {
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
            }
            Tlog.v(TAG, " cur:" + dateFormat.format(new Date(currentTimeMillis))
                    + " last:" + dateFormat.format(new Date(aLastSendMillis))
                    + " diff:" + diff
                    + " delay:" + aLastSendDelay);
        }

    }


    private static final int MSG_WHAT_BROAD_OBJ = 0x02;
    private static final int MSG_WHAT_BROAD_OBJ_DELAY = 0x03;
    private static final int MSG_WHAT_WRITE_OBJ = 0x04;
    private static final int MSG_WHAT_WRITE_OBJ_DELAY = 0x05;

    private void handleMessage(Message msg) {

        switch (msg.what) {

            case MSG_WHAT_BROAD_OBJ:
                sendObj(msg);
                break;

            case MSG_WHAT_BROAD_OBJ_DELAY:
                sendObj(msg);
                break;

            case MSG_WHAT_WRITE_OBJ:
                sendObj(msg);
                break;
            case MSG_WHAT_WRITE_OBJ_DELAY:
                sendObj(msg);
                break;
            default:

                break;
        }

    }


    private void sendObj(Message msg) {

        DatagramPacket datagramPacket = (DatagramPacket) msg.obj;

        if (datagramPacket == null) {
            Tlog.e(TAG, " sendObj datagramPacket==null ");
            return;
        }

        if (mUDP != null) {
            mUDP.send(datagramPacket);
        } else {
            Tlog.e(TAG, " sendObj mUDP==null ");
        }

    }


    private static class WriteHandler extends Handler {
        WeakReference<UDPWrapper> wr;

        private WriteHandler(UDPWrapper mUdpLanCom, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<>(mUdpLanCom);
        }

        void clear() {
            if (wr != null) {
                wr.clear();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            UDPWrapper mFastUdpImpl;

            if (wr != null && (mFastUdpImpl = wr.get()) != null) {
                mFastUdpImpl.handleMessage(msg);
            } else {
                Tlog.e(TAG, " UdpLanCom = null ");
            }

        }
    }


}
