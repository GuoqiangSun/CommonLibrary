package cn.com.swain.support.ble.send;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public abstract class AbsBleSend {

    public abstract void setResolveDataLength(int length);

    public abstract void setPrintData(boolean print);

    public abstract void sendData(byte[] data);

    public abstract void sendData(byte[] data, long delay);

    public abstract String getUuidStr();

    public abstract void removeMsg();

    public abstract void closeGatt();

    public abstract String getMac();


}
