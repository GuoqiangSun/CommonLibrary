package cn.com.swain.support.udp;

import android.os.Looper;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/24 0024
 * Desc:
 */
public class FastUdpFactory {

    public static AbsFastUdp newFastUdp(Looper mLooper) {
        return new FastUdpImpl(mLooper);
    }

}
