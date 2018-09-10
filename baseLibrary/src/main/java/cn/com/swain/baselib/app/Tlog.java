package cn.com.swain.baselib.app;

import android.util.Log;

import cn.com.swain.baselib.file.logRecord.ILogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/20 0020
 * desc :
 */

public class Tlog {

    private Tlog() {
    }

    private static boolean DEBUG = true;

    static void setDebug(boolean flag) {
        DEBUG = flag;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    private static boolean LOG_RECORD_DEBUG = false;

    static void setLogRecordDebug(boolean flag) {
        LOG_RECORD_DEBUG = flag;
    }

    public static boolean isLogRecordDebug() {
        return LOG_RECORD_DEBUG;
    }

    private static ILogRecord mRecordMsg;

    public static void regIRecordMsgFile(ILogRecord recordMsg) {
        mRecordMsg = recordMsg;
    }

    public static void v(String TAG, String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgV(TAG, msg);
            }
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, msg, e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgV(TAG, msg, e);
            }
        }
    }

    public static void v(String TAG, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgV(TAG, e);
            }
        }
    }

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgD(TAG, msg);
            }
        }
    }

    public static void d(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, msg, e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgD(TAG, msg, e);
            }
        }
    }

    public static void d(String TAG, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgD(TAG, e);
            }
        }
    }

    public static void i(String TAG, String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgI(TAG, msg);
            }
        }
    }

    public static void i(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, msg, e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgI(TAG, msg, e);
            }
        }
    }

    public static void i(String TAG, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgI(TAG, e);
            }
        }
    }

    public static void w(String TAG, String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgW(TAG, msg);
            }
        }
    }

    public static void w(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, msg, e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgW(TAG, msg, e);
            }
        }
    }

    public static void w(String TAG, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgW(TAG, e);
            }
        }
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgE(TAG, msg);
            }
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, msg, e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgE(TAG, msg, e);
            }
        }
    }

    public static void e(String TAG, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG && mRecordMsg != null) {
                mRecordMsg.recordMsgE(TAG, e);
            }
        }
    }

    /********/

    private static String TAG_GLOBAL = "startai";

    static void setGlobalTag(String TAG) {
        TAG_GLOBAL = TAG;
    }

    public static void v(String msg) {
        v(TAG_GLOBAL, msg);
    }

    public static void v(Throwable e) {
        v(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void d(String msg) {
        d(TAG_GLOBAL, msg);
    }

    public static void d(Throwable e) {
        d(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void i(String msg) {
        i(TAG_GLOBAL, msg);
    }

    public static void i(Throwable e) {
        i(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void w(String msg) {
        w(TAG_GLOBAL, msg);
    }

    public static void w(Throwable e) {
        w(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void e(String msg) {
        e(TAG_GLOBAL, msg);
    }

    public static void e(Throwable e) {
        e(TAG_GLOBAL, "Throwable : ", e);
    }
}
