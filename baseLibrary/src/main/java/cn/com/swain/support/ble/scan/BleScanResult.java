package cn.com.swain.support.ble.scan;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/17 0017
 * desc :
 */
public class BleScanResult implements Serializable {

    public IBleScanCheckResult mBleScanCheckResult;

    public BleScanResult(IBleScanCheckResult mBleScanCheckResult, long delay, ArrayList<ScanBle> mScanLst) {
        this.mBleScanCheckResult = mBleScanCheckResult;
        setDelay(delay);
        setScanLst(mScanLst);
    }

    private long delay = 1000 * 10;

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        if (this.delay <= 0) {
            return 1000;
        }
        return delay;
    }

    private ArrayList<ScanBle> mScanLst;

    public void setScanLst(ArrayList<ScanBle> mScanLst) {
        this.mScanLst = mScanLst;
    }

    private boolean isNull;

    public boolean getCheckResultIsNull() {
        return isNull;
    }

    public boolean checkIsNull() {
        if (mScanLst == null || mScanLst.size() <= 0) {
            isNull = true;
            return false;
        } else {
            isNull = false;
            return true;
        }
    }


}
