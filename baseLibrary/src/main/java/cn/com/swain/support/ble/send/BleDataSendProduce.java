package cn.com.swain.support.ble.send;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class BleDataSendProduce {

    private BleDataSendProduce() {
    }

    public static AbsBleSend produceFirstBleSend(BluetoothGatt mConGatt) {
        return produceIndexBleSend(mConGatt, 0);
    }

    public static AbsBleSend produceIndexBleSend(BluetoothGatt mConGatt, int index) {
        final ArrayList<AbsBleSend> absBleSends = produceBleSend(mConGatt);

        AbsBleSend absBleSend = null;

        if (absBleSends != null) {
            if (absBleSends.size() > index) {
                absBleSend = absBleSends.get(index);
            }
            absBleSends.clear();
        }

        return absBleSend;
    }

    public static ArrayList<AbsBleSend> produceBleSend(BluetoothGatt mConGatt) {

        if (mConGatt == null) {
            return null;
        }
        ArrayList<AbsBleSend> mSendLst = null;

        List<BluetoothGattService> services = mConGatt.getServices();

        if (services != null && services.size() > 0) {

            mSendLst = new ArrayList<>(3);

            for (BluetoothGattService service : services) {

                List<BluetoothGattCharacteristic> characteristics = service
                        .getCharacteristics();

                if (characteristics != null && characteristics.size() > 0) {

                    for (BluetoothGattCharacteristic mGattCharact : characteristics) {

                        int properties = mGattCharact.getProperties();
                        UUID uuid = mGattCharact.getUuid();

//                        int permissions = mGattCharact.getPermissions();
//                        BluetoothGattCharacteristic.PERMISSION_WRITE;

                        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            AbsBleSend mBleSend = new BleSend(mConGatt, mGattCharact);
                            mSendLst.add(mBleSend);
                        }

                    }


                }
            }
        }


        return mSendLst;
    }


    public static SpannableStringBuilder showService(BluetoothGatt mGatt) {

        String service = getServiceStr(mGatt);

        int serviceCount = 0;

        if (mGatt != null) {
            List<BluetoothGattService> services = mGatt.getServices();
            if (services != null) {
                serviceCount = services.size();
            }

        }
        return serviceAddColor(service, serviceCount);
    }

    public static SpannableStringBuilder serviceAddColor(String str, int serviceCount) {

        SpannableStringBuilder style = new SpannableStringBuilder(str);

        String of = "services:";
        int length = of.length();
        int from = str.indexOf(of);
        // Log.e(TAG, "from " + from);

        String of1 = "characteristics:";
        int length1 = of1.length();
        int from1 = str.indexOf(of1);
        // Log.e(TAG, "from1 " + from1);

        for (int i = 0; i < serviceCount; i++) {

            int bstart = str.indexOf(of, from);
            // Log.e(TAG, "" + bstart);
            int bend = bstart + length;
            from = bend;

            if (bstart == -1) {
                continue;
            }

            // String substring = str.substring(bstart, bend);
            // Log.e(TAG, "" + substring);

            style.setSpan(new BackgroundColorSpan(Color.RED), bstart, bend,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            int fstart = str.indexOf(of1, from1);
            // Log.e(TAG, "" + fstart);
            int fend = fstart + length1;
            from1 = fend;
            if (fstart == -1) {
                continue;
            }

            // String fsubstring = str.substring(fstart, fend);
            // Log.e(TAG, "" + fsubstring);

            style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        }
        return style;

    }


    public static String getServiceStr(BluetoothGatt mConGatt) {
        if (mConGatt == null) {
            return "services: null gatt";
        }

        StringBuilder sb = new StringBuilder();

        List<BluetoothGattService> services = mConGatt.getServices();
        if (services != null && services.size() > 0) {

            for (BluetoothGattService service : services) {
                sb.append("services:\n");
                if (service == null) {
                    continue;
                }
                sb.append(service.getUuid().toString());
                sb.append("\n");
                List<BluetoothGattCharacteristic> characteristics = service
                        .getCharacteristics();

                if (characteristics != null && characteristics.size() > 0) {
                    sb.append("characteristics:\n");
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        if (characteristic == null) {
                            continue;
                        }
                        sb.append(characteristic.getUuid().toString());
                        sb.append("\n");

                        int charaProp = characteristic.getProperties();

                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            sb.append("-PROPERTY_READ-");
                        }
                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            sb.append("-PROPERTY_WRITE-");
                        }
                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            sb.append("-PROPERTY_NOTIFY-");
                        }
                        sb.append("\n");
                    }

                } else {
                    sb.append("characteristics == null\n");
                }

            }
        } else {
            sb.append("services == null\n");
        }

        return sb.toString();
    }


}


