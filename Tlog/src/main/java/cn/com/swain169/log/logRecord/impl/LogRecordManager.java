package cn.com.swain169.log.logRecord.impl;

import java.io.File;

import cn.com.swain169.log.logRecord.AbsLogRecord;
import cn.com.swain169.log.logRecord.ILogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public class LogRecordManager extends AbsLogRecord {

    private final LogRecordClient mLogRecordClient;
    private ILogRecord mILogRecord;

    /**
     * @param logPath 日志保存路径
     * @param prefix  日志名称前缀
     * @param size    单个日志文件大小
     */
    public LogRecordManager(File logPath, String prefix, long size) {
        this.mLogRecordClient = new LogRecordClient(logPath, prefix, size);
    }

    private boolean init = false;

    public void initLogFile() {
        if (this.init) {
            throw new RuntimeException(" on init(); LogRecordClient has already init. No repetition init ");
        }
        this.init = true;
        this.mLogRecordClient.initWriteThread();
        this.mLogRecordClient.checkBufferWriter();
        this.mILogRecord = this.mLogRecordClient;
        recordMsgV("LogRecordClient", " LogRecordClient init success...");
    }

    public void releaseLogFile() {
        if (!this.init) {
            throw new RuntimeException(" on release(); LogRecordClient is not init. can not release ");
        }
        this.init = false;
        this.mLogRecordClient.releaseWriteThread();
        this.mILogRecord = null;
    }

    private void checkInit() {
        if (!this.init) {
            throw new RuntimeException(" LogRecordClient is not init ,you can not exe this method");
        }
    }

    /**
     * 检测是否在录制
     * 如果没有录制则开启录制
     */
    public boolean checkIsRecord() {
        checkInit();
        return mLogRecordClient.checkBufferWriter();
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        checkInit();
        mLogRecordClient.createBufferWriter();
    }

    /**
     * 数据同步到磁盘
     */
    public void syncRecordData() {
        checkInit();
        mLogRecordClient.syncBufferWriter();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        checkInit();
        mLogRecordClient.releaseBufferWriter();
    }


    @Override
    public void recordMsgA(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgA(TAG, msg);
        }
    }

    @Override
    public void recordMsgA(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgA(TAG, e);
        }
    }

    @Override
    public void recordMsgA(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgA(TAG, msg, e);
        }
    }

    @Override
    public void recordMsgV(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgV(TAG, msg);
        }
    }

    @Override
    public void recordMsgV(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgV(TAG, e);
        }
    }

    @Override
    public void recordMsgV(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgV(TAG, msg, e);
        }
    }

    @Override
    public void recordMsgD(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgD(TAG, msg);
        }
    }

    @Override
    public void recordMsgD(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgD(TAG, e);
        }
    }

    @Override
    public void recordMsgD(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgD(TAG, msg, e);
        }
    }

    @Override
    public void recordMsgI(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgI(TAG, msg);
        }
    }

    @Override
    public void recordMsgI(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgI(TAG, e);
        }
    }

    @Override
    public void recordMsgI(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgI(TAG, msg, e);
        }
    }

    @Override
    public void recordMsgW(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgW(TAG, msg);
        }
    }

    @Override
    public void recordMsgW(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgW(TAG, e);
        }
    }

    @Override
    public void recordMsgW(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgW(TAG, msg, e);
        }
    }

    @Override
    public void recordMsgE(String TAG, String msg) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgE(TAG, msg);
        }
    }

    @Override
    public void recordMsgE(String TAG, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgE(TAG, e);
        }
    }

    @Override
    public void recordMsgE(String TAG, String msg, Throwable e) {
        if (mILogRecord != null) {
            mILogRecord.recordMsgE(TAG, msg, e);
        }
    }


}