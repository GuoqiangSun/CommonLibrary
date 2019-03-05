package cn.com.swain.support.protocolEngine.resolve.QX;

import android.os.Looper;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.DataInspector.IDataInspector;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.resolve.DataResolveQueue;
import cn.com.swain.support.protocolEngine.resolve.ResolveData;

/**
 * author: Guoqiang_Sun
 * date: 2019/1/24 0024
 * Desc:
 */
public class QxDataResolveQueue extends DataResolveQueue {

    private static final byte STX = ProtocolBuild.QX.STX;
    private static final byte ETX = ProtocolBuild.QX.ETX;

    public QxDataResolveQueue(Looper mLooper, IDataInspector mDataInspector,
                              ISocketDataProducer mSocketDataProducer, ISocketDataProducer mLargerSocketDataProducer) {
        super(mLooper, mDataInspector, mSocketDataProducer, mLargerSocketDataProducer);
    }

    public QxDataResolveQueue(Looper mLooper, IDataInspector mDataInspector, ISocketDataProducer mSocketDataProducer) {
        super(mLooper, mDataInspector, mSocketDataProducer);
    }

    @Override
    protected void resolveData(ReceivesData receiverData, byte[] buf, ResolveData mResolveData) {

        SocketDataArray mTmpSocketDataArray = mResolveData.mLastSocketDataArray;

        final int length = buf.length;

        for (int i = 0; i < length; i++) {

            switch (buf[i]) {
                case STX:
                    // 头字节

                    mResolveData.hasHead = true;
                    mResolveData.count = 0;

                    if (mTmpSocketDataArray != null && !mTmpSocketDataArray.isCompletePkg()) {
                        // 重新收到一包数据，判断上包数据是否是完整的包，不是完整的包设置可回收
                        Tlog.e(TAG, "last SocketDataArray is not complete pkg ");
                        mTmpSocketDataArray.setISUnUsed();

                        resolveDataHasHeadNoTail();

                    }

                    if (checkPkgIsLargerSize(buf, length)) {
                        mTmpSocketDataArray = produceLargerSocketDataArray();
                    }

                    if (mTmpSocketDataArray != null) {
                        mResolveData.isLargerPkg = true;
                    } else {
                        mResolveData.isLargerPkg = false;
                        mTmpSocketDataArray = produceSocketDataArray();
                    }

                    mTmpSocketDataArray.resetIsCompletePkg();
                    mTmpSocketDataArray.reset();
                    mTmpSocketDataArray.setISUsed();
                    mTmpSocketDataArray.setID(receiverData.fromID);
                    mTmpSocketDataArray.setObj(receiverData.obj);
                    mTmpSocketDataArray.setArg(receiverData.arg);
                    mTmpSocketDataArray.setModel(receiverData.getReceiveModel());
                    mTmpSocketDataArray.changeStateToReverse();
                    mTmpSocketDataArray.onAddHead(buf[i]);

                    mResolveData.mLastSocketDataArray = mTmpSocketDataArray;

                    // Tlog.v("startCount", " count : " + ++time);

                    break;
                case ETX:
                    // 尾字节

                    if (mResolveData.hasHead) {

                        if (mTmpSocketDataArray == null) {

                            resolveDataIsNull();

                        } else {

                            mTmpSocketDataArray.onAddTail(buf[i]);
                            mTmpSocketDataArray.setIsCompletePkg();
                            resolveDataSuccess(mTmpSocketDataArray);

                        }


                    } else {

                        // 解析到尾字节，但没解析到头部字节
                        Tlog.e(TAG, " PARSE ERROR . Parsing to ETX, but no parsing to STX . ");
                        resolveDataHasTailNoHead();

                    }

                    mResolveData.hasHead = false;
                    mResolveData.count = 0;

                    break;

                default:

                    if (mResolveData.hasHead) {

                        if (mTmpSocketDataArray == null) {

                            resolveDataIsNull();

                        } else {

                            ++mResolveData.count;

                            if (mResolveData.count < MAX_COUNT
                                    || (mResolveData.isLargerPkg && mResolveData.count < LARGER_COUNT)) {

                                mTmpSocketDataArray.onAddDataReverse(buf[i]);

                            } else {
                                // 解析内容太长
                                mResolveData.hasHead = false;
                                mTmpSocketDataArray.setISUnUsed();
                                resolveDataMoreLength();

                                Tlog.e(TAG, " PARSE ERROR .Parsing data , but count("
                                        + mResolveData.count + ")>=MAX_COUNT(" + MAX_COUNT + ")");
                            }
                        }


                    } else {

                        // 没有解析到头部字节
                        Tlog.e(TAG, " PARSE ERROR . parse buf[" + i + "]("
                                + Integer.toHexString(buf[i] & 0xFF) + ") , but no parsing to STX . ");
                    }


                    break;
            }

        }

    }
}
