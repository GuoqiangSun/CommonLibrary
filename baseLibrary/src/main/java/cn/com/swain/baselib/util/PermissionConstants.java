package cn.com.swain.baselib.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * author: Guoqiang_Sun
 * date: 2019/1/11 0011
 * Desc:
 */
@SuppressLint("InlinedApi")
public class PermissionConstants {

    public static final String CALENDAR = Manifest.permission_group.CALENDAR;
    public static final String CAMERA = Manifest.permission_group.CAMERA;
    public static final String CONTACTS = Manifest.permission_group.CONTACTS;
    public static final String LOCATION = Manifest.permission_group.LOCATION;
    public static final String MICROPHONE = Manifest.permission_group.MICROPHONE;
    public static final String PHONE = Manifest.permission_group.PHONE;
    public static final String SENSORS = Manifest.permission_group.SENSORS;
    public static final String SMS = Manifest.permission_group.SMS;
    public static final String STORAGE = Manifest.permission_group.STORAGE;

    private static final String[] GROUP_CALENDAR = {
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };
    private static final String[] GROUP_CAMERA = {
            Manifest.permission.CAMERA
    };
    private static final String[] GROUP_CONTACTS = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS
    };
    private static final String[] GROUP_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] GROUP_MICROPHONE = {
            Manifest.permission.RECORD_AUDIO
    };
    private static final String[] GROUP_PHONE = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ANSWER_PHONE_CALLS
    };
    private static final String[] GROUP_PHONE_BELOW_O = Arrays.copyOf(
            GROUP_PHONE, GROUP_PHONE.length - 1
    );

    private static final String[] GROUP_SENSORS = {
            Manifest.permission.BODY_SENSORS
    };
    private static final String[] GROUP_SMS = {
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS,
    };
    private static final String[] GROUP_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @StringDef({CALENDAR, CAMERA, CONTACTS, LOCATION, MICROPHONE, PHONE, SENSORS, SMS, STORAGE,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
    }

    public static String[] getPermissions(@Permission final String permission) {
        switch (permission) {
            case CALENDAR:
                return GROUP_CALENDAR;
            case CAMERA:
                return GROUP_CAMERA;
            case CONTACTS:
                return GROUP_CONTACTS;
            case LOCATION:
                return GROUP_LOCATION;
            case MICROPHONE:
                return GROUP_MICROPHONE;
            case PHONE:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    return GROUP_PHONE_BELOW_O;
                } else {
                    return GROUP_PHONE;
                }
            case SENSORS:
                return GROUP_SENSORS;
            case SMS:
                return GROUP_SMS;
            case STORAGE:
                return GROUP_STORAGE;
        }
        return new String[]{permission};
    }

    public static String forPermission(final String[] permission) {

        if (permission != null && permission.length == 1) {
            if (Arrays.equals(GROUP_SENSORS, permission)) {
                return SENSORS;
            } else {
                return permission[0];
            }
        }

        if (Arrays.equals(GROUP_CALENDAR, permission)) {
            return CALENDAR;
        } else if (Arrays.equals(GROUP_CAMERA, permission)) {
            return CAMERA;
        } else if (Arrays.equals(GROUP_CONTACTS, permission)) {
            return CONTACTS;
        } else if (Arrays.equals(GROUP_LOCATION, permission)) {
            return LOCATION;
        } else if (Arrays.equals(GROUP_MICROPHONE, permission)) {
            return MICROPHONE;
        } else if (Arrays.equals(GROUP_PHONE_BELOW_O, permission)) {
            return PHONE;
        } else if (Arrays.equals(GROUP_PHONE, permission)) {
            return PHONE;
        } else if (Arrays.equals(GROUP_SENSORS, permission)) {
            return SENSORS;
        } else if (Arrays.equals(GROUP_SMS, permission)) {
            return SMS;
        } else if (Arrays.equals(GROUP_STORAGE, permission)) {
            return STORAGE;
        }

        return (permission == null || permission.length <= 0) ? "" : permission[0];
    }

}
