package cn.com.swain.support.udp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */
public class UdpLanCom extends AbsFastUdp {

    public static final String TAG = "UdpLanCom";

    private WriteHandler mHandler;
    private final ISocketResult mResult;

    private static final long MAX_DELAY = 1000 * 3L;
    private static final long DELAY = 500L;

    public UdpLanCom(Looper mLooper, ISocketResult mResult) {

        this.mHandler = new WriteHandler(this, mLooper);
        this.mResult = mResult;

    }

    private ExecutorService executorService;

    public void init() {
        release();

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                initUdp();
            }
        });
    }

    private DatagramSocket mUdpSocket;
    private boolean run;

    private void initUdp() {

        String ip = "0.0.0.0";
        int port = -1;
        try {
            mUdpSocket = new DatagramSocket();

            InetAddress localAddress = mUdpSocket.getLocalAddress();
            byte[] address = localAddress.getAddress();
            ip = String.format("%x.%x.%x.%x", address[0], address[1], address[2], address[3]);
            port = mUdpSocket.getLocalPort();

            String localIpV4Address = IpUtil.getLocalIpV4Address();

            Tlog.e(TAG, " DatagramSocket init() ip:" + ip + " port:" + port + " localIpV4Address:" + localIpV4Address);

            this.run = true;
        } catch (SocketException e) {
            e.printStackTrace();
            Tlog.e(TAG, " DatagramSocket init:", e);
            this.run = false;
        }

        if (mResult != null) {
            mResult.onSocketInitResult(run, ip, port);
        }

        Tlog.v(TAG, " UdpLanCom init finish ; run : " + run);

        if (!this.run) {
            Tlog.e(TAG, " init lan com udp fail ... ");
            release();
            return;
        }

//        Thread.currentThread().setPriority(Process.THREAD_PRIORITY_DEFAULT);
        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

        final int mReceiveLength = 1024;
        byte[] mRecBuf = new byte[mReceiveLength];
        final DatagramPacket mReceivePacket = new DatagramPacket(mRecBuf, mReceiveLength);


        try {
            while (run) {

                mUdpSocket.receive(mReceivePacket);
                int length = mReceivePacket.getLength();
//                Tlog.i(TAG, "udp receive " + length);
                if (length <= 0) {
                    continue;
                }

                byte[] data = new byte[length];
                System.arraycopy(mRecBuf, 0, data, 0, length);
                String hostAddress = mReceivePacket.getAddress().getHostAddress();
                int port1 = mReceivePacket.getPort();

                if (Tlog.isDebug()) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : data) {
                        sb.append(Integer.toHexString(b & 0xFF)).append(" , ");
                    }
                    Tlog.d(TAG, " receive-" + hostAddress + ":" + port1 + " " + sb.toString());
                }

                if (mResult != null) {
                    mResult.onSocketReceiveData(hostAddress, port1, data);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, "udp receive ", e);
        }

        Tlog.i(TAG, "udp run finish ");
        release();

    }

    public void release() {

        Tlog.v(TAG, " udpLanCom release ");

        this.run = false;

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
    public void broadcast(UdpResponseMsg mResponseData) {
        if (run) {
            mHandler.obtainMessage(MSG_WHAT_BROAD_OBJ, mResponseData).sendToTarget();
        } else {
            Tlog.e(TAG, " udp broadcast ; but not run ");
        }
    }

    private long mLastBroadMillis = System.currentTimeMillis();

    private int mBroadTimes = 0;

    @Override
    public void broadcastDelay(UdpResponseMsg mResponseData) {
        broadcastDelay(mResponseData, DELAY, MAX_DELAY);
    }

    @Override
    public void broadcastDelay(UdpResponseMsg mResponseData, long DELAY, long maxDelay) {

        if (!run) {
            Tlog.e(TAG, " udp broadcastDelay ; but not run ");
        }

        long currentTimeMillis = System.currentTimeMillis();

        long delay = 10L;

        if (currentTimeMillis - mLastBroadMillis <= DELAY) {
            delay = mBroadTimes * DELAY;

            if (delay > maxDelay) {
                delay = maxDelay;
            } else if (delay <= 0) {
                delay = DELAY;
            }

        }
        mLastBroadMillis = currentTimeMillis;
        mBroadTimes++;
        Message message = mHandler.obtainMessage(MSG_WHAT_BROAD_OBJ_DELAY, mResponseData);
        mHandler.sendMessageDelayed(message, delay);
    }

    @Override
    public void write(UdpResponseMsg mResponseData) {
        if (run) {
            mHandler.obtainMessage(MSG_WHAT_WRITE_OBJ, mResponseData).sendToTarget();
        } else {
            Tlog.e(TAG, " udp write ; but not run ");
        }
    }

    private long mLastSendMillis = System.currentTimeMillis();

    private int mSendTimes = 0;

    @Override
    public void writeDelay(UdpResponseMsg mResponseData) {
        writeDelay(mResponseData, DELAY, MAX_DELAY);
    }

    @Override
    public void writeDelay(UdpResponseMsg mResponseData, long DELAY, long maxDelay) {

        if (!run) {
            Tlog.e(TAG, " udp writeDelay ; but not run ");
        }

        long currentTimeMillis = System.currentTimeMillis();

        long delay = 10L;

        if (currentTimeMillis - mLastSendMillis <= DELAY) {
            delay = mSendTimes * DELAY;

            if (delay > MAX_DELAY) {
                delay = MAX_DELAY;
            } else if (delay <= 0) {
                delay = DELAY;
            }

        }
        mLastSendMillis = currentTimeMillis;
        mSendTimes++;
        Message message = mHandler.obtainMessage(MSG_WHAT_WRITE_OBJ_DELAY, mResponseData);
        mHandler.sendMessageDelayed(message, delay);

    }


    private static final int MSG_WHAT_BROAD_OBJ = 0x02;
    private static final int MSG_WHAT_BROAD_OBJ_DELAY = 0x03;
    private static final int MSG_WHAT_WRITE_OBJ = 0x04;
    private static final int MSG_WHAT_WRITE_OBJ_DELAY = 0x05;

    private void handleMessage(Message msg) {

        switch (msg.what) {

            case MSG_WHAT_BROAD_OBJ:
                broadObj(msg);
                break;

            case MSG_WHAT_BROAD_OBJ_DELAY:
                broadObj(msg);
                mBroadTimes--;
                break;

            case MSG_WHAT_WRITE_OBJ:
                writeObj(msg);
                break;
            case MSG_WHAT_WRITE_OBJ_DELAY:
                writeObj(msg);
                mSendTimes--;
                break;
            default:

                break;
        }

    }

    private void writeObj(Message msg) {

        if (mUdpSocket == null) {
            Tlog.e(TAG, " writeObj mUdpSocket==null ");
            return;
        }

        UdpResponseMsg mResponseData = (UdpResponseMsg) msg.obj;

        if (mResponseData == null) {
            Tlog.e(TAG, " writeObj mResponseData==null ");
            return;
        }


        try {

            String ip = mResponseData.ip;

            InetAddress byName;
            try {
                byName = InetAddress.getByName(ip);
            } catch (Exception e) {
                e.printStackTrace();
                Tlog.e(TAG, "writeObj InetAddress.getByName(" + ip + ") ", e);
                return;
            }

            int port = mResponseData.port;
            byte[] buf = mResponseData.data;
            int length = buf.length;

            if (Tlog.isDebug()) {
                Tlog.i(TAG, " writeObj-" + ip + ":" + port + " " + StrUtil.toString(buf));
            }

            DatagramPacket datagramPacket = new DatagramPacket(buf, length, byName, port);
            mUdpSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " writeObj IOException ", e);
        }


    }


    private void broadObj(Message msg) {

        if (mUdpSocket == null) {
            Tlog.e(TAG, " broadObj mUdpSocket==null ");
            return;
        }

        UdpResponseMsg mResponseData = (UdpResponseMsg) msg.obj;

        if (mResponseData == null) {
            Tlog.e(TAG, " broadObj mResponseData==null ");
            return;
        }

        try {

            String ip = mResponseData.ip;

            InetAddress byName;
            try {
                byName = InetAddress.getByName(ip);
            } catch (Exception e) {
                e.printStackTrace();
                Tlog.e(TAG, "broadObj InetAddress.getByName(" + ip + ") ", e);
                return;
            }

            int port = mResponseData.port;
            byte[] buf = mResponseData.data;
            int length = buf.length;

            if (Tlog.isDebug()) {
                Tlog.i(TAG, " broadObj-" + byName.getHostAddress() + ":" + port + " " + StrUtil.toString(buf));
            }

            DatagramPacket datagramPacket = new DatagramPacket(buf, length, byName, port);
            mUdpSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " broadObj IOException ", e);
        }

    }

    private static class WriteHandler extends Handler {
        WeakReference<UdpLanCom> wr;

        private WriteHandler(UdpLanCom mUdpLanCom, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<>(mUdpLanCom);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            UdpLanCom mUdpLanCom;

            if (wr != null && (mUdpLanCom = wr.get()) != null) {
                mUdpLanCom.handleMessage(msg);
            } else {
                Tlog.e(TAG, " UdpLanCom = null ");
            }

        }
    }


}
