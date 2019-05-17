package cn.com.common.test.test;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/9 0009
 * desc :
 */

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.swain.baselib.jsInterface.IotContent.request.DataContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.AbsBusinessJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.AbsControlJson;
import cn.com.swain.baselib.jsInterface.IotContent.response.ResponseMethod;
import cn.com.swain.baselib.log.Tlog;


public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        JSONObject jsonObject = originaPkg();
        String s = jsonObject.toString();
        Tlog.d(s);

        DataContent mDataContent = new DataContent();
        mDataContent.setRootJsonStr(s);
        mDataContent.setRootJsonObj(jsonObject);
        Tlog.d("DataContent:" + mDataContent);

        AbsBusinessJson businessJsonBean = mDataContent.getBusinessJsonBean();
        Tlog.d("getCmdByJson:" + businessJsonBean.getCmdByJson());
        Tlog.d("getCustomByJson:" + businessJsonBean.getCustomByJson());
        Tlog.d("getProductByJson:" + businessJsonBean.getProductByJson());
        Tlog.d("getResultByJson:" + businessJsonBean.getResultByJson());

        AbsControlJson controlJsonBean = mDataContent.getControlJsonBean();
        Tlog.d("getVerByJson:" + controlJsonBean.getVerByJson());
        Tlog.d("getTsByJson:" + controlJsonBean.getTsByJson());
        Tlog.d("getFromByJson:" + controlJsonBean.getFromByJson());
        Tlog.d("getToByJson:" + controlJsonBean.getToByJson());
        Tlog.d("getSessionByJson:" + controlJsonBean.getSessionByJson());
        Tlog.d("getAppidByJson:" + controlJsonBean.getAppidByJson());
        Tlog.d("getMsgtwByJson:" + controlJsonBean.getMsgtwByJson());


        originaPkg2();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void originaPkg2() {

        ResponseMethod responseMethod = new ResponseMethod();
        JSONObject obj = new JSONObject();
        try {
            obj.put("ver", 257);
            obj.put("ts", System.currentTimeMillis());
            obj.put("from", 1);
            obj.put("to", 2);
            obj.put("session", 689);
            obj.put("appid", 6948);
            obj.put("msgtw", 693659);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseMethod.memorControlContent(obj);

        JSONObject business = new JSONObject();
        try {
            business.put("custom", 8);
            business.put("product", 8);
            business.put("cmd", 102569865);
            business.put("result", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String s = responseMethod.toJsMethod(business);
        Tlog.v(" toJsMethod:" + s);
    }


    private JSONObject originaPkg() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("ver", 257);
            obj.put("ts", System.currentTimeMillis());
            obj.put("from", 1);
            obj.put("to", 2);
            obj.put("session", 689);
            obj.put("appid", 6948);
            obj.put("msgtw", 693659);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject business = new JSONObject();
        try {
            business.put("custom", 8);
            business.put("product", 8);
            business.put("cmd", 102569865);
            business.put("result", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put("content", business);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
