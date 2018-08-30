package cn.com.common.support.ble.scan;

import android.os.ParcelUuid;

import java.util.List;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/2 0002
 * desc :
 */

public abstract class AbsBleScan {

    public abstract void bsBleStateChange(boolean on);

    public abstract void bsScanBleOnce();

    /**
     * 扫描ble
     */
    public abstract void bsAutoScanBle();

    /**
     * 强制重新扫描
     */
    public abstract void bsForceAutoScan();

    /**
     * 停止扫描
     */
    public abstract void bsStopScan();

    /**
     * 坚持扫描结果
     *
     * @param removeLast
     * @param mBleScanResult
     */
    public abstract void checkBleScanResult(boolean removeLast, BleScanResult mBleScanResult);

    public abstract void removeCheckScanResult();

    abstract void leScan(String name ,String address, int rssi,List<ParcelUuid> serviceUuids);

}
