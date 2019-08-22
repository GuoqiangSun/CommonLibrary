package cn.com.swain.baselib.jsInterface.example;

import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.request.DataContent;
import cn.com.swain.baselib.jsInterface.request.AbsCommonJsInterface;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public class _CommonJsInterfaceTask extends AbsCommonJsInterface {

    private IJsInterfaceRequest mIJsInterfaceRequest;

    public _CommonJsInterfaceTask(Looper mLooper, IJsInterfaceRequest mIJsInterfaceRequest) {
        super("_CommonJsInterfaceTask");
        final _X5CommonJsRequestInterface commonJsRequest = new _X5CommonJsRequestInterface(mLooper, this);
        attachAbsJsInterface(commonJsRequest);
        this.mIJsInterfaceRequest = mIJsInterfaceRequest;
    }

    @Override
    protected void handleJsRequest(String jsonData, JSONObject json) {
        DataContent mDataContent = DataContent.newDataContent(json);
        mDataContent.setRootJsonStr(jsonData);
        if (mIJsInterfaceRequest != null) {
            mIJsInterfaceRequest.handleJsRequest(mDataContent);
        }
    }

    @Override
    protected void onJsDataParseError(JSONException e, String jsonData) {
        if (mIJsInterfaceRequest != null) {
            mIJsInterfaceRequest.onJsDataParseError(e, jsonData);
        }
    }

    @Override
    public void release() {
        mIJsInterfaceRequest = null;
        super.release();
    }

    public interface IJsInterfaceRequest {
        void handleJsRequest(DataContent mDataContent);

        void onJsDataParseError(Exception e, String jsonData);
    }
}
