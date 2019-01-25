package cn.com.swain.baselib.log.impl;

import android.util.Log;

import java.io.File;
import java.util.Formatter;

import cn.com.swain.baselib.log.logRecord.AbsLogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/20 0020
 * desc :
 */

public class TlogImpl extends TFlogImpl {

    public TlogImpl() {
    }

    private boolean DEBUG = true;

    /**
     * 设置是否开启日志
     */
    public void setDebug(boolean flag) {
        DEBUG = flag;
    }

    /**
     * 设置是否开启日志功能
     */
    public boolean isDebug() {
        return DEBUG;
    }

    private boolean LOG_RECORD_DEBUG = false;

    /**
     * 设置是否保存文件到日志
     *
     * @param flag if flag is true
     *             need call this method {@link #set(AbsLogRecord)}
     */
    public void setLogRecordDebug(boolean flag) {
        LOG_RECORD_DEBUG = flag;
    }

    /**
     * 是否开启了录制文件功能
     */
    public boolean isLogRecordDebug() {
        return LOG_RECORD_DEBUG;
    }

    @Override
    public synchronized boolean set(File logPath) {
        if (!LOG_RECORD_DEBUG) {
            return false;
        }
        return super.set(logPath);
    }

    @Override
    public synchronized boolean set(AbsLogRecord recordMsg) {
        if (!LOG_RECORD_DEBUG) {
            return false;
        }
        return super.set(recordMsg);
    }

    @Override
    public boolean isRecording() {
        return LOG_RECORD_DEBUG && super.isRecording();
    }

    @Override
    public void startRecord() {
        if (LOG_RECORD_DEBUG) {
            super.startRecord();
        }
    }

    private boolean LOG_PRINT_STACK = false;

    /**
     * 设置是否打印堆栈消息
     *
     * @param flag true print
     */
    public void setPrintStackDebug(boolean flag) {
        LOG_PRINT_STACK = flag;
    }

    /**
     * 是否开启打印堆栈消息功能
     */
    public boolean isPrintStackDebug() {
        return LOG_PRINT_STACK;
    }

    private final int PrintStackLine = 5;

    private String reMsg(String msg, int l) {
        if (LOG_PRINT_STACK) {
            return getStackTrace(l) + "\n" + msg;
        }
        return msg;
    }

    public void v(String TAG, String msg) {
        v(TAG, msg, PrintStackLine);
    }

    public void v(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.v(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.v(TAG, msg);
            }
        }
    }

    public void v(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.v(TAG, msg, e);
            }
        }
    }

    public void v(String TAG, Throwable e) {
        if (DEBUG) {
            Log.v(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.v(TAG, e);
            }
        }
    }

    public void d(String TAG, String msg) {
        d(TAG, msg, PrintStackLine);
    }

    public void d(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.d(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.d(TAG, msg);
            }
        }
    }

    public void d(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.d(TAG, msg, e);
            }
        }
    }

    public void d(String TAG, Throwable e) {
        if (DEBUG) {
            Log.d(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.d(TAG, e);
            }
        }
    }


    public void i(String TAG, String msg) {
        i(TAG, msg, PrintStackLine);
    }

    public void i(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.i(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.i(TAG, msg);
            }
        }
    }

    public void i(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.i(TAG, msg, e);
            }
        }
    }

    public void i(String TAG, Throwable e) {
        if (DEBUG) {
            Log.i(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.i(TAG, e);
            }
        }
    }


    public void w(String TAG, String msg) {
        w(TAG, msg, PrintStackLine);
    }

    public void w(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.w(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.w(TAG, msg);
            }
        }
    }

    public void w(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.w(TAG, msg, e);
            }
        }
    }

    public void w(String TAG, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.w(TAG, e);
            }
        }
    }


    public void e(String TAG, String msg) {
        e(TAG, msg, PrintStackLine);
    }

    public void e(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.e(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.e(TAG, msg);
            }
        }
    }

    public void e(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.e(TAG, msg, e);
            }
        }
    }

    public void e(String TAG, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.e(TAG, e);
            }
        }
    }


    public void a(String TAG, String msg) {
        a(TAG, msg, PrintStackLine);
    }

    public void a(String TAG, String msg, int l) {
        if (DEBUG) {
            Log.e(TAG, reMsg(msg, l));
            if (LOG_RECORD_DEBUG) {
                super.a(TAG, msg);
            }
        }
    }


    public void a(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, msg, e);
            if (LOG_RECORD_DEBUG) {
                super.a(TAG, msg, e);
            }
        }
    }

    public void a(String TAG, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "Throwable : ", e);
            if (LOG_RECORD_DEBUG) {
                super.a(TAG, e);
            }
        }
    }

    public void pst(String TAG, String msg) {
        Throwable throwable = new Throwable();
        if (DEBUG) {
            Log.w(TAG, msg, throwable);
            if (LOG_RECORD_DEBUG) {
                super.w(TAG, msg, throwable);
            }
        }
    }

    private String getStackTrace(int l) {
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

    private String getFileName(final StackTraceElement targetElement) {
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

    private String TAG_GLOBAL = "swain";

    private final int GlobalPrintStackLine = PrintStackLine;

    public void setGlobalTag(String TAG) {
        TAG_GLOBAL = TAG;
    }

    public void v(String msg) {
        v(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public void v(Throwable e) {
        v(TAG_GLOBAL, "Throwable : ", e);
    }

    public void d(String msg) {
        d(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public void d(Throwable e) {
        d(TAG_GLOBAL, "Throwable : ", e);
    }

    public void i(String msg) {
        i(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public void i(Throwable e) {
        i(TAG_GLOBAL, "Throwable : ", e);
    }

    public void w(String msg) {
        w(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public void w(Throwable e) {
        w(TAG_GLOBAL, "Throwable : ", e);
    }

    public void e(String msg) {
        e(TAG_GLOBAL, msg, GlobalPrintStackLine);
    }

    public void e(Throwable e) {
        e(TAG_GLOBAL, "Throwable : ", e);
    }

    public void pst(String msg) {
        pst(TAG_GLOBAL, msg);
    }
}
