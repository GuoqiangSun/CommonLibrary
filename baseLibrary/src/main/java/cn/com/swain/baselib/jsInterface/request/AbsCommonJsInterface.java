package cn.com.swain.baselib.jsInterface.request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.AbsJsInterface;

/**
 * author Guoqiang_Sun
 * date 2019/5/15
 * desc
 */
public abstract class AbsCommonJsInterface
        extends AbsCommonJsInterfaceWrapper
        implements IJSRequest {

    public AbsCommonJsInterface(String name) {
        super(name);
    }

    public AbsCommonJsInterface(String name, AbsJsInterface mJsInterface) {
        super(name, mJsInterface);
    }

    @Override
    public void handleJsRequest(String jsonData) {
        JSONObject json;
        try {
            json = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            onJsDataParseError(e, jsonData);
            return;
        }
        handleJsRequest(jsonData, json);
    }

    protected abstract void handleJsRequest(String jsonData, JSONObject json);

    protected abstract void onJsDataParseError(JSONException e, String jsonData);
}
