package cn.com.swain.support.ble.scan;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public interface IBleScanObserver {

    void onBsStartScan();

    void onBsStopScan();

    void onResultBsGattScan(ScanBle mBle);

}
