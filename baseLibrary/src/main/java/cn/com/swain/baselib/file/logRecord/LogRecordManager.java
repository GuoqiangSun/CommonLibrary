package cn.com.swain.baselib.file.logRecord;

import java.io.File;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public class LogRecordManager extends LogRecordWrapper {

    private final LogRecordClient mLogRecordClient;

    public LogRecordManager(File logPath, String prefix) {
        mLogRecordClient = new LogRecordClient(logPath, prefix);
    }

    private boolean init = false;

    public void init() {
        if (init) {
            throw new RuntimeException(" on init(); LogRecordClient has already init. No repetition init ");
        }
        init = true;
        mLogRecordClient.initWriteThread();
        attachIRecordMsgFile(mLogRecordClient);
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
     * 检测文件记录客户端是否正在运行
     */
    public void checkBufferWriter() {
        checkInit();
        mLogRecordClient.checkBufferWriter();
    }

    /**
     * 数据同步到磁盘
     */
    public void syncBufferWriter() {
        checkInit();
        mLogRecordClient.syncBufferWriter();
    }

    /**
     * 释放资源
     */
    public void releaseBufferWriter() {
        checkInit();
        mLogRecordClient.releaseBufferWriter();
    }


}
