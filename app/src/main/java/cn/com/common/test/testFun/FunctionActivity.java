package cn.com.common.test.testFun;

import android.Manifest;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.common.test.R;
import cn.com.swain.baselib.jsInterface.IotContent.request.DataContent;
import cn.com.swain.baselib.jsInterface.IotContent.request.business.AbsBusinessJson;
import cn.com.swain.baselib.jsInterface.IotContent.request.control.AbsControlJson;
import cn.com.swain.baselib.jsInterface.IotContent.response.ResponseMethod;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.app.utils.CpuUtil;
import cn.com.swain.baselib.permission.PermissionRequest;
import cn.com.swain.baselib.util.WiFiUtil;

public class FunctionActivity extends AppCompatActivity {



    TextView mWifiInfoTxt;

    PermissionRequest request;
    TextToSpeech textToSpeech;
    EditText mSpeechEdt;

    private String TAG = "testFun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        request = new PermissionRequest(this);

        mWifiInfoTxt = findViewById(R.id.wifi_mac_txt);

        mSpeechEdt = findViewById(R.id.speech_edt);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Tlog.v(TAG, " TextToSpeech onInit::" + status);
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

//        SipManager sipManager = SipManager.newInstance(getApplicationContext());

        testAbsBusinessJson();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request.release();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    public void getWiFiMac(View view) {


        request.requestPermission(new PermissionRequest.OnPermissionResult() {
            @Override
            public boolean onPermissionRequestResult(String permission, boolean granted) {

                mWifiInfoTxt.setText(WiFiUtil.getWifiMacAddress());

                return true;
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION);

    }

    public void speech(View view) {
        String s = mSpeechEdt.getText().toString();
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void testAbsBusinessJson() {

        JSONObject jsonObject = originaPkg();
        String s = jsonObject.toString();
        Tlog.d(s);

        DataContent mDataContent = new DataContent();
        mDataContent.setRootJsonStr(s);
        mDataContent.setRootJsonObj(jsonObject);
        Tlog.d("DataContent:" + mDataContent);


        AbsBusinessJson businessJsonBean1 = mDataContent.getBusinessJsonBean();

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
