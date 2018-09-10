package cn.com.swain.support.protocolEngine.task;

import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public abstract class AbsSocketDataTask extends AbsProtocolTask<SocketDataArray, SocketDataArray> {

    public AbsSocketDataTask() {

    }

    @Override
    protected SocketDataArray onBackgroundTask(SocketDataArray mParams) {

        doTask(mParams);

        return mParams;
    }

    @Override
    protected void onPostExecute(SocketDataArray mResult) {
        super.onPostExecute(mResult);
        if (mResult != null) {
            mResult.setISUnUsed();
        }
    }


    protected abstract void doTask(SocketDataArray mSocketDataArray);

}
