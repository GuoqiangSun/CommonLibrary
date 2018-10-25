package cn.com.common.test.testProtocol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.com.common.test.global.LooperManager;
import cn.com.common.test.main.CommonApplication;
import cn.com.swain.baselib.util.StatusBarUtil;
import cn.com.swain.support.protocolEngine.DataInspector.DatagramInspector;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.EscapeIOException;
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

public class TestProtocolActivity extends AppCompatActivity {

    private static String TAG = CommonApplication.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tlog.v(TAG, " TestProtocolActivity onCreate ");
        run = true;

        testSocketDataArray();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pm != null) {
            pm.release();
        }
        run = false;
    }

    private ProtocolProcessor pm;
    private boolean run;

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

                while (run) {
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

//        buf = new byte[]{(byte) 0xff, 0, 6, 0, 0, 1, 1, 2, (byte) 0x55, (byte) 0xaa, (byte) 0xeb, (byte) 0xee};

        mSocketDataArray.onAddPackageReverse(buf);
        Tlog.v(TAG, mSocketDataArray.toString());

        DatagramInspector inspector = new DatagramInspector(mSocketDataArray);
        Tlog.v(TAG, " crc " + inspector.checkCrc() + " h " + inspector.hasHead() + " t " + inspector.hasTail());

        SocketDataArray socketDataArray = null;
        try {
            socketDataArray = SocketDataArray.parseSocketData(buf, 0);
            Tlog.d(TAG, " socketDataArray " + socketDataArray.toString());
        } catch (EscapeIOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " ", e);
        }

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
        SocketDataArray socketDataArray2 = null;
        try {
            socketDataArray2 = SocketDataArray.parseSocketData(buf, 0);
            Tlog.d(TAG, " socketDataArray2 " + socketDataArray2.toString());
        } catch (EscapeIOException e) {
            e.printStackTrace();
            Tlog.e(TAG, " ", e);
        }


    }


}
