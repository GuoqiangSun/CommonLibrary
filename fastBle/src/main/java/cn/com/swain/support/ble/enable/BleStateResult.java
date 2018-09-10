package cn.com.swain.support.ble.enable;

import java.io.Serializable;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/4 0004
 * desc :
 */

public class BleStateResult implements Serializable {


    public static final int BLE_STATE_ENABLED = 0x01;

    public static final int BLE_STATE_DISENABLED = 0x02;

    private int state;

    public boolean isEnabled() {
        return (state == BLE_STATE_ENABLED);
    }

    public boolean isDisEnabled() {
        return (state == BLE_STATE_DISENABLED);
    }

    public void setEnabled() {
        this.state = BLE_STATE_ENABLED;
    }

    public void setDisEnabled() {
        this.state = BLE_STATE_DISENABLED;
    }

    public static final int MIN_DELAY = 500;

    private long delay = MIN_DELAY;

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
        if (this.delay < MIN_DELAY) {
            this.delay = MIN_DELAY;
        }
    }

    public boolean isValidDelay() {
        return (delay >= MIN_DELAY);
    }

    private int mTotalTimes = 1;

    public void setTotalTimes(int mTotalTimes) {
        this.mTotalTimes = mTotalTimes;
    }

    public int getTotalTimes() {
        return mTotalTimes;
    }


    private int mCurTimes;

    public int getCurTimes() {
        return this.mCurTimes;
    }

    public int updateTimes() {
        return ++mCurTimes;
    }


    public boolean checkFinish() {
        if (getCurTimes() < getTotalTimes()) {
            return false;
        }

        return true;
    }

    public IBleStateCallBack mBleStateCallBack;

    public void disCheck() {

        this.mCurTimes = mTotalTimes;
        this.mBleStateCallBack = null;

    }

}
