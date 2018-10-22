package cn.com.swain.support.protocolEngine.resolve;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class DataResolveQueue extends Handler {

    private String TAG = ProtocolProcessor.TAG;
    private final IDataResolveCallBack mCallBack;
    private final ISocketDataProducer mSocketDataProducer;
    private final Map<String, ResolveData> mSocketArrayMap;

    public DataResolveQueue(Looper mLooper, IDataResolveCallBack mCallBack, ISocketDataProducer mSocketDataProducer) {
        super(mLooper);
        if (mCallBack == null) {
            throw new NullPointerException(" <DataResolveQueue> ; IDataResolveCallBack==null . ");
        }
        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <DataResolveQueue> ; mSocketDataFactory==null . ");
        }
        this.mCallBack = mCallBack;
        this.mSocketDataProducer = mSocketDataProducer;
        this.mSocketArrayMap = Collections.synchronizedMap(new HashMap<String, ResolveData>());
    }

    public void release() {
        removeCallbacksAndMessages(null);

        if (mSocketDataProducer != null) {
            mSocketDataProducer.clear();
        }

        if (mCallBack != null) {
            mCallBack.release();
        }

        if (mSocketArrayMap != null) {
            mSocketArrayMap.clear();
        }

    }

    public void postReceiveDataToQueue(ReceivesData mData) {
        obtainMessage(MSG_WHAT_ANALYSIS, mData).sendToTarget();
    }

    private static final int MSG_WHAT_ANALYSIS = 0x02;
    private static final int MSG_WHAT_CALLBACK = 0x03;

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_WHAT_ANALYSIS:

                final ReceivesData mReceiverData = (ReceivesData) msg.obj;
                resolveData(mReceiverData);

                break;

            case MSG_WHAT_CALLBACK:

                Object obj = msg.obj;
                SocketDataArray mSocketDataArray = null;
                if (obj != null) {
                    mSocketDataArray = (SocketDataArray) obj;
                }

                mCallBack.onOutDataResolve(msg.arg1, mSocketDataArray);

                break;

        }

    }

    private void receiveDataIsNull() {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL;
        message.obj = null;
        message.sendToTarget();
    }


    /**
     * 一包数据最大多少，超过这个字节认为是错误的包
     */
    private static final int MAX_COUNT = 1024;

//    /**
//     * 是否解析到头字节
//     */
//    private volatile boolean hasHead = false;
//
//    /**
//     * 解析到的数据包大小
//     */
//    private int count = 0;

//    private volatile SocketDataArray mTmpSocketDataArray = null;


    private void resolveData(ReceivesData receiverData) {

        if (receiverData == null) {
            Tlog.w(TAG, " DataResolveQueue handleMessage mReceiverData==null ");
            receiveDataIsNull();
            return;
        }

        final byte[] buf = receiverData.data;

        if (buf == null) {
            Tlog.w(TAG, " DataResolveQueue handleMessage byte[]==null ");
            receiveDataIsNull();
            return;
        }

        ResolveData mResolveData = mSocketArrayMap.get(receiverData.fromID);

        if (mResolveData == null) {
            mResolveData = new ResolveData();
            mResolveData.device = receiverData.fromID;
            mSocketArrayMap.put(receiverData.fromID, mResolveData);
        }

        SocketDataArray mTmpSocketDataArray = mResolveData.mLastSocketDataArray;

        final int length = buf.length;

        for (int i = 0; i < length; i++) {

            switch (buf[i]) {
                case ProtocolCode.STX:
                    // 头字节

                    mResolveData.hasHead = true;
                    mResolveData.count = 0;

                    if (mTmpSocketDataArray != null && !mTmpSocketDataArray.isCompletePkg()) {
                        // 重新收到一包数据，判断上包数据是否是完整的包，不是完整的包设置可回收
                        Tlog.e(TAG, "last SocketDataArray is not complete pkg ");
                        mTmpSocketDataArray.setISUnUsed();

                        Message message = obtainMessage();
                        message.what = MSG_WHAT_CALLBACK;
                        message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL;
                        message.obj = null;
                        message.sendToTarget();

                    }

                    mTmpSocketDataArray = mSocketDataProducer.produceSocketDataArray();
                    mTmpSocketDataArray.resetIsCompletePkg();
                    mTmpSocketDataArray.reset();
                    mTmpSocketDataArray.setISUsed();
                    mTmpSocketDataArray.setID(receiverData.fromID);
                    mTmpSocketDataArray.setArg(receiverData.arg);
                    mTmpSocketDataArray.setObj(receiverData.obj);
                    mTmpSocketDataArray.changeStateToReverse();
                    mTmpSocketDataArray.onAddHead(buf[i]);

                    mResolveData.mLastSocketDataArray = mTmpSocketDataArray;

                    // Tlog.v("startCount", " count : " + ++time);

                    break;
                case ProtocolCode.ETX:
                    // 尾字节

                    if (mResolveData.hasHead) {

                        if (mTmpSocketDataArray == null) {

                            receiveDataIsNull();

                        } else {

                            mTmpSocketDataArray.onAddTail(buf[i]);
                            mTmpSocketDataArray.setIsCompletePkg();

                            Message message = obtainMessage();
                            message.what = MSG_WHAT_CALLBACK;
                            message.arg1 = ProtocolCode.SUCCESS_CODE_RESOLVE;
                            message.obj = mTmpSocketDataArray;
                            message.sendToTarget();
                        }


                    } else {

                        // 解析到尾字节，但没解析到头部字节
                        Tlog.e(TAG, " PARSE ERROR . Parsing to ETX, but no parsing to STX . ");

                        Message message = obtainMessage();
                        message.what = MSG_WHAT_CALLBACK;
                        message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD;
                        message.obj = null;
                        message.sendToTarget();

                    }

                    mResolveData.hasHead = false;
                    mResolveData.count = 0;

                    break;

                default:

                    if (mResolveData.hasHead) {

                        if (mTmpSocketDataArray == null) {

                            receiveDataIsNull();

                        } else {
                            if (++mResolveData.count < MAX_COUNT) {

                                mTmpSocketDataArray.onAddDataReverse(buf[i]);

                            } else {
                                // 解析内容太长
                                mResolveData.hasHead = false;
                                mTmpSocketDataArray.setISUnUsed();

                                Message message = obtainMessage();
                                message.what = MSG_WHAT_CALLBACK;
                                message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH;
                                message.obj = null;
                                message.sendToTarget();
                                Tlog.e(TAG, " PARSE ERROR .Parsing data , but count(" + mResolveData.count + ")>=MAX_COUNT(" + MAX_COUNT + ")");
                            }
                        }


                    } else {

                        // 没有解析到头部字节
                        Tlog.e(TAG, " PARSE ERROR . parse buf[" + i + "](" + Integer.toHexString(buf[i] & 0xFF) + ") , but no parsing to STX . ");
                    }


                    break;
            }

        }

    }

}
