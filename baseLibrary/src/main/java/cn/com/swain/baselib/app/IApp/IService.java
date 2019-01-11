package cn.com.swain.baselib.app.IApp;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public interface IService {

    /**
     * Activity onCreate();
     */
    void onSCreate();

    /**
     * Activity onResume();
     */
    void onSResume();

    /**
     * Activity onPause();
     */
    void onSPause();

    /**
     * when call activity finish(),
     * callBack this method;
     */
    void onSFinish();

    /**
     * Activity onDestroy();
     */
    void onSDestroy();


}
