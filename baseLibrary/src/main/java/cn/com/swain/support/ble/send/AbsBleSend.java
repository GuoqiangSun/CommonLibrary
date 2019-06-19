package cn.com.swain.support.ble.send;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public abstract class AbsBleSend {

    public abstract void sendData(byte[] data);

    public abstract String getUuidStr();

    public abstract void removeMsg();

    public abstract void closeGatt();

    public abstract String getMac();


}
