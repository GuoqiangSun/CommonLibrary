package cn.com.swain169.log;

import cn.com.swain169.log.logRecord.AbsLogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/12 0012
 * desc : 文件录制
 */
public class TFlog {

    private TFlog() {
    }

    private static AbsLogRecord mRecordMsg;

    /**
     * when main Activity onCreate() ,need call {@link #startRecord()}
     * <p>
     * when main Activity onDestroy() ,if you need not record ,need call {@link #stopRecord()}.
     *
     * @param recordMsg you can use {@link cn.com.swain169.log.logRecord.impl.LogRecordManager }
     * @return true or false
     */
    public static synchronized boolean set(AbsLogRecord recordMsg) {

        if (hasILogRecordImpl()) {
            return false;
        }
        if (recordMsg != null && !recordMsg.isInit()) {
            recordMsg.initLogFile();
        }
        mRecordMsg = recordMsg;
        return true;
    }


    public static synchronized boolean remove(AbsLogRecord recordMsg) {
        AbsLogRecord mTmpRecordMsg = mRecordMsg;
        if (mTmpRecordMsg == null) {
            return true;
        }
        if (mTmpRecordMsg != recordMsg) {
            return false;
        }
        mRecordMsg = null;
        if (mTmpRecordMsg.isInit()) {
            mTmpRecordMsg.releaseLogFile();
        }
        return true;
    }

    public static synchronized boolean remove() {
        return remove(mRecordMsg);
    }

    public static boolean hasILogRecordImpl() {
        return mRecordMsg != null;
    }

    // 是否正在录制log中
    public static boolean isRecording() {
        if (mRecordMsg != null) {
            return mRecordMsg.isRecording();
        }
        return false;
    }

    // 开始录制
    public static void startRecord() {
        synchronized (TFlog.class) {
            record++;
        }
        if (record == 1) {
            if (mRecordMsg != null) {
                mRecordMsg.startRecord();
            }
        }
    }

    /**
     * when call {@link #startRecord()} , itself++
     */
    private static volatile int record = 0;

    public static int getStartTimes() {
        return record;
    }

    //同步数据到磁盘
    public static void syncRecordData() {
        if (mRecordMsg != null) {
            mRecordMsg.syncRecordData();
        }

    }

    // 停止录制
    public static void stopRecord() {

        synchronized (TFlog.class) {
            record--;
        }

        if (record <= 0) {
            record = 0;
            if (mRecordMsg != null) {
                mRecordMsg.stopRecord();
            }
        }
    }

    public static void v(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, msg);
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, msg, e);
        }
    }

    public static void v(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, e);
        }
    }

    public static void d(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, msg);
        }
    }

    public static void d(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, msg, e);
        }
    }

    public static void d(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, e);
        }
    }

    public static void i(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, msg);
        }
    }

    public static void i(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, msg, e);
        }
    }

    public static void i(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, e);
        }
    }

    public static void w(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, msg);
        }
    }

    public static void w(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, msg, e);
        }
    }

    public static void w(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, e);
        }
    }

    public static void e(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, msg);
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, msg, e);
        }
    }

    public static void e(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, e);
        }
    }

    public static void a(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, msg);
        }
    }

    public static void a(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, e);
        }
    }

    public static void a(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, msg, e);
        }
    }
}
