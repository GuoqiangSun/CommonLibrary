package cn.com.swain.support.ble.scan;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import java.util.List;

/**
 * author: Guoqiang_Sun
 * date: 2018-03-12
 * description:
 */

public class ScanBle implements Parcelable {

    public ScanBle() {

    }

    public static final String DEFAULT_NAME = "$unknown";
    public static final String DEFAULT_ADDRESS = "00:00:00:00:00:00";

    public static final int STATE_DISCONNECTED = 0x00;
    public static final int STATE_CONNECTED = 0x01;


    public int state;
    public String name;
    public String address;
    public int rssi;
    private List<ParcelUuid> serviceUuids;


    protected ScanBle(Parcel in) {
        state = in.readInt();
        name = in.readString();
        address = in.readString();
        rssi = in.readInt();
        serviceUuids = in.createTypedArrayList(ParcelUuid.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(rssi);
        dest.writeTypedList(serviceUuids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScanBle> CREATOR = new Creator<ScanBle>() {
        @Override
        public ScanBle createFromParcel(Parcel in) {
            return new ScanBle(in);
        }

        @Override
        public ScanBle[] newArray(int size) {
            return new ScanBle[size];
        }
    };

    /**
     * 地址 名称是否可用
     *
     * @return
     */
    public boolean isValid() {
        return this.address != null && !this.address.equalsIgnoreCase(DEFAULT_ADDRESS)
                && this.name != null && !this.name.equalsIgnoreCase(DEFAULT_NAME);
    }

    public void setConnected() {
        state = STATE_CONNECTED;
    }

    public void setDisconnected() {
        state = STATE_DISCONNECTED;
    }

    public boolean isConnected() {
        return (this.state == STATE_CONNECTED);
    }

    public boolean isDisconnect() {
        return (this.state == STATE_DISCONNECTED);
    }


    public void setName(String name) {
        if (name == null || ("").equals(name)) {
            this.name = ScanBle.DEFAULT_NAME;
        } else {
            this.name = name;
        }
    }

    public void setAddress(String address) {
        if (address == null || ("").equals(address)) {
            this.address = ScanBle.DEFAULT_ADDRESS;
        } else {
            this.address = address;
        }
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setBroadServiceUuids(List<ParcelUuid> serviceUuids) {
        this.serviceUuids = serviceUuids;
    }


    public String getFirstBroadUUID() {
        if (serviceUuids == null || serviceUuids.size() <= 0) {
            return "NULL";
        }
        return serviceUuids.get(0).getUuid().toString();
    }

    public boolean matchBroadUUID(List<String> uuidStrs) {
        if (serviceUuids == null || serviceUuids.size() <= 0) {
            return false;
        }
        if (uuidStrs == null || uuidStrs.size() <= 0) {
            return true;
        }
        for (ParcelUuid mParcelUuid : serviceUuids) {
            for (String uStr : uuidStrs) {
                if (mParcelUuid.getUuid().toString().equalsIgnoreCase(uStr)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "name : " + name + " address : " + address + " rssi : " + rssi + " isConnected: " + isConnected() + " broadUUID: " + getFirstBroadUUID();
    }

}
