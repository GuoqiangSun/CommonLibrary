package cn.com.common.test.shake;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/9 0009
 * desc :
 */

import android.app.Activity;
import android.os.Bundle;


public class ShakeActivity extends Activity {

    ShakeUtils mShakeUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mShakeUtils = new ShakeUtils(getApplication());
        mShakeUtils.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShakeUtils != null) {
            mShakeUtils.onDestroy();
        }
    }
}
