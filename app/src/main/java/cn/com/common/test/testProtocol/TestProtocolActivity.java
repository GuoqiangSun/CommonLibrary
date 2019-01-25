package cn.com.common.test.testProtocol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

import cn.com.common.test.global.LooperManager;
import cn.com.common.test.main.CommonApplication;
import cn.com.swain.support.protocolEngine.DataInspector.DatagramInspector;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.datagram.ProtocolException.EscapeIOException;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.pack.ResponseData;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.result.SimpleProtocolResult;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class TestProtocolActivity extends AppCompatActivity {

    private static String TAG = CommonApplication.TAG;
    AbsProtocolProcessor absProtocolProcessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tlog.v(TAG, " TestProtocolActivity onCreate ");
        run = true;

//        testSocketDataArray();


        absProtocolProcessor = ProtocolProcessorFactory.newSingleChannelSingleTask(
                LooperManager.getInstance().getProtocolLooper(),
                new SimpleProtocolResult() {
                    @Override
                    public void onFail(FailTaskResult failTaskResult) {
                        Tlog.e(TAG, " onFail:" + String.valueOf(failTaskResult));
                    }

                    @Override
                    public void onSuccess(SocketDataArray mSocketDataArray) {
                        Tlog.e(TAG, " onSuccess " + String.valueOf(mSocketDataArray));

                        byte[] protocolParams = mSocketDataArray.getProtocolParams();
                        Tlog.v(TAG, " getProtocolParamsLength:"
                                + mSocketDataArray.getProtocolParamsLength()
                                + " getProtocolParams:"
                                + protocolParams.length);

                        mSocketDataArray.setISUnUsed();

                    }
                }, ProtocolBuild.VERSION.VERSION_SEQ, true);

        ProtocolDataCache.BuildParams mParams = new ProtocolDataCache.BuildParams();
        mParams.mCustom = 0;
        mParams.mProduct = 0;
        mParams.mProtocolVersion = ProtocolBuild.VERSION.VERSION_SEQ;
        ProtocolDataCache.getInstance().init(mParams);
        ProtocolDataCache.getInstance().onSCreate();

        LooperManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {


                byte[] buf = new byte[]{(byte) 0xff, 6, 18, 1, 0xd, 0, 0, 0, 0, 0, 2, 2, 0x1a,
                        0, 12, 0xb, 13, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 17,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 87, (byte) 0xa8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 58, 98, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 40, (byte) 2d, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 62,
                        (byte) 0xe5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 80, 0, 0, 0, 67, 0x4c, 0, 0, 0, 0, 0, 0, 0x4c, 0x2c, 0, 0, 0,
                        0, 0, 0, 28, (byte) 0x8c, 0, 0, 0, 0, 0, 0, 0x4b, 0x6c, 0, 0, 0, 0, 0, 0, 0x4b, 0x2f, 0, 0, 0, 0, 0, 0, 0x4b, 0xc, 0, 0, 0, 0,
                        0, 0, 4, 92, 0, 0, 0, 0, 0, 0, 0x4c, 0x3b, 0, 0, 0, 0, 0, 0, 0x4c, 27, 0, 0, 0, 0, 0, 0, 0x4b, (byte) 0xd5, 0, 0, 0, 0, 0,
                        0, 21, 55, 99, 0, 0, 0, 0, 0, 0, 0x4c, 17, 0, 0, 0, 0, 0, 0, 0x4c, 0x2c, 0, 0, 0, 0, 0, 0, 0x4c, 0x4f, 0, 0, 0, 0, 0,
                        0, 0x4c, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x5a, 84, 0, 0, 0, 0, 0, 0, 0x4b, 82, 0, 0, 0, 0, 0, 0, 0x4b,
                        0x1a, 0, 0, 0, 0, 0, 0, 0x4b, 5, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0,
                        0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b,
                        0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0x4b, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x1e, 43, 0, 0, 0, 0, 0, 0, 0x4c, 27, 0, 0, 0, 0,
                        0, 0, 0x4b, 74, 0, 0, 0, 0, 0, 0, 0x4b, 0xb, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0,
                        0, 0, 0, 0, 0x4b, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 96, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 96, 45, 0, 0, 0, 0, 0, 0, 0x4b, (byte) 0xac, 0, 0, 0, 0, 0, 0, 0x4b, (byte) 0xb6,
                        0, 0, 0, 0, 0, 0, 0x4b, 0x6a, 0, 0, 0, 0, 0, 0, 0x4b, 48, 0, 0, 0, 0, 0, 0, 0x4b, 0x4b, 0, 0, 0, 0, 0, 0, 0x4b,
                        0xf, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 0, 0, 0, 0, 0, 0, 0, 0x4b, 67,
                        0, 0, 0, 0, 0, 0, 0x4b, 87, 0, 0, 0, 0, 0, 0, 0x4b, 21, 0, 0, 0, 0, 0, 0, 0x4b, 6, 0, 0, 0, 0, 0, 0, 0x4b, 0xf,
                        0, 0, 0, 0, 0, 0, 0x4b, 24, 0,
                        0, 0, 0, 0, 4, 18, (byte) 0xab, 0, 0, 0, 0, 0, 3, 27, (byte) 9d, 0, 0, 0, 0, 0, 3, 0x2a, 25, 0, 0, 0, 0, 1, 0x5b, (byte) 0xee
                };

                ReceivesData mReceiveData = new ReceivesData("00:00:00:00:00:00", buf);
                absProtocolProcessor.onInputServerData(mReceiveData);

//                ResponseData electricityPrice = ProtocolDataCache.getElectricityPrice("00:00:00:00:00:00", 2);

                ResponseData electricityPrice = ProtocolDataCache.getTempUnit("00:00:00:00:00:00", (byte)2);

                ResponseData monetaryUnit = ProtocolDataCache.getMonetaryUnit("00:00:00:00:00:00", (byte) 0x03);

                byte[] bytes = Arrays.copyOf(monetaryUnit.data, monetaryUnit.data.length);
                if (bytes[3] != 0x69) {
                    bytes[3] = 0x69;
                } else {
                    bytes[3] = (byte) 0x96;
                }


                exe = true;
                while (exe) {
                    mReceiveData = new ReceivesData(electricityPrice.toID, electricityPrice.data);
                    absProtocolProcessor.onInputServerData(mReceiveData);

                    mReceiveData = new ReceivesData(monetaryUnit.toID, monetaryUnit.data);
                    absProtocolProcessor.onInputServerData(mReceiveData);


                    mReceiveData = new ReceivesData(monetaryUnit.toID, bytes);
                    absProtocolProcessor.onInputServerData(mReceiveData);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private boolean exe;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        exe = false;
        run = false;
        if (pm != null) {
            pm.release();
        }
        if (absProtocolProcessor != null) {
            absProtocolProcessor.release();
        }
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
