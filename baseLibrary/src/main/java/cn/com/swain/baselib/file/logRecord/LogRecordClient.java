package cn.com.swain.baselib.file.logRecord;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.swain.baselib.file.FileUtil;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/15 0015
 * desc :
 */
public class LogRecordClient implements ILogRecord {

    private final File logRootDir;

    private final String fileNamePrefix;

    private static final String fileNameSuffix = ".log";

    LogRecordClient(File logRootDir, String fileNamePrefix) {
        if (logRootDir == null) {
            throw new RuntimeException("(LogRecordClient) logRootDir not found");
        }
        if (!logRootDir.exists()) {
            logRootDir.mkdirs();
        }
        this.logRootDir = logRootDir;
        if (fileNamePrefix != null) {
            this.fileNamePrefix = fileNamePrefix;
        } else {
            this.fileNamePrefix = "log";
        }

        mRecordDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    }

    private FileRecordHandler mHandler;
    private HandlerThread mHT;

    private final Object synObj = new byte[1];

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

        checkBufferWriter();
        recordMsgV("LogRecordClient", " LogRecordClient init success...");

    }


    public void releaseWriteThread() {
        mHandler = null;
        if (mHT != null) {
            synchronized (synObj) {
                if (mHT != null) {
                    mHT.quitSafely();
                    mHT = null;
                }
            }
        }
    }

    public void checkBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_CHECK_BUFFER_WRITE);
        }
    }

    public void releaseBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_RELEASE);
        }
    }

    public void syncBufferWriter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_WHAT_SYNC);
        }
    }


    private static final int MAX_WRITE_BUFFER_TIMES = 1024;

    // 写数据
    private static final int MSG_WHAT_RECORD = 0x01;

    // 同步数据
    private static final int MSG_WHAT_SYNC = 0x02;

    //释放数据
    private static final int MSG_WHAT_RELEASE = 0x03;

    // 重新创建
    private static final int MSG_WHAT_RE_GENERAL = 0x04;

    // 检测文件是否存在
    private static final int MSG_WHAT_CHECK_BUFFER_WRITE = 0x05;

    private final SimpleDateFormat mRecordDateFormat;

    private static final int MAX_RECORD_FILE_LENGTH = 1024 * 1024 * 6;

    private int writeTimes = 0;

    private void handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_WHAT_RECORD:

                writeMsgInThread(msg.obj);

                if (++writeTimes >= MAX_WRITE_BUFFER_TIMES) {
                    syncBufferWriterInThread();

                    if (msgFile != null && msgFile.length() >= MAX_RECORD_FILE_LENGTH) {
                        syncBufferWriterInThread();
                        reGeneralBufferWriterInThread();
                    }

                }

                break;

            case MSG_WHAT_SYNC:

                syncBufferWriterInThread();

                break;
            case MSG_WHAT_RELEASE:

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

    private void writeMsgInThread(Object obj) {

        if (mBufferWriter != null) {
            try {
                mBufferWriter.write(mRecordDateFormat.format(System.currentTimeMillis()));
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
    private FileDescriptor fd;
    private BufferedWriter mBufferWriter;

    private void reGeneralBufferWriterInThread() {

        releaseBufferWriteInThread();

        msgFile = generalSaveFile();

//        fd = generaFD(msgFile);
//        mBufferWriter = generalBufferedWriterByFd(fd);

        mBufferWriter = generalBufferedWriter(msgFile);

    }

    private File generalSaveFile() {
        int random = (int) ((Math.random() + 1) * 100);
        SimpleDateFormat mFileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        final String name = fileNamePrefix
                + "_"
                + mFileNameDateFormat.format(new Date())
                + "_"
                + String.valueOf(random)
                + fileNameSuffix;
        return new File(logRootDir, name);
    }

    private FileDescriptor generaFD(File msgFile) {
        try {
            FileOutputStream fos = new FileOutputStream(msgFile);
            return fos.getFD();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedWriter generalBufferedWriterByFd(FileDescriptor fd) {
        if (fd == null) {
            return null;
        }
        return new BufferedWriter(new FileWriter(fd));
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
        if (fd != null) {
            FileUtil.syncFile(fd);
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
        fd = null;
    }

    @Override
    public void recordMsgA(String TAG, String msg) {
        if (mHandler != null) {
            String log = "A/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgA(String TAG, Throwable e) {
        if (mHandler != null) {
            String log = "A/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgA(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "A/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgV(String TAG, String msg) {

        if (mHandler != null) {
            String log = "V/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = "V/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgV(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "V/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgD(String TAG, String msg) {

        if (mHandler != null) {
            String log = "D/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = "D/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgD(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "D/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgI(String TAG, String msg) {

        if (mHandler != null) {
            String log = "I/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = "I/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgI(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "I/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgW(String TAG, String msg) {

        if (mHandler != null) {
            String log = "W/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = "W/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgW(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "W/" + TAG + ":" + msg + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }
    }

    @Override
    public void recordMsgE(String TAG, String msg) {

        if (mHandler != null) {
            String log = "E/" + TAG + ":" + msg;
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, Throwable e) {

        if (mHandler != null) {
            String log = "E/" + TAG + ":" + Log.getStackTraceString(e);
            mHandler.obtainMessage(MSG_WHAT_RECORD, log).sendToTarget();
        }

    }

    @Override
    public void recordMsgE(String TAG, String msg, Throwable e) {
        if (mHandler != null) {
            String log = "E/" + TAG + ":" + msg + Log.getStackTraceString(e);
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

            if ((fileRecordClient = wr.get()) != null) {
                fileRecordClient.handleMessage(msg);
            }

        }
    }

}
