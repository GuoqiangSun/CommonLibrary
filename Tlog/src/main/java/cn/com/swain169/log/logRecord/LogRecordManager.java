package cn.com.swain169.log.logRecord;

import java.io.File;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public class LogRecordManager extends LogRecordWrapper {

    private final LogRecordClient mLogRecordClient;

    public LogRecordManager(File logPath, String prefix, long size) {
        mLogRecordClient = new LogRecordClient(logPath, prefix, size);
    }

    private boolean init = false;

    public void init() {
        if (init) {
            throw new RuntimeException(" on init(); LogRecordClient has already init. No repetition init ");
        }
        init = true;
        mLogRecordClient.initWriteThread();
        mLogRecordClient.checkBufferWriter();
        attachIRecordMsgFile(mLogRecordClient);
        recordMsgV("LogRecordClient", " LogRecordClient init success...");
    }

    public void release() {
        if (!init) {
            throw new RuntimeException(" on release(); LogRecordClient is not init. can not release ");
        }
        init = false;
        mLogRecordClient.releaseWriteThread();
    }

    private void checkInit() {
        if (!init) {
            throw new RuntimeException(" LogRecordClient is not init ,you can not exe this method");
        }
    }

    /**
     * 检测是否在录制
     * 如果没有录制则开启录制
     */
    public void checkIsRecord() {
        checkInit();
        mLogRecordClient.checkBufferWriter();
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


}
