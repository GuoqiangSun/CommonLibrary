package cn.com.swain.support.ble.enable;

import android.content.Context;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/4 0004
 * desc :
 */

public abstract class AbsBleEnable {


    public abstract  void skipBleSetting(Context mCtx);

    /**
     * 获取ble的状态
     *
     * @return
     */
    public abstract  int beGetBleState();

    /**
     * 蓝牙是否激活
     *
     * @return
     */
    public abstract boolean beIsBleEnable();


    /**
     * 激活ble
     */
    public abstract boolean beEnableBle();


    /**
     * 检测ble的状态
     *
     * @param mResult
     */
    public abstract void checkBleState(BleStateResult mResult);


}
