package cn.com.common.test.scanOR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import cn.com.common.test.R;
import cn.com.startai.scansdk.ChargerScanActivity;
import cn.com.swain.baselib.util.MD5Util;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain169.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/27 0027
 * Desc:
 */
public class ScanORCodeActivity extends AppCompatActivity {

    private String TAG = "scan";
    private TextView mScanInfoTxt;
    private TextView mSplitInfoTxt;
    private TextView mPwdInfoTxt;

    private String mScanResult;
    private String mSplitResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_or);

        mScanInfoTxt = findViewById(R.id.scan_info);
        mSplitInfoTxt = findViewById(R.id.split_info);
        mPwdInfoTxt = findViewById(R.id.pwd_info);
        TextView mSecretTxt = findViewById(R.id.secret_info);
        mSecretTxt.setText(mSecretKey);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void scanOR(View view) {

        ChargerScanActivity.showActivityForResult(this, 1002);
    }


    public void splitORCode(View view) {

        String scanResult = mScanResult;

        if (scanResult == null) {
            Toast.makeText(getApplicationContext(), " scan ORCode is null ", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] split = scanResult.split("/");

        if (split.length == 0) {
            Toast.makeText(getApplicationContext(), " split ORCode is null ", Toast.LENGTH_SHORT).show();
            return;
        }

        String s = split[split.length - 1];
        mSplitResult = s;

        if (mSplitInfoTxt != null) {
            mSplitInfoTxt.setText(s);
        } else {
            Toast.makeText(getApplicationContext(), "split" + String.valueOf(s), Toast.LENGTH_SHORT).show();
        }

    }

    private String getSeq(int seq) {
        String s = String.valueOf(seq);
        if (s.length() <= 0) {
            s = "00";
        } else if (s.length() == 1) {
            s = "0" + s;
        } else if (s.length() > 2) {
            s = s.substring(0, 2);
        }
        return s;
    }

    final String mSecretKey = "52993c8c4e81e97ac1683b96b7c25d82";

    public void calcul(View view) {

        String deviceSeq = mSplitResult;
        if (deviceSeq == null) {
            Toast.makeText(getApplicationContext(), "calcul device seq is null", Toast.LENGTH_SHORT).show();
            return;
        }

        mPwdInfoTxt.setText("");

        String pre = "" + deviceSeq;

        for (int i = 0; i < 40; i++) {
            String seq = getSeq(i);
//            String seq = String.valueOf(i);
            String msg = pre + seq;


            Tlog.v(TAG, " calcul str:" + msg);

            try {

                String strMD5 = MD5Util.getStrMD5(msg);
                byte[] bytes = StrUtil.splitHexStr(strMD5);
                String hexMd5 = StrUtil.toString(bytes);
                Tlog.v(TAG, " hexMd5:" + hexMd5);

                String pwd = calculPwd2(bytes);

                if (mPwdInfoTxt != null) {
                    mPwdInfoTxt.append("md5:" + strMD5 + "\n");
                    mPwdInfoTxt.append("hexMd5:" + hexMd5 + "\n");
                    mPwdInfoTxt.append(" seq:" + seq + "  pwd:" + pwd + "\n\n");
                }


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                if (mPwdInfoTxt != null) {
                    mPwdInfoTxt.append("calcul error\n\n");
                }
                Toast.makeText(getApplicationContext(), "calcul md5 error ", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private String calculPwd(byte[] md5) {
        int i = ((md5[13] & 0xFF) * 65535 + (md5[14] & 0xFF) * 256 + (md5[15] & 0xFF)) % 100000;

        String s = String.valueOf(i);

        if (s.length() == 4) {
            s = "0" + s;
        } else if (s.length() == 3) {
            s = "00" + s;
        } else if (s.length() == 2) {
            s = "000" + s;
        } else if (s.length() == 1) {
            s = "0000" + s;
        } else if (s.length() == 0) {
            s = "00000" + s;
        }
        return s;
    }

    private String calculPwd2(byte[] md5) {

        int a = ((md5[14] & 0xFF) & 0x03) + 1;
        int b = (((md5[15] & 0xFF) >> 6) & 0x03) + 1;
        int c = (((md5[15] & 0xFF) >> 4) & 0x03) + 1;
        int d = (((md5[15] & 0xFF) >> 2) & 0x03) + 1;
        int e = ((md5[15] & 0xFF) & 0x03) + 1;

        Tlog.v(TAG, " calculPwd2 : a:" + a + " b:" + b + " c:" + c + " d:" + d + " e:" + e);
        Tlog.v(TAG, " calculPwdH : a:" + Integer.toHexString(a)
                + " b:" + Integer.toHexString(b)
                + " c:" + Integer.toHexString(c)
                + "d:" + Integer.toHexString(d)
                + " e:" + Integer.toHexString(e));

        return String.valueOf(a)
                + String.valueOf(b)
                + String.valueOf(c)
                + String.valueOf(d)
                + String.valueOf(e);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1002) {
                String scanResult = data.getStringExtra("result");
                Tlog.i(TAG, "scanResult = " + scanResult);

                mScanResult = scanResult;
                if (mScanInfoTxt != null) {
                    mScanInfoTxt.setText(scanResult);
                } else {
                    Toast.makeText(getApplicationContext(), "scan" + String.valueOf(scanResult), Toast.LENGTH_SHORT).show();
                }


            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Tlog.e(TAG, " QR scan fail user cancel ");
        } else {
            Tlog.e(TAG, " QR scan fail unknown ");
            Toast.makeText(getApplicationContext(), "QR scan fail", Toast.LENGTH_SHORT).show();
        }

    }

}
