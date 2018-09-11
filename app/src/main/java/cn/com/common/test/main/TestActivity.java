package cn.com.common.test.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.com.common.test.global.LooperManager;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.result.SimpleProtocolResult;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class TestActivity extends AppCompatActivity {

    private static String TAG = SocketApplication.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tlog.v(TAG, " testActivity onCreate ");

//        testSocketDataArray();
//        testProtocolManager();

//        IO io = new IO();
//        io.testException();

//        testBle();

//        testHandler();


//        WebView w;

//        testHandler1();


//        testDB();

//        byte[] buf = new byte[1024*1024*12];
//        StringBuffer sb = new StringBuffer(1024*1024*12);
//        sb.append("hello");

//        localDa

//        com.android.internal.R.array.networkAttributes;

//        ConnectivityService c;
//        WifiCommand d;


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pm != null) {
            pm.release();
        }

    }

    private ProtocolProcessor pm;

    private void testProtocolManager() {

        pm = new ProtocolProcessor(LooperManager.getInstance().getProtocolLooper(), new SimpleProtocolResult() {
            @Override
            public void onFail(FailTaskResult failTaskResult) {

            }

            @Override
            public void onSuccess(SocketDataArray mSocketDataArray) {

            }
        }, new SocketDataQueueProducer(0), 3);

        new Thread() {
            @Override
            public void run() {
                super.run();

                int times = 0;

                while (true) {
//                    if (++times >= 20) {
//                        break;
//                    }
//        byte[] buf = new byte[]{(byte) 0xff,0,6,0,0,1,1,2,(byte)0x55,(byte)0xaa,(byte)0xeb,(byte)0xee };
//                    byte[] buf = new byte[]{(byte) 0xff, 0x02, 0x05, 0x00, 0x00, 0x01, 0x01, 0x01, 0x31, (byte) 0xcc};
//
//                    ReceivesData mData = new ReceivesData();
//                    mData.fromID = "123";
//                    mData.data = buf;
//                    pm.onInReceiveData(mData);

                    ReceivesData mData0 = new ReceivesData();
                    byte[] buf0 = new byte[]{(byte) 0xff, 0x00, 0x05, 0x00, 0x00, 0x01, 0x01, 0x01, 0x31, (byte) 0xee};
                    mData0.data = buf0;
                    pm.onInReceiveData(mData0);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

    private void testSocketDataArray() {
        SocketDataArray mSocketDataArray = new SocketDataArray(0);

//        心跳	0xff	0x0	0x5	0x0	0x0	0x01	0x01	0x01	效验（0x31）	0xee

        byte[] buf = new byte[]{(byte) 0xff, 0x00, 0x05, 0x00, 0x00, 0x01, 0x01, 0x01, 0x31, (byte) 0xee};

        buf = new byte[]{(byte) 0xff, 0, 6, 0, 0, 1, 1, 2, (byte) 0x55, (byte) 0xaa, (byte) 0xeb, (byte) 0xee};

        mSocketDataArray.onAddPackageReverse(buf);
        Tlog.v(TAG, mSocketDataArray.toString());

        mSocketDataArray.reset();

        buf = new byte[]{
                (byte) 0xff,
                0x00, 0x07, // vl
                0x00, 0x00,
                0x01, 0x01,
                0x02, (byte) 0xFF, (byte) 0xEE,
                (byte) 0xEB,  // crc
                (byte) 0xee};


        mSocketDataArray.onAddPackageEscape(buf);
        Tlog.v(TAG, mSocketDataArray.toString());


        ByteArrayOutputStream bos;


    }

    Handler h;

    private void testHandler() {

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 0:
                        Tlog.v(TAG, "case 0 ; has 0 " + h.hasMessages(0) + " has 1 " + h.hasMessages(1));
                        break;
                    case 1:
                        Tlog.v(TAG, "case 1 ; has 0 " + h.hasMessages(0) + " has 1 " + h.hasMessages(1));
                        break;
                }

            }
        };

        h.sendEmptyMessage(0);
        h.sendEmptyMessageDelayed(1, 2000);
        h.sendEmptyMessageDelayed(1, 3000);
        h.removeMessages(1);
    }

    private void testHandler1() {


    }

    public class IO {

        private void testException() throws INDEX {
            Tlog.v(TAG, "testException");
        }
    }

    private class INDEX extends IOException {

    }


}
