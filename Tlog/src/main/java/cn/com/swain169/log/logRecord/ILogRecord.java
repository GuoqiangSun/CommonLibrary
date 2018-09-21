package cn.com.swain169.log.logRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public interface ILogRecord {

    void recordMsgV(String TAG, String msg);

    void recordMsgV(String TAG, Throwable e);

    void recordMsgV(String TAG, String msg, Throwable e);


    void recordMsgD(String TAG, String msg);

    void recordMsgD(String TAG, Throwable e);

    void recordMsgD(String TAG, String msg, Throwable e);


    void recordMsgI(String TAG, String msg);

    void recordMsgI(String TAG, Throwable e);

    void recordMsgI(String TAG, String msg, Throwable e);


    void recordMsgW(String TAG, String msg);

    void recordMsgW(String TAG, Throwable e);

    void recordMsgW(String TAG, String msg, Throwable e);


    void recordMsgE(String TAG, String msg);

    void recordMsgE(String TAG, Throwable e);

    void recordMsgE(String TAG, String msg, Throwable e);


    void recordMsgA(String TAG, String msg);

    void recordMsgA(String TAG, Throwable e);

    void recordMsgA(String TAG, String msg, Throwable e);

}
