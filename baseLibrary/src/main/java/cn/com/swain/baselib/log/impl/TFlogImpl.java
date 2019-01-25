package cn.com.swain.baselib.log.impl;

import java.io.File;

import cn.com.swain.baselib.log.logRecord.AbsLogRecord;
import cn.com.swain.baselib.log.logRecord.impl.LogRecordManager;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/12 0012
 * desc : 文件录制
 */
public class TFlogImpl {

    public TFlogImpl() {
    }

    private AbsLogRecord mRecordMsg;

    /**
     * 设置录制路径
     * <p>
     * 如果路径不存在,则创建,如果创建失败,则抛异常
     *
     * @param logPath 录制路径
     * @return 是否设置成功
     */
    public synchronized boolean set(File logPath) {
        return set(new LogRecordManager(logPath));
    }

    /**
     * 如果 {@link #mRecordMsg}不为null 和 参数是同一实例 则成功,否则失败
     * <p>
     * if set
     * <p>
     * when main Activity onCreate() ,need call {@link #startRecord()}
     * when main Activity onDestroy() ,if you need not record,need call {@link #startRecord()}
     *
     * @param recordMsg you can use {@link LogRecordManager }
     * @return true or false
     */
    public synchronized boolean set(AbsLogRecord recordMsg) {

        if (hasILogRecordImpl()) {
            return mRecordMsg == recordMsg;
        }
        if (recordMsg != null && !recordMsg.isInit()) {
            recordMsg.initLogFile();
        }
        mRecordMsg = recordMsg;
        return true;
    }

    /**
     * 移除录制
     * <p>
     * 如果 {@link #mRecordMsg}为null 则 true
     * 如果 {@link #mRecordMsg}不为null 和 参数是同一实例 则成功
     *
     * @param recordMsg {@link LogRecordManager }
     * @return 是否移除成功
     */
    public synchronized boolean remove(AbsLogRecord recordMsg) {
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

    /**
     * {@link #remove(AbsLogRecord)}
     */
    public synchronized boolean remove() {
        return remove(mRecordMsg);
    }

    /**
     * 是否有录制实例
     *
     * @return boolean
     */
    public boolean hasILogRecordImpl() {
        return mRecordMsg != null;
    }

    // 是否正在录制log中
    public boolean isRecording() {
        if (mRecordMsg != null) {
            return mRecordMsg.isRecording();
        }
        return false;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        synchronized (TFlogImpl.class) {
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
    private volatile int record = 0;

    /**
     * {@link #record};
     */
    public int getStartTimes() {
        return record;
    }

    /**
     * 同步数据到磁盘
     */
    public void syncRecordData() {
        if (mRecordMsg != null) {
            mRecordMsg.syncRecordData();
        }
    }

    /**
     * 停止录制
     */
    public void stopRecord() {

        synchronized (TFlogImpl.class) {
            record--;
        }

        if (record <= 0) {
            record = 0;
            if (mRecordMsg != null) {
                mRecordMsg.stopRecord();
            }
        }
    }

    /**
     * 强制停止录制日志
     */
    public void forceStopRecord() {
        record = 0;
        if (mRecordMsg != null) {
            mRecordMsg.stopRecord();
        }
    }

    public void v(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, msg);
        }
    }

    public void v(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, msg, e);
        }
    }

    public void v(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgV(TAG, e);
        }
    }

    public void d(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, msg);
        }
    }

    public void d(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, msg, e);
        }
    }

    public void d(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgD(TAG, e);
        }
    }

    public void i(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, msg);
        }
    }

    public void i(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, msg, e);
        }
    }

    public void i(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgI(TAG, e);
        }
    }

    public void w(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, msg);
        }
    }

    public void w(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, msg, e);
        }
    }

    public void w(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgW(TAG, e);
        }
    }

    public void e(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, msg);
        }
    }

    public void e(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, msg, e);
        }
    }

    public void e(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgE(TAG, e);
        }
    }

    public void a(String TAG, String msg) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, msg);
        }
    }

    public void a(String TAG, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, e);
        }
    }

    public void a(String TAG, String msg, Throwable e) {
        if (mRecordMsg != null) {
            mRecordMsg.recordMsgA(TAG, msg, e);
        }
    }
}
