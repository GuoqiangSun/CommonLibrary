package cn.com.swain.support.ble.scan;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import java.util.List;

import cn.com.swain.baselib.util.StrUtil;

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
    public List<ParcelUuid> serviceUuids;
    public byte[] scanRecord;

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    private String scanRecordStr;

    public String getScanRecordStr() {
        if (scanRecordStr == null) {
            scanRecordStr = StrUtil.bytesToHex(scanRecord);
        }
        return scanRecordStr;
    }

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
     * 检测 mac是否为 null or {@link #DEFAULT_ADDRESS}
     */
    public boolean checkMac() {
        return this.address != null && !this.address.equalsIgnoreCase(DEFAULT_ADDRESS);
    }


    /**
     * 检测 name是否为 null or {@link #DEFAULT_NAME}
     */
    public boolean checkName() {
        return this.name != null && !this.name.equalsIgnoreCase(DEFAULT_NAME);
    }

    /**
     * 地址 名称 是否可用
     *
     * @return true valid false invalid
     */
    public boolean isValid() {
        return checkMac() && checkName();
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
            return false;
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

    public boolean startWithMac(List<String> macStrs) {
        if (checkMac()) {
            return false;
        }
        if (macStrs == null || macStrs.size() <= 0) {
            return false;
        }
        for (String mStr : macStrs) {
            if (this.address.startsWith(mStr)) {
                return true;
            }
        }
        return false;
    }

    private double distance = -1.0;

    public double getDistance() {
        return distance;
    }

    public double calculateAccuracy() {
        Ibeancon ibeacon = findIbeacon();
        if (ibeacon.patternFound) {
            distance = calculateAccuracy(ibeacon.txPower, rssi);
        }
        distance = -1.0;
        return distance;
    }

    private Ibeancon mIbeancon = new Ibeancon();

    public static class Ibeancon {
        public boolean hasFound;
        public boolean patternFound;
        public String uuid;
        public int major;
        public int minor;
        public int txPower;
    }

    public Ibeancon findIbeacon() {
        if (mIbeancon.hasFound) {
            return mIbeancon;
        }
        mIbeancon.hasFound = true;
        int startByte = 2;
        mIbeancon.patternFound = false;
        while (startByte <= 5) {
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 &&
                    // Identifies
                    // an
                    // iBeacon
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                // Identifies
                // correct
                // data
                // length
                mIbeancon.patternFound = true;
                break;
            }
            startByte++;
        }

        // 如果找到了的话

        if (mIbeancon.patternFound) {
            // 转换为16进制

            byte[] uuidBytes = new byte[16];

            System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);

            String hexString = StrUtil.bytesToHex(uuidBytes);

            // ibeacon的UUID值
            mIbeancon.uuid = hexString.substring(0, 8)
                    + "-" + hexString.substring(8, 12)
                    + "-" + hexString.substring(12, 16)
                    + "-" + hexString.substring(16, 20)
                    + "-" + hexString.substring(20, 32);

            // ibeacon的Major值
            mIbeancon.major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

            // ibeacon的Minor值
            mIbeancon.minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

            mIbeancon.txPower = (scanRecord[startByte + 24]);

        }

        return mIbeancon;
    }

    public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            // if we cannot determine accuracy, return -1.
            return -1.0;
        }
        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {

            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;

        }
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "name : " + name + " address : " + address
                + " rssi : " + rssi + " isConnected: " + isConnected()
                + " broadUUID: " + getFirstBroadUUID();
    }

}
