package cn.com.swain.support.udp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/28 0028
 * Desc:
 */
class FastUdpImpl extends AbsFastUdp {

    public static final String TAG = "fastUdp";

    private WriteHandler mHandler;
    private IUDPSocketResult mResult;

    private static final long MAX_DELAY = 1000 * 3L;
    private static final long DELAY = 500L;


    private static final String ERROR_IP = "0.0.0.0";
    private static final int ERROR_PORT = -1;

    FastUdpImpl(Looper mLooper) {
        if (mLooper == null) {
            throw new NullPointerException(" mLooper == null ");
        }
        this.mHandler = new WriteHandler(this, mLooper);
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

    private ExecutorService executorService;

    @Override
    public void init() {

        if (run) {

            DatagramSocket datagramSocket = getDatagramSocket();

            Tlog.v(TAG, " init UDP  datagramSocket==null?" + (datagramSocket == null));

            if (datagramSocket != null && !datagramSocket.isClosed()) {
                if (mResult != null) {
                    String ip = datagramSocket.getLocalAddress().getHostName();
                    int port = datagramSocket.getLocalPort();
                    mResult.onUDPSocketInitResult(true, ip, port);
                }
            } else {
                if (mResult != null) {
                    mResult.onUDPSocketInitResult(false, ERROR_IP, ERROR_PORT);
                }
            }

            return;
        }

        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newSingleThreadExecutor();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                initUdp();
            }
        });
    }

    private DatagramSocket mUdpSocket;

    @Override
    public DatagramSocket getDatagramSocket() {
        return mUdpSocket;
    }

    @Override
    public void regUDPSocketResult(IUDPSocketResult mResult) {
        this.mResult = mResult;
    }

    @Override
    public void unregUDPSocketResult(IUDPSocketResult mResult) {
        if (this.mResult != null && this.mResult == mResult) {
            this.mResult = null;
        }
    }

    private boolean run;

    private void initUdp() {


        DatagramSocket datagramSocket = newDatagramSocket();

        if (datagramSocket == null) {
            Tlog.e(TAG, " init lan com udp fail ... ");
            if (mResult != null) {
                mResult.onUDPSocketInitResult(false, ERROR_IP, ERROR_PORT);
            }
            return;
        }

        this.run = true;

        String ip = datagramSocket.getLocalAddress().getHostName();
        int port = datagramSocket.getLocalPort();
        this.mUdpSocket = datagramSocket;

        Tlog.e(TAG, " DatagramSocket init success ip:" + ip + " port:" + port);
        if (mResult != null) {
            mResult.onUDPSocketInitResult(run, ip, port);
        }

        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

        final int mReceiveLength = 1024;
        final byte[] mRecBuf = new byte[mReceiveLength];
        final DatagramPacket mReceivePacket = new DatagramPacket(mRecBuf, mReceiveLength);

        try {
            while (run) {

                mUdpSocket.receive(mReceivePacket);
                int length = mReceivePacket.getLength();
//                Tlog.i(TAG, "udp receive " + length);
                if (length <= 0) {
                    continue;
                }

                byte[] data = Arrays.copyOf(mRecBuf, length);

//                byte[] data = new byte[length];
//                System.arraycopy(mRecBuf, 0, data, 0, length);

                String hostAddress = mReceivePacket.getAddress().getHostAddress();
                int port1 = mReceivePacket.getPort();

                if (Tlog.isDebug()) {
                    Tlog.d(TAG, " receive-" + hostAddress + ":" + port1 + " " + StrUtil.toString(data));
                }

                if (mResult != null) {
                    mResult.onUDPSocketReceiveData(hostAddress, port1, data);
                } else {
                    Tlog.e(TAG, " receive one pkg mResult=null");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, "udp receive ", e);
        }

        Tlog.i(TAG, "udp run finish ");
        release();

        if (mResult != null) {
            mResult.onUDPSocketReleaseResult(true);
            mResult = null;
        }
    }

    private DatagramSocket newDatagramSocket() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            Tlog.e(TAG, " newDatagramSocket:", e);
        }
        return datagramSocket;
    }

    @Override
    public void release() {

        Tlog.e(TAG, " release FastUdpImpl ");

        this.run = false;

        mHandler.removeCallbacksAndMessages(null);

        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        if (mUdpSocket != null) {
            mUdpSocket.close();
            mUdpSocket = null;
        }

    }

    @Override
    public void broadcast(DatagramPacket datagramPacket) {
        if (run) {
            mHandler.obtainMessage(MSG_WHAT_BROAD_OBJ, datagramPacket).sendToTarget();
        } else {
            Tlog.e(TAG, " udp broadcast ; but not run ");
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

        if (!run) {
            Tlog.e(TAG, " udp broadcastDelay ; but not run ");
        }

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
        if (run) {
            mHandler.obtainMessage(MSG_WHAT_WRITE_OBJ, datagramPacket).sendToTarget();
        } else {
            Tlog.e(TAG, " udp send ; but not run ");
        }
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

        if (!run) {
            Tlog.e(TAG, " udp sendDelay ; but not run ");
        }

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

//        if (diff > 0 && diff <= DELAY) {
//            delay = mSendTimes * DELAY;
//
//            if (delay > maxDelay) {
//                delay = maxDelay;
//            } else if (delay <= 0) {
//                delay = DELAY;
//            }
//
//        }

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

        if (Tlog.isDebug()) {
            Tlog.i(TAG, " sendObj-" + IpUtil.valueOf(datagramPacket));
        }

        if (mUdpSocket != null) {
            try {
                mUdpSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                Tlog.e(TAG, " sendObj IOException ", e);
            }
        } else {
            Tlog.e(TAG, " sendObj mUdpSocket==null ");
        }

    }


    private static class WriteHandler extends Handler {
        WeakReference<FastUdpImpl> wr;

        private WriteHandler(FastUdpImpl mUdpLanCom, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<>(mUdpLanCom);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            FastUdpImpl mFastUdpImpl;

            if (wr != null && (mFastUdpImpl = wr.get()) != null) {
                mFastUdpImpl.handleMessage(msg);
            } else {
                Tlog.e(TAG, " UdpLanCom = null ");
            }

        }
    }


}
