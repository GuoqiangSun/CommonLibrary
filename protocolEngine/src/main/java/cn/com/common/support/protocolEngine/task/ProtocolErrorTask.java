package cn.com.common.support.protocolEngine.task;

import cn.com.common.baselib.app.Tlog;
import cn.com.common.support.protocolEngine.datagram.SocketDataArray;
import cn.com.common.support.protocolEngine.utils.ProtocolCode;

/**
 * author: Guoqiang_Sun
 * date : 2018/5/17 0017
 * desc :
 */
public class ProtocolErrorTask extends SocketResponseTask {

    protected final FailTaskResult mTask = new FailTaskResult();

    public FailTaskResult getFailTaskResult() {
        return this.mTask;
    }

    public ProtocolErrorTask(int errorCode) {
        mTask.errorCode = errorCode;
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {
        String description;

        switch (mTask.errorCode) {

            case ProtocolCode.ERROR_CODE_INTERNAL_RECEIVE_NULL:
                description = " onInternalFailReceiveDataNull ";
                break;

            case ProtocolCode.ERROR_CODE_RESOLVE_HAS_TAIL_NO_HEAD:
                description = " onFailHasTailNoHead ";
                break;
            case ProtocolCode.ERROR_CODE_RESOLVE_HAS_HEAD_NO_TAIL:
                description = " onFailHasHeadNoTail ";
                break;
            case ProtocolCode.ERROR_CODE_RESOLVE_MORE_LENGTH:
                description = " onFailLengthTooLong ";
                break;
            case ProtocolCode.ERROR_CODE_INTERNAL_PKG_NULL:
                description = " onInternalFailPackNull ";
                break;
            case ProtocolCode.ERROR_CODE_NO_TAIL:
                description = " onPackNoTailError ";
                break;
            case ProtocolCode.ERROR_CODE_NO_HEAD:
                description = " onPackNoHeadError ";
                break;
            case ProtocolCode.ERROR_CODE_CRC:
                description = " onPackCrcError ";
                break;
            case ProtocolCode.ERROR_CODE_RESOLVE_TYPE:
                description = " onFailNoType ";
                break;
            case ProtocolCode.ERROR_CODE_RESOLVE_CMD:
                description = " onFailNoCmd ";
                break;
            default:
                description = " onThisErrorCode ";
                break;
        }

        Tlog.e(TAG, description);
        mTask.description = description;
        if (mSocketDataArray != null) {
            mTask.type = mSocketDataArray.getProtocolType();
            mTask.cmd = mSocketDataArray.getProtocolCmd();
            mTask.mac = mSocketDataArray.getID();
            mTask.data = mSocketDataArray.toArray();
        }

    }

}
