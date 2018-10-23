package cn.com.swain169.log;

import android.util.Log;

import java.util.Formatter;

import cn.com.swain169.log.logRecord.AbsLogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/20 0020
 * desc :
 */

public class Tlog {

    private Tlog() {
    }


    /**
     * 设置是否开启日志
     *
     * @param flag
     */
    public static void setDebug(boolean flag) {
        DEBUG = flag;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    private static boolean DEBUG = true;

    /**
     * 设置是否保存文件到日志
     * <p>
     *
     * @param flag if flag is true
     *             need call this method {@link #regIRecordMsgFile(AbsLogRecord)}
     *             or {@link cn.com.swain169.log.TFlog#set(AbsLogRecord)}
     */
    public static void setLogRecordDebug(boolean flag) {
        LOG_RECORD_DEBUG = flag;
    }

    public static boolean isLogRecordDebug() {
        return LOG_RECORD_DEBUG;
    }

    private static boolean LOG_RECORD_DEBUG = false;

    /**
     * if regIRecordMsgFile
     * <p>
     * when main Activity onCreate() ,need call {@link #startRecord()}
     * when main Activity onDestroy() ,if you need not record,need call {@link #startRecord()}
     *
     * @param recordMsg you can use {@link cn.com.swain169.log.logRecord.impl.LogRecordManager }
     */
    public static void regIRecordMsgFile(AbsLogRecord recordMsg) {
        TFlog.set(recordMsg);
    }

    public static void unregTREcordMsgFile(AbsLogRecord recordMsg) {
        TFlog.remove(recordMsg);
    }

    public static void unregTREcordMsgFile() {
        TFlog.remove();
    }

    public static void startRecord() {
        TFlog.startRecord();
    }

    public static void stopRecord() {
        TFlog.stopRecord();
    }

    public static void syncRecordData() {
        TFlog.syncRecordData();
    }

    public static boolean hasILogRecordImpl() {
        return TFlog.hasILogRecordImpl();
    }

    public static boolean isRecording() {
        return TFlog.isRecording();
    }

    /**
     * 设置是否打印堆栈消息
     *
     * @param flag true print
     */
    public static void setPrintStackDebug(boolean flag) {
        LOG_PRINT_STACK = flag;
    }

    public static boolean isPrintStackDebug() {
        return LOG_PRINT_STACK;
    }

    private static boolean LOG_PRINT_STACK = false;

    private static final int PrintStackLine = 4;

    private static String reMsg(String msg, int l) {
        if (LOG_PRINT_STACK) {
            return getStackTrace(l) + "\n" + msg;
        }
        return msg;
    }

    public static void v(String TAG, String msg) {
        v(TAG, msg, PrintStackLine);
    }

    public static void v(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.v(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.v(TAG, msg);
            }
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.v(TAG, msg, e);
            }
        }
    }

    public static void v(String TAG, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.v(TAG, e);
            }
        }
    }

    public static void d(String TAG, String msg) {
        d(TAG, msg, PrintStackLine);
    }

    public static void d(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.d(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.d(TAG, msg);
            }
        }
    }

    public static void d(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.d(TAG, msg, e);
            }
        }
    }

    public static void d(String TAG, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.d(TAG, e);
            }
        }
    }


    public static void i(String TAG, String msg) {
        i(TAG, msg, PrintStackLine);
    }

    public static void i(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.i(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.i(TAG, msg);
            }
        }
    }

    public static void i(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.i(TAG, msg, e);
            }
        }
    }

    public static void i(String TAG, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.i(TAG, e);
            }
        }
    }


    public static void w(String TAG, String msg) {
        w(TAG, msg, PrintStackLine);
    }

    public static void w(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.w(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.w(TAG, msg);
            }
        }
    }

    public static void w(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.w(TAG, msg, e);
            }
        }
    }

    public static void w(String TAG, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.w(TAG, e);
            }
        }
    }


    public static void e(String TAG, String msg) {
        e(TAG, msg, PrintStackLine);
    }

    public static void e(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.e(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.e(TAG, msg);
            }
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.e(TAG, msg, e);
            }
        }
    }

    public static void e(String TAG, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.e(TAG, e);
            }
        }
    }


    public static void a(String TAG, String msg) {
        a(TAG, msg, PrintStackLine);
    }

    public static void a(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.e(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                TFlog.a(TAG, msg);
            }
        }
    }


    public static void a(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                TFlog.a(TAG, msg, e);
            }
        }
    }

    public static void a(String TAG, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                TFlog.a(TAG, e);
            }
        }
    }

    public static void pst(String TAG, String msg) {
        Throwable throwable = new Throwable();
        if (DEBUG) {
            Log.e(TAG, msg, throwable);
            if (LOG_RECORD_DEBUG) {
                TFlog.a(TAG, msg, throwable);
            }
        }
    }

    public static String getStackTrace(int l) {
        Throwable throwable = new Throwable();
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        if (l >= stackTrace.length) {
            return Log.getStackTraceString(throwable);
        }
        StackTraceElement targetElement = stackTrace[l];
        final String fileName = getFileName(targetElement);

//        return targetElement.toString();

        String tName = Thread.currentThread().getName();
        return new Formatter()
                .format("at[%s]%s.%s(%s:%d)",
                        tName,
                        targetElement.getClassName(),
                        targetElement.getMethodName(),
                        fileName,
                        targetElement.getLineNumber())
                .toString();
    }

    private static String getFileName(final StackTraceElement targetElement) {
        String fileName = targetElement.getFileName();
        if (fileName != null) return fileName;
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }
        int index = className.indexOf('$');
        if (index != -1) {
            className = className.substring(0, index);
        }
        return className + ".java";
    }


    /********/

    private static String TAG_GLOBAL = "swain";

    private static final int GlobalPrintStackLine = PrintStackLine;

    public static void setGlobalTag(String TAG) {
        TAG_GLOBAL = TAG;
    }

    public static void v(String msg) {
        v(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public static void v(Throwable e) {
        v(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void d(String msg) {
        d(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public static void d(Throwable e) {
        d(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void i(String msg) {
        i(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public static void i(Throwable e) {
        i(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void w(String msg) {
        w(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public static void w(Throwable e) {
        w(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void e(String msg) {
        e(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public static void e(Throwable e) {
        e(TAG_GLOBAL, "Throwable : ", e);
    }

    public static void pst(String msg) {
        pst(TAG_GLOBAL, msg);
    }
}
