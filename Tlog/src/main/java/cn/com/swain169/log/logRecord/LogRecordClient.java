package cn.com.swain169.log.logRecord;

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

        if (fileRecordSize < -1 || fileRecordSize > 1024 * 1024 * 512) {
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
    public void initWriteThread() {

        if (mHT == null) {
            synchronized (synObj) {
                if (mHT == null) {
                    mHT = new HandlerThread("writeMsg");
                    mHT.start();
                    mHandler = new FileRecordHandler(mHT.getLooper(), this);
                }
            }
        }

    }

    /**
     * 释放写线程
     */
    public void releaseWriteThread() {
        if (mHT != null) {
            synchronized (synObj) {
                if (mHT != null) {
                    mHandler = null;
                    mHT.quitSafely();
                    mHT = null;
                }
            }
        }
    }


    /**
     * 检测FileWriter是否创建，没有创建则创建.
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_CHECK_BUFFER_WRITE }
     */
    public void checkBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_CHECK_BUFFER_WRITE);
        }
    }

    /**
     * 释放FileWriter资源.
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_RELEASE_BUFFER_WRITE }
     */
    public void releaseBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_RELEASE_BUFFER_WRITE);
        }
    }

    /**
     * 同步FileWriter数据
     * 此操作属于线程安全操作
     * {@link #MSG_WHAT_RELEASE_BUFFER_WRITE }
     */
    public void syncBufferWriter() {
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


    private final SimpleDateFormat mRecordDateFormat;


    private int writeTimes = 0;
    /**
     * 最大写1024次同步一次数据
     */
    private static final int MAX_WRITE_BUFFER_TIMES = 1024;
    /**
     * 最大等10分钟同步一次数据
     */
    private static final long MAX_WAIT_WRITE_TIME = 1000 * 60 * 10;

    private long lastWriteTm;

    private void handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_WHAT_RECORD:

                long curTMs = System.currentTimeMillis();

                writeMsgInThread(msg.obj, curTMs);

                if (++writeTimes >= MAX_WRITE_BUFFER_TIMES
                        || Math.abs(curTMs - lastWriteTm) > MAX_WAIT_WRITE_TIME) {
                    syncBufferWriterInThread();

                    if (msgFile != null && msgFile.length() >= fileRecordSize) {
                        reGeneralBufferWriterInThread();
                    }

                }

                lastWriteTm = curTMs;
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
                + "_"
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

    private int getTid() {
        return Process.myTid();
    }

    @Override
    public void recordMsgV(String TAG, String msg) {

        if (mHandler != null) {
            String log = getTid() + " V/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = getTid() + " V/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " V/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgD(String TAG, String msg) {

        if (mHandler != null) {
            String log = getTid() + " D/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = getTid() + " D/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " D/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgI(String TAG, String msg) {

        if (mHandler != null) {
            String log = getTid() + " I/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = getTid() + " I/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " I/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgW(String TAG, String msg) {

        if (mHandler != null) {
            String log = getTid() + " W/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = getTid() + " W/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " W/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgE(String TAG, String msg) {

        if (mHandler != null) {
            String log = getTid() + " E/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = getTid() + " E/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " E/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }


    @Override
    public void recordMsgA(String TAG, String msg) {
        if (mHandler != null) {
            String log = getTid() + " A/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgA(String TAG, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " A/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgA(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = getTid() + " A/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }


    private static final class FileRecordHandler extends Handler {

        private WeakReference<LogRecordClient> wr;

        FileRecordHandler(Looper mLooper, LogRecordClient mFileRecordClient) {
            super(mLooper);
            wr = new WeakReference<>(mFileRecordClient);
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
