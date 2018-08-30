package cn.com.common.support.protocolEngine.result;

import cn.com.common.support.protocolEngine.datagram.SocketDataArray;
import cn.com.common.support.protocolEngine.task.FailTaskResult;
import cn.com.common.support.protocolEngine.task.ProtocolErrorTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/1 0001
 * desc :
 */
public abstract class SimpleProtocolResult implements IProtocolAnalysisResult {

    @Override
    public void onFailReceiveDataNull(int errorCode) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(null);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onFailLengthTooLong(int errorCode) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(null);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onFailHasHeadNoTail(int errorCode) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(null);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onFailHasTailNoHead(int errorCode) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(null);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onPackNullError(int errorCode) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(null);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onPackNoHeadError(int errorCode, SocketDataArray mSocketDataArray) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(mSocketDataArray);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onPackCrcError(int errorCode, SocketDataArray mSocketDataArray) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(mSocketDataArray);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    @Override
    public void onPackNoTailError(int errorCode, SocketDataArray mSocketDataArray) {
        ProtocolErrorTask protocolErrorTask = new ProtocolErrorTask(errorCode);
        protocolErrorTask.execute(mSocketDataArray);
        FailTaskResult failTaskResult = protocolErrorTask.getFailTaskResult();
        onFail(failTaskResult);
    }

    public abstract void onFail(FailTaskResult failTaskResult);

}
