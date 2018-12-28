package cn.com.swain.support.udp.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/12/6 0006
 * Desc:
 */
public class UDP implements Runnable {

    private static final String TAG = AbsFastUdp.TAG;

    private ExecutorService executorService;

    private boolean run;

    private IUDPResult mResult;

    private final BuildUDPParams mBuildUDPParams;

    public UDP(BuildUDPParams mBuildUDPParams, IUDPResult mResult) {
        if (mBuildUDPParams == null) {
            throw new NullPointerException(" BuildUDPParams=null ");
        }
        this.mBuildUDPParams = mBuildUDPParams;
        this.mResult = mResult;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void exe() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(this);
        } else {
            Tlog.e(TAG, " UDP exe fail, executorService shutdown");

            if (mResult != null) {
                mResult.onUDPInitResult(false, "0.0.0.0", -1);
            }

        }
    }

    private void closeSocket() {
        DatagramSocket datagramSocket = getDatagramSocket();
        if (datagramSocket != null && !datagramSocket.isClosed()) {
            datagramSocket.close();
        }
    }

    public void release() {
        this.run = false;
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
            this.executorService = null;
        } else {
            if (mResult != null) {
                mResult.onUDPReleaseResult(true);
            }
        }
        closeSocket();
        this.datagramSocket = null;
    }

    private DatagramSocket datagramSocket;

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    @Override
    public void run() {

        final DatagramSocket tmpDatagramSocket = newUDP(mBuildUDPParams);
        this.run = tmpDatagramSocket != null && !tmpDatagramSocket.isClosed();
        this.datagramSocket = tmpDatagramSocket;

        if (mResult != null) {
            String ip = tmpDatagramSocket != null ? tmpDatagramSocket.getLocalAddress().getHostName() : "0.0.0.0";
            int port = tmpDatagramSocket != null ? tmpDatagramSocket.getLocalPort() : -1;
            mResult.onUDPInitResult(this.run, ip, port);
        }

        final int mReceiveLength = mBuildUDPParams.recPkgLength;
        final byte[] mRecBuf = new byte[mReceiveLength];
        final DatagramPacket mReceivePacket = new DatagramPacket(mRecBuf, mReceiveLength);

        Tlog.e(TAG, " UDP exe run:" + run);
        try {
            while (run) {

                this.datagramSocket.receive(mReceivePacket);
                int length = mReceivePacket.getLength();
                if (length <= 0) {
                    continue;
                }

                byte[] data = Arrays.copyOf(mRecBuf, length);
                String hostAddress = mReceivePacket.getAddress().getHostAddress();
                int port = mReceivePacket.getPort();

                if (Tlog.isDebug()) {
                    Tlog.d(TAG, " receive-" + hostAddress + ":" + port + " " + StrUtil.toString(data));
                }

                if (mResult != null) {
                    mResult.onUDPReceiveData(hostAddress, port, data);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " UDP.rec:", e);
        }

        closeSocket();
        this.datagramSocket = null;

        if (mResult != null) {
            mResult.onUDPReleaseResult(true);
        }
    }

    public void send(DatagramPacket datagramPacket) {
        if (!run) {
            Tlog.e(TAG, "UDP send run=false");
            return;
        }
        if (datagramSocket == null) {
            Tlog.e(TAG, "UDP send datagramSocket=null");
            return;
        }

        if (Tlog.isDebug()) {
            Tlog.i(TAG, " send-" + IpUtil.valueOf(datagramPacket));
        }

        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " UDP.send:", e);
        }
    }

    private static DatagramSocket newUDP(BuildUDPParams mBuildUDPParams) {

        if (mBuildUDPParams.groupInet == null) {
            return newUnicastSocket(mBuildUDPParams.bindPort, mBuildUDPParams.isReuseAddress);
        } else {
            return newMulticastSocket(mBuildUDPParams.bindPort, mBuildUDPParams.isReuseAddress, mBuildUDPParams.groupInet);
        }

    }

    public static DatagramSocket newUnicastSocket(int bindPort, boolean reuseAddress) {
        DatagramSocket datagramSocket = null;
        try {
            if (bindPort >= 0 && bindPort <= 65535) {
                datagramSocket = new DatagramSocket(null);
                datagramSocket.setReuseAddress(reuseAddress);
                datagramSocket.bind(new InetSocketAddress(bindPort));
            } else {
                //  Enable SO_REUSEADDR before binding
                // 所以没有指定port ,不可以设置setReuseAddress
                datagramSocket = new DatagramSocket();
            }

        } catch (SocketException e) {
            e.printStackTrace();
            Tlog.e(TAG, " newDatagramSocket:", e);
        }
        return datagramSocket;
    }

    public static MulticastSocket newMulticastSocket(int bindPort, boolean reuseAddress, InetAddress groupInet) {

        MulticastSocket multicastSocket = null;

        try {
            if (bindPort >= 0 && bindPort <= 65535) {
                multicastSocket = new MulticastSocket(null);
//                Enable SO_REUSEADDR before binding
                multicastSocket.setReuseAddress(reuseAddress);
                multicastSocket.bind(new InetSocketAddress(bindPort));
            } else {
                multicastSocket = new MulticastSocket();
            }
            multicastSocket.joinGroup(groupInet);

        } catch (IOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " newMulticastSocket:", e);
        }

        return multicastSocket;
    }

}
