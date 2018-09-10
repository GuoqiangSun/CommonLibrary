package cn.com.swain.support.protocolEngine.DataInspector;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolProcessor;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;
import cn.com.swain.support.protocolEngine.utils.ProtocolCode;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/11 0011
 * desc :
 */

public class DataResolveInspector extends AbsDataInspector {

    private String TAG = ProtocolProcessor.TAG;

    private final IProtocolAnalysisResult mProtocolCallBack;

    private final ThreadLocal<DatagramInspector> mThreadLocal = new ThreadLocal<>();

    public DataResolveInspector(IProtocolAnalysisResult mProtocolCallBack) {
        this.mProtocolCallBack = mProtocolCallBack;
    }

    @Override
    public void onOutDataResolve(int code, SocketDataArray mSocketDataArray) {

        if (mProtocolCallBack == null) {
            Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve mProtocolCallBack==null ");
            return;
        }

        if (code != ProtocolCode.SUCCESS_CODE_RESOLVE) {

            switch (code) {
                case ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL:
                    Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_INTERNAL_RECEIVE_NULL");
                    mProtocolCallBack.onFailReceiveDataNull(ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL);
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH:
                    Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_RESOLVE_MORE_LENGTH");
                    mProtocolCallBack.onFailLengthTooLong(ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH);
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD:
                    Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD");
                    mProtocolCallBack.onFailHasTailNoHead(ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD);
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL:
                    Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL");
                    mProtocolCallBack.onFailHasHeadNoTail(ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL);
                    break;
            }

            return;

        }

        DatagramInspector mCheckDatagram = mThreadLocal.get();
        if (mCheckDatagram == null) {
            mCheckDatagram = new DatagramInspector();
            mThreadLocal.set(mCheckDatagram);
        }

        mCheckDatagram.setSocketDataArray(mSocketDataArray);

        if (mCheckDatagram.isNull()) {
            Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_INTERNAL_PKG_NULL");
            mCheckDatagram.clearCache();
            mProtocolCallBack.onPackNullError(ProtocolCode.ERROR_CODE_INTERNAL_PKG_NULL);
            return;
        }

        if (!mCheckDatagram.hasHead()) {
            Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_NO_HEAD");
            mCheckDatagram.clearCache();
            mProtocolCallBack.onPackNoHeadError(ProtocolCode.ERROR_CODE_NO_HEAD, mSocketDataArray);
            return;
        }

        if (!mCheckDatagram.checkCrc()) {
            Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_CRC");
            mCheckDatagram.clearCache();
            mProtocolCallBack.onPackCrcError(ProtocolCode.ERROR_CODE_CRC, mSocketDataArray);
            return;
        }

        if (!mCheckDatagram.hasTail()) {
            Tlog.e(TAG, " <ProtocolProcessor> onOutDataResolve ERROR_CODE_NO_TAIL");
            mCheckDatagram.clearCache();
            mProtocolCallBack.onPackNoTailError(ProtocolCode.ERROR_CODE_NO_TAIL, mSocketDataArray);
            return;
        }

        mCheckDatagram.clearCache();
        mProtocolCallBack.onSuccess(mSocketDataArray);

    }

    @Override
    public void release() {
        mThreadLocal.remove();
    }
}
