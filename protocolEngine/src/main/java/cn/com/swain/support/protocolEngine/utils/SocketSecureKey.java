package cn.com.swain.support.protocolEngine.utils;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public class SocketSecureKey extends SecureKey {

    public static final class Type extends SType {
        //

    }

    public static final class Cmd extends SCmd {


        /**********0x01**************/

        /**
         * rename
         */
        public static final byte CMD_RENAME = 0x0B;
        public static final byte CMD_RENAME_RESPONSE = 0x0C;

        public static final byte CMD_SET_VOLTAGE_ALARM_VALUE = 0x0F;
        public static final byte CMD_SET_VOLTAGE_ALARM_VALUE_RESPONSE = 0x10;
        public static final byte CMD_QUERY_VOLTAGE_ALARM_VALUE = 0x11;
        public static final byte CMD_QUERY_VOLTAGE_ALARM_VALUE_RESPONSE = 0x12;

        public static final byte CMD_SET_CURRENT_ALARM_VALUE = 0x13;
        public static final byte CMD_SET_CURRENT_ALARM_VALUE_RESPONSE = 0x14;
        public static final byte CMD_QUERY_CURRENT_ALARM_VALUE = 0x15;
        public static final byte CMD_QUERY_CURRENT_ALARM_VALUE_RESPONSE = 0x16;

        public static final byte CMD_SET_POWER_ALARM_VALUE = 0x17;
        public static final byte CMD_SET_POWER_ALARM_VALUE_RESPONSE = 0x18;
        public static final byte CMD_QUERY_POWER_ALARM_VALUE = 0x19;
        public static final byte CMD_QUERY_POWER_ALARM_VALUE_RESPONSE = 0x1A;

        public static final byte CMD_SET_UNIT_TEMPERATURE = 0x1B;
        public static final byte CMD_SET_UNIT_TEMPERATURE_RESPONSE = 0x1C;
        public static final byte CMD_QUERY_UNIT_TEMPERATURE = 0x1D;
        public static final byte CMD_QUERY_UNIT_TEMPERATURE_RESPONSE = 0x1E;

        public static final byte CMD_SET_UNIT_MONETARY = 0x1F;
        public static final byte CMD_SET_UNIT_MONETARY_RESPONSE = 0x20;
        public static final byte CMD_QUERY_UNIT_MONETARY = 0x21;
        public static final byte CMD_QUERY_UNIT_MONETARY_RESPONSE = 0x22;

        public static final byte CMD_SET_PRICES_ELECTRICITY = 0x23;
        public static final byte CMD_SET_PRICES_ELECTRICITY_RESPONSE = 0x24;
        public static final byte CMD_QUERY_PRICES_ELECTRICITY = 0x25;
        public static final byte CMD_QUERY_PRICES_ELECTRICITY_RESPONSE = 0x26;

        public static final byte CMD_SET_RECOVERY_SCM = 0x27;
        public static final byte CMD_SET_RECOVERY_SCM_RESPONSE = 0x28;

        //帮助scm注册
        public static final byte CMD_REGISTER_SCM = 0x2B;
        public static final byte CMD_REGISTER_SCM_RESPONSE = 0x2C;

        public static final byte CMD_REQUEST_TOKEN = 0x2D;
        public static final byte CMD_REQUEST_TOKEN_RESPONSE = 0x2E;

        public static final byte CMD_CONTROL_TOKEN = 0x2F;
        public static final byte CMD_CONTROL_TOKEN_RESPONSE = 0x30;

        public static final byte CMD_SLEEP_TOKEN = 0x31;
        public static final byte CMD_SLEEP_TOKEN_RESPONSE = 0x32;

        public static final byte CMD_DISCONTROL_TOKEN = 0x33;
        public static final byte CMD_DISCONTROL_TOKEN_RESPONSE = 0x34;

        /**********0x02**************/

        /**
         * 设置时间
         */
        public static final byte CMD_SET_TIME = 0x03;
        public static final byte CMD_SET_TIME_RESPONSE = 0x04;

        /**
         * 设置定时
         */
        public static final byte CMD_SET_TIMING = 0x05;
        public static final byte CMD_SET_TIMING_RESPONSE = 0x06;

        /**
         * 继电器
         */
        public static final byte CMD_RELAY_SWITCH_REQUEST = 0x01;
        public static final byte CMD_RELAY_SWITCH_RESPONSE = 0x02;

        /**
         * 继电器
         */
        public static final byte CMD_QUERY_RELAY_STATUS = 0x0B;
        public static final byte CMD_QUERY_RELAY_STATUS_RESPONSE = 0x0C;

        /**
         * 插座倒计时
         */
        public static final byte CMD_SET_COUNTDOWN = 0x07;
        public static final byte CMD_SET_COUNTDOWN_RESPONSE = 0x08;

        /**
         * 温度湿度设置
         */
        public static final byte CMD_SET_ALARM = 0x09;
        public static final byte CMD_SET_ALARM_RESPONSE = 0x0A;


        /**
         * 查询倒计时
         */
        public static final byte CMD_QUERY_COUNTDOWN_DATA = 0x0d;
        public static final byte CMD_QUERY_COUNTDOWN_DATA_RESPONSE = 0x0E;

        /**
         * 查询时间
         */
        public static final byte CMD_QUERY_TIME = 0x0F;
        public static final byte CMD_QUERY_TIME_RESPONSE = 0x10;

        /**
         * 查询温度湿度
         */
        public static final byte CMD_QUERY_TEMPERATURE_HUMIDITY_DATA = 0x11;
        public static final byte CMD_QUERY_TEMPERATURE_HUMIDITY_DATA_RESPONSE = 0x12;


        /**
         * 查询插座定时
         */
        public static final byte CMD_QUERY_TIMING_LIST_DATA = 0x13;
        public static final byte CMD_QUERY_TIMING_LIST_DATA_RESPONSE = 0x14;

        public static final byte CMD_SET_SPENDING_ELECTRICITY_DATA = 0x15;
        public static final byte CMD_SET_SPENDING_ELECTRICITY_DATA_RESPONSE = 0x16;

        public static final byte CMD_QUERY_SPENDING_ELECTRICITY_DATA = 0x17;
        public static final byte CMD_QUERY_SPENDING_ELECTRICITY_DATA_RESPONSE = 0x18;


       /**********report**************/

        /**
         * 温度湿度上报
         */
        public static final byte CMD_TEMP_HUMI_REPORT = 0x01;
        public static final byte CMD_TEMP_HUMI_REPORT_RESPONSE = 0x02;

        /**
         * 电压电流 功率 频率上报
         */
        public static final byte CMD_POWER_FREQ_REPORT = 0x03;
        public static final byte CMD_POWER_FREQ_REPORT_RESPONSE = 0x04;


        /**
         * 温度湿度上报
         */
        public static final byte CMD_TEMPERATURE_HUMIDITY_REPORT = 0x05;
        public static final byte CMD_TEMPERATURE_HUMIDITY_REPORT_RESPONSE = 0x06;


        /**
         * 倒计时上报
         */
        public static final byte CMD_COUNTDOWN_REPORT = 0x07;
        public static final byte CMD_COUNTDOWN_REPORT_RESPONSE = 0x08;

        /**
         * 定时上报
         */
        public static final byte CMD_TIMING_REPORT = 0x09;
        public static final byte CMD_TIMING_REPORT_RESPONSE = 0x0A;


    }

    public static final class Model extends SModel {


        /**
         * 继电器
         */
        public static final byte MODEL_RELAY = 0x01;
        /**
         * 定温度
         */
        public static final byte ALARM_MODEL_TEMPERATURE = 0x01;
        /**
         * 定湿度
         */
        public static final byte ALARM_MODEL_HUMIDITY = 0x02;

        /**
         * 报警上限(加热)
         */
        public static final byte ALARM_LIMIT_UP = 0x01;
        /**
         * 报警下限
         */
        public static final byte ALARM_LIMIT_DOWN = 0x02;

        /**
         * 定时普通
         */
        public static final byte TIMING_COMMON = 0x01;

        /**
         * 定时高级
         */
        public static final byte TIMING_ADVANCE = 0x02;

        public static final byte DISCOVERY_MODEL_BLE = 0x01;
        public static final byte DISCOVERY_MODEL_WIFI = 0x02;

        /**
         * 定电量
         */
        public static final byte SPENDING_ELECTRICITY_E = 0x01;
        /**
         * 定花费
         */
        public static final byte SPENDING_ELECTRICITY_S = 0x02;

    }

    public static final class Util extends SUtil {

        public static boolean isCommonTiming(byte model) {
            return (model == Model.TIMING_COMMON);
        }

        public static boolean isAdvanceTiming(byte model) {
            return (model == Model.TIMING_ADVANCE);
        }

        public static boolean isTemperature(byte model) {
            return (model == Model.ALARM_MODEL_TEMPERATURE);
        }

        public static boolean isHumidity(byte model) {
            return (model == Model.ALARM_MODEL_HUMIDITY);
        }

        public static boolean isLimitUp(byte limit) {
            return (limit == Model.ALARM_LIMIT_UP);
        }

        public static boolean isLimitDown(byte limit) {
            return (limit == Model.ALARM_LIMIT_DOWN);
        }

        public static byte limitUp(boolean limitUp) {
            return (byte) (limitUp ? Model.ALARM_LIMIT_UP : Model.ALARM_LIMIT_DOWN);
        }

        public static boolean isBleModel(byte model) {
            return (model == Model.DISCOVERY_MODEL_BLE);
        }

        public static boolean isWiFiModel(byte model) {
            return (model == Model.DISCOVERY_MODEL_WIFI);
        }

        public static boolean isElectricity(byte model) {
            return (model == Model.SPENDING_ELECTRICITY_E);
        }

    }

}
