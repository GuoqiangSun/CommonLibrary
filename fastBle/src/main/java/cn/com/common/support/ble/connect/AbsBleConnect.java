package cn.com.common.support.ble.connect;

import cn.com.common.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/3 0003
 * desc :
 */

public abstract class AbsBleConnect {

    public abstract void connect(ScanBle mScanBle);

    public abstract void disconnect(ScanBle mScanBle);

    public abstract void checkConnectResult(boolean removeLast, BleConnectResult mResult);

    public abstract void removeCheckConnectResult();

    public abstract void release();

}
