package cn.com.swain.support.udp;

import android.os.Parcel;
import android.os.Parcelable;

import cn.com.swain.baselib.util.StrUtil;

/**
 * author: Guoqiang_Sun
 * date: 2018/10/22 0022
 * Desc:
 */

@Deprecated
public class UdpResponseMsg implements Parcelable {

    public UdpResponseMsg() {
    }

    public UdpResponseMsg(String ip, int port, byte[] data) {
        this.ip = ip;
        this.port = port;
        this.data = data;
    }

    public String ip;
    public int port;
    public byte[] data;

    protected UdpResponseMsg(Parcel in) {
        ip = in.readString();
        port = in.readInt();
        data = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip);
        dest.writeInt(port);
        dest.writeByteArray(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UdpResponseMsg> CREATOR = new Creator<UdpResponseMsg>() {
        @Override
        public UdpResponseMsg createFromParcel(Parcel in) {
            return new UdpResponseMsg(in);
        }

        @Override
        public UdpResponseMsg[] newArray(int size) {
            return new UdpResponseMsg[size];
        }
    };

    @Override
    public String toString() {
        return " ip:" + ip + ":" + port  + " " + StrUtil.toString(data);
    }
}
