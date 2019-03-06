package cn.com.swain.support.protocolEngine.resolve;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.DataInspector.IDataInspector;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public abstract class DataResolveQueue extends Handler {

    protected String TAG = AbsProtocolProcessor.TAG;
    private IDataInspector mDataInspector;
    private ISocketDataProducer mSocketDataProducer;
    private ISocketDataProducer mLargerSocketDataProducer;
    private Map<String, ResolveData> mSocketArrayMap;

    public DataResolveQueue(Looper mLooper,
                            IDataInspector mDataInspector,
                            ISocketDataProducer mSocketDataProducer) {
        this(mLooper, mDataInspector, mSocketDataProducer, null);
    }

    /**
     * @param mLooper                   解析线程
     * @param mDataInspector            数据质检
     * @param mSocketDataProducer       一般包
     * @param mLargerSocketDataProducer 如果要支持超大包,就传这个参数进来
     */
    public DataResolveQueue(Looper mLooper,
                            IDataInspector mDataInspector,
                            ISocketDataProducer mSocketDataProducer,
                            ISocketDataProducer mLargerSocketDataProducer) {
        super(mLooper);
        if (mDataInspector == null) {
            throw new NullPointerException(" <DataResolveQueue> ; IDataInspector==null . ");
        }
        if (mSocketDataProducer == null) {
            throw new NullPointerException(" <DataResolveQueue> ; mSocketDataFactory==null . ");
        }
        this.mSocketArrayMap = Collections.synchronizedMap(new HashMap<String, ResolveData>());
        this.mDataInspector = mDataInspector;
        this.mSocketDataProducer = mSocketDataProducer;
        this.mLargerSocketDataProducer = mLargerSocketDataProducer;
    }

    public void release() {
        removeCallbacksAndMessages(null);

        if (mSocketDataProducer != null) {
            mSocketDataProducer.clear();
            mSocketDataProducer = null;
        }

        if (mLargerSocketDataProducer != null) {
            mLargerSocketDataProducer.clear();
            mSocketDataProducer = null;
        }

        if (mDataInspector != null) {
            mDataInspector.release();
            mDataInspector = null;
        }

        Map<String, ResolveData> socketArrayMap = this.mSocketArrayMap;
        this.mSocketArrayMap = null;
        if (socketArrayMap != null) {
            socketArrayMap.clear();
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

                resolveData((ReceivesData) msg.obj);

                break;

            case MSG_WHAT_CALLBACK:

                if (mDataInspector != null) {
                    mDataInspector.inspectData(msg.arg1, (SocketDataArray) msg.obj);
                } else {
                    Tlog.w(TAG, " handleMessage inspect data ,but mDataInspector=null ");
                }

                break;

        }

    }


    /**
     * 一包数据最大多少，
     * 如果{@link #mLargerSocketDataProducer} ==null ,
     * 超过这个字节认为是错误的包
     */
    protected static final int MAX_COUNT = 1024;

    /**
     * 一包数据最大多少，
     * 如果{@link #mLargerSocketDataProducer} !=null ,
     * 超过这个字节认为是错误的包
     */
    protected static final int LARGER_COUNT = MAX_COUNT * 16;


    protected boolean checkPkgIsLargerSize(final byte[] buf, final int length) {
        if (length >= 3) {
            int size = (buf[1] & 0xFF) << 8 | (buf[2] & 0xFF);
            return size > MAX_COUNT;
        }
        return false;
    }

    /**
     * new一个超大buf包 包大小超过{@link #MAX_COUNT}不超过{@link #LARGER_COUNT}
     */
    protected SocketDataArray produceLargerSocketDataArray() {
        return mLargerSocketDataProducer != null ? mLargerSocketDataProducer.produceSocketDataArray() : null;
    }

    /**
     * new 一个普通buf包 包大小不超过{@link #MAX_COUNT}
     */
    protected SocketDataArray produceSocketDataArray() {
        return mLargerSocketDataProducer != null ? mLargerSocketDataProducer.produceSocketDataArray() : null;
    }

    private void resolveData(ReceivesData receiverData) {

        if (receiverData == null) {
            Tlog.w(TAG, " DataResolveQueue handleMessage mReceiverData==null ");
            resolveDataIsNull();
            return;
        }

        final byte[] buf = receiverData.data;

        if (buf == null) {
            Tlog.w(TAG, " DataResolveQueue handleMessage byte[]==null ");
            resolveDataIsNull();
            return;
        }

        if (mSocketArrayMap != null) {
            ResolveData mResolveData = mSocketArrayMap.get(receiverData.fromID);

            if (mResolveData == null) {
                mResolveData = new ResolveData();
                mResolveData.device = receiverData.fromID;
                mSocketArrayMap.put(receiverData.fromID, mResolveData);
                Tlog.w(TAG, " DataResolveQueue mSocketArrayMap put " + receiverData.fromID);
            }

            resolveData(receiverData, buf, mResolveData);

        } else {
            Tlog.w(TAG, " resolveData but mSocketArrayMap=null ");
        }

    }

    //接收数据为null
    protected void resolveDataIsNull() {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL;
        message.obj = null;
        message.sendToTarget();
    }

    // 解析的数据有头无尾
    protected void resolveDataHasHeadNoTail() {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL;
        message.obj = null;
        message.sendToTarget();
    }

    // 解析数据成功
    protected void resolveDataSuccess(SocketDataArray mTmpSocketDataArray) {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.SUCCESS_CODE_RESOLVE;
        message.obj = mTmpSocketDataArray;
        message.sendToTarget();
    }

    // 解析数据有尾无头
    protected void resolveDataHasTailNoHead() {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD;
        message.obj = null;
        message.sendToTarget();
    }

    // 解析数据太长
    protected void resolveDataMoreLength() {
        Message message = obtainMessage();
        message.what = MSG_WHAT_CALLBACK;
        message.arg1 = ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH;
        message.obj = null;
        message.sendToTarget();
    }

    protected abstract void resolveData(ReceivesData receiverData, byte[] buf, ResolveData mResolveData);

}
