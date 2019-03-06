package cn.com.swain.support.protocolEngine.DataInspector;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.result.IProtocolAnalysisResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/11 0011
 * desc :
 */

public class DataInspector extends AbsDataInspector {

    private String TAG = AbsProtocolProcessor.TAG;

    private IProtocolAnalysisResult mProtocolCallBack;

    public DataInspector(IProtocolAnalysisResult mProtocolCallBack) {
        this.mProtocolCallBack = mProtocolCallBack;
    }

    @Override
    public void inspectData(int code, SocketDataArray mSocketDataArray) {

        if (code != ProtocolCode.SUCCESS_CODE_RESOLVE) {

            switch (code) {
                case ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL:
                    Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_INTERNAL_RECEIVE_NULL");
                    if (mProtocolCallBack != null) {
                        mProtocolCallBack.onFailReceiveDataNull(ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL);
                    }
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH:
                    Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_RESOLVE_MORE_LENGTH");
                    if (mProtocolCallBack != null) {
                        mProtocolCallBack.onFailLengthTooLong(ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH);
                    }
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD:
                    Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD");
                    if (mProtocolCallBack != null) {
                        mProtocolCallBack.onFailHasTailNoHead(ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD);
                    }
                    break;
                case ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL:
                    Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL");
                    if (mProtocolCallBack != null) {
                        mProtocolCallBack.onFailHasHeadNoTail(ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL);
                    }
                    break;
            }

            return;

        }

        if (null == mSocketDataArray) {
            Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_INTERNAL_PKG_NULL");
            if (mProtocolCallBack != null) {
                mProtocolCallBack.onPackNullError(ProtocolCode.ERROR_CODE_INTERNAL_PKG_NULL);
            }
            return;
        }

        if (!mSocketDataArray.hasProtocolHead()) {
            Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_NO_HEAD");
            if (mProtocolCallBack != null) {
                mProtocolCallBack.onPackNoHeadError(ProtocolCode.ERROR_CODE_NO_HEAD, mSocketDataArray);
            }
            return;
        }

        if (!mSocketDataArray.checkProtocolCrc()) {
            Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_CRC");
            if (mProtocolCallBack != null) {
                mProtocolCallBack.onPackCrcError(ProtocolCode.ERROR_CODE_CRC, mSocketDataArray);
            }
            return;
        }

        if (!mSocketDataArray.hasProtocolTail()) {
            Tlog.e(TAG, " <ProtocolProcessor> inspectData ERROR_CODE_NO_TAIL");
            if (mProtocolCallBack != null) {
                mProtocolCallBack.onPackNoTailError(ProtocolCode.ERROR_CODE_NO_TAIL, mSocketDataArray);
            }
            return;
        }

        if(Tlog.isDebug()){
            Tlog.i(TAG, " <ProtocolProcessor> inspectData SUCCESS");
        }
        if (mProtocolCallBack != null) {
            mProtocolCallBack.onSuccess(mSocketDataArray);
        }

    }

    @Override
    public void release() {
        mProtocolCallBack = null;
    }
}
