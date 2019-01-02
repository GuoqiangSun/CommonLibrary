package cn.com.swain.baselib.log.logRecord.impl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.swain.baselib.log.logRecord.ILogRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public class LogRecordClient implements ILogRecord {

    /**
     * 文件的目录
     */
    private final File logRootDir;

    /**
     * 文件的前缀名
     */
    private final String fileNamePrefix;

    /**
     * 文件的后缀名
     */
    private static final String fileNameSuffix = ".log";

    /**
     * 一个文件录制的最大大小
     */
    private final long fileRecordSize;

    private static final int DEFAULT_RECORD_FILE_LENGTH = 1024 * 1024 * 6;

    public static final long MAX_RECORD_SIZE = 1024 * 1024 * 512L;

    LogRecordClient(File logRootDir, String fileNamePrefix, long fileRecordSize) {
        if (logRootDir == null) {
            throw new RuntimeException("(LogRecordClient) logRootDir not found");
        }
        if (!logRootDir.exists()) {
            boolean mkdirs = logRootDir.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("(LogRecordClient) logRootDir mkdirs fail");
            }
        }

        this.logRootDir = logRootDir;

        if (fileNamePrefix != null) {
            this.fileNamePrefix = fileNamePrefix;
        } else {
            this.fileNamePrefix = "log";
        }

        if (fileRecordSize < -1 || fileRecordSize > MAX_RECORD_SIZE) {
            this.fileRecordSize = DEFAULT_RECORD_FILE_LENGTH;
        } else {
            this.fileRecordSize = fileRecordSize;
        }

        this.mRecordDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    }

    private FileRecordHandler mHandler;
    private HandlerThread mHT;

    private final Object synObj = new byte[1];

    /**
     * 初始化写线程
     */
    void initWriteThread() {

        if (mHT == null) {
            synchronized (synObj) {
                if (mHT == null) {
                    mHT = new HandlerThread("writeLog");
                    mHT.start();
                    mHandler = new FileRecordHandler(mHT.getLooper(), this);
                }
            }
        }

    }

    /**
     * 释放写线程
     */
    void releaseWriteThread() {
        if (mHT != null) {
            synchronized (synObj) {
                if (mHT != null) {
                    if (mHandler != null) {
                        mHandler.release();
                    }
                    mHandler = null;
                    mHT.quitSafely();
                    mHT = null;
                }
            }
        }
    }

    /**
     * 检测mBufferWriter是否已经创建
     */
    synchronized boolean checkBufferWriter() {
        return mBufferWriter == null;
    }

    /**
     * 检测FileWriter是否创建，没有创建则创建.
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_CHECK_BUFFER_WRITE }
     */
    void createBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_CHECK_BUFFER_WRITE);
            // 开启同步
            mHandler.sendEmptyMessageDelayed(MSG_WHAT_SYNC_CYCLE, MAX_WAIT_SYNC_TIME);
        }
    }

    /**
     * 释放FileWriter资源.
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_RELEASE_BUFFER_WRITE }
     */
    void releaseBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_RELEASE_BUFFER_WRITE);
            mHandler.removeMessages(MSG_WHAT_SYNC_CYCLE);
        }
    }

    /**
     * 同步FileWriter数据
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_RELEASE_BUFFER_WRITE }
     */
    void syncBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_SYNC);
        }
    }


    /**
     * 检测文件是否存在
     * {@link #checkBufferWriteInThread()}
     */
    private static final int MSG_WHAT_CHECK_BUFFER_WRITE = 0x01;

    // 写数据
    private static final int MSG_WHAT_RECORD = 0x02;

    // 同步数据
    private static final int MSG_WHAT_SYNC = 0x03;

    /**
     * 释放写的资源
     * {@link #releaseBufferWriteInThread()} ()}
     */
    private static final int MSG_WHAT_RELEASE_BUFFER_WRITE = 0x04;

    // 重新创建
    private static final int MSG_WHAT_RE_GENERAL = 0x05;

    // 周期同步
    private static final int MSG_WHAT_SYNC_CYCLE = 0x06;


    private final SimpleDateFormat mRecordDateFormat;


    private int writeTimes = 0;
    /**
     * 最大写1024次同步一次数据
     */
    private static final int MAX_WRITE_BUFFER_TIMES = 1024;
    /**
     * 最大等10分钟同步一次数据
     */
    private static final long MAX_WAIT_SYNC_TIME = 1000 * 60 * 10;


    private void handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_WHAT_RECORD:

                long curTMs = System.currentTimeMillis();

                writeMsgInThread(msg.obj, curTMs);

                if (++writeTimes >= MAX_WRITE_BUFFER_TIMES) {
                    syncBufferWriterInThread();

                    if (msgFile != null && msgFile.length() >= fileRecordSize) {
                        reGeneralBufferWriterInThread();
                    }

                }

                break;

            case MSG_WHAT_SYNC:

                syncBufferWriterInThread();

                break;
            case MSG_WHAT_RELEASE_BUFFER_WRITE:

                releaseBufferWriteInThread();

                break;

            case MSG_WHAT_RE_GENERAL:

                reGeneralBufferWriterInThread();

                break;

            case MSG_WHAT_CHECK_BUFFER_WRITE:

                checkBufferWriteInThread();

                break;

            case MSG_WHAT_SYNC_CYCLE:

                if (mBufferWriter != null) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_SYNC_CYCLE, MAX_WAIT_SYNC_TIME);
                    }
                    if (writeTimes > 0) {
                        syncBufferWriterInThread();
                    }
                }

                break;

        }

    }

    private void writeMsgInThread(Object obj, long ts) {

        if (mBufferWriter != null) {
            try {
                mBufferWriter.write(mRecordDateFormat.format(ts));
                mBufferWriter.write(" ");
                mBufferWriter.write(String.valueOf(obj));
                mBufferWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkBufferWriteInThread() {

        if (mBufferWriter == null) {
            reGeneralBufferWriterInThread();
        }
    }

    private File msgFile;
    private BufferedWriter mBufferWriter;

    private void reGeneralBufferWriterInThread() {

        releaseBufferWriteInThread();

        msgFile = generalSaveFile();
        mBufferWriter = generalBufferedWriter(msgFile);

    }

    private File generalSaveFile() {

        SimpleDateFormat mFileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        final String name = fileNamePrefix
                + "_"
                + mFileNameDateFormat.format(new Date())
                + "_p"
                + String.valueOf(Process.myPid())
                + fileNameSuffix;
        return new File(logRootDir, name);
    }

    private BufferedWriter generalBufferedWriter(File fd) {
        if (fd == null) {
            return null;
        }
        try {
            return new BufferedWriter(new FileWriter(fd));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void syncBufferWriterInThread() {

        if (mBufferWriter != null) {
            try {
                mBufferWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeTimes = 0;
    }

    private void releaseBufferWriteInThread() {
        if (mBufferWriter != null) {
            try {
                mBufferWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mBufferWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mBufferWriter = null;
        msgFile = null;
    }

    @Override
    public void recordMsgV(String TAG, String msg) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.VERBOSE);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, Throwable e) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.VERBOSE);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.VERBOSE);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    @Override
    public void recordMsgD(String TAG, String msg) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.DEBUG);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, Throwable e) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.DEBUG);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.DEBUG);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    @Override
    public void recordMsgI(String TAG, String msg) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.INFO);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, Throwable e) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.INFO);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.INFO);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    @Override
    public void recordMsgW(String TAG, String msg) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.WARN);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, Throwable e) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.WARN);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.WARN);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    @Override
    public void recordMsgE(String TAG, String msg) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.ERROR);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, Throwable e) {

        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.ERROR);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.ERROR);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }


    @Override
    public void recordMsgA(String TAG, String msg) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, Log.ASSERT);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    @Override
    public void recordMsgA(String TAG, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, e, Log.ASSERT);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }

    }

    @Override
    public void recordMsgA(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            LogMsg mLogMsg = new LogMsg(TAG, msg, e, Log.ASSERT);
            mHandler.obtainMessage(MSG_WHAT_RECORD, mLogMsg).sendToTarget();
        }
    }

    private static final class LogMsg {

        LogMsg() {
            this(null, null, null, 0);
        }

        LogMsg(String TAG, String msg, int level) {
            this(TAG, msg, null, level);
        }

        LogMsg(String TAG, Throwable e, int level) {
            this(TAG, null, e, level);
        }

        LogMsg(String TAG, String msg, Throwable e, int level) {
            this.tid = Process.myTid();
            this.TAG = TAG;
            this.msg = msg;
            this.e = e;
            this.level = level;
        }

        int tid;
        String TAG;
        int level;
        String msg;
        Throwable e;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(tid);
            switch (level) {
                case Log.VERBOSE:
                    sb.append(" V/");
                    break;
                case Log.DEBUG:
                    sb.append(" D/");
                    break;
                case Log.INFO:
                    sb.append(" I/");
                    break;
                case Log.WARN:
                    sb.append(" W/");
                    break;
                case Log.ERROR:
                    sb.append(" E/");
                    break;
                case Log.ASSERT:
                    sb.append(" A/");
                    break;
                default:
                    sb.append(" P/");
                    break;
            }

            sb.append(TAG).append(": ");
            if (msg != null) {
                sb.append(msg);
            }
            if (e != null) {
                sb.append(Log.getStackTraceString(e));
            }
            return sb.toString();
        }
    }

    private static final class FileRecordHandler extends Handler {

        private WeakReference<LogRecordClient> wr;

        FileRecordHandler(Looper mLooper, LogRecordClient mFileRecordClient) {
            super(mLooper);
            wr = new WeakReference<>(mFileRecordClient);
        }

        void release() {
            if (wr != null) {
                wr.clear();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LogRecordClient fileRecordClient;

            if (wr != null && (fileRecordClient = wr.get()) != null) {
                fileRecordClient.handleMessage(msg);
            }

        }
    }

}
