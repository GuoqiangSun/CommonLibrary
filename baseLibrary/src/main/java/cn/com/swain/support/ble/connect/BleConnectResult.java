package cn.com.swain.support.ble.connect;

import cn.com.swain.support.ble.scan.ScanBle;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/3 0003
 * desc :
 */
public class BleConnectResult {

    public ScanBle mBle;

    public IBleConnectCheckResult mCallBack;

    public long delay = 1000;


}
