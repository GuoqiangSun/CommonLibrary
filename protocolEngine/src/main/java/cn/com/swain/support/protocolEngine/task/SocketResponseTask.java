package cn.com.swain.support.protocolEngine.task;

import cn.com.swain.baselib.app.Tlog;
import cn.com.swain.support.protocolEngine.IO.IDataProtocolOutput;
import cn.com.swain.support.protocolEngine.pack.ResponseData;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public abstract class SocketResponseTask extends AbsSocketDataTask {

    public static final String TAG = "AbsProtocolTask";

    private IDataProtocolOutput mResponse;

    public SocketResponseTask(IDataProtocolOutput mResponse) {
        this.mResponse = mResponse;
    }

    public SocketResponseTask() {
    }

    /**
     * 发送数据给客户端
     *
     * @param mResponseData
     */
    protected void response(ResponseData mResponseData) {

        if (mResponse != null) {
            mResponse.onOutputDataToServer(mResponseData);
        } else {
            Tlog.e(TAG, " response mResponse == null ");
        }

    }
}
