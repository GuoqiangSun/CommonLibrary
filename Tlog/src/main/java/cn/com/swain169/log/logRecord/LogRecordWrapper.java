package cn.com.swain169.log.logRecord;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/16 0016
 * desc :
 */
public class LogRecordWrapper implements ILogRecord {

    public LogRecordWrapper() {

    }

    public LogRecordWrapper(ILogRecord mILogRecord) {
        this.mILogRecord = mILogRecord;
    }


    private ILogRecord mILogRecord;

    public void attachIRecordMsgFile(ILogRecord mILogRecord) {
        if (this.mILogRecord != null) {
            throw new IllegalArgumentException(" mILogRecord is not null ");
        }
        this.mILogRecord = mILogRecord;
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
