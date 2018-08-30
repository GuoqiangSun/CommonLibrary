package cn.com.common.support.protocolEngine.utils;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/9 0009
 * desc :
 */

public class SecureKey {

    public static class SCustom {

        public static final byte CUSTOM_WAN = 0x00;

        public static final byte PRODUCT_BLE_SOCKET = 0x00;
        public static final byte PRODUCT_WIFI_SOCKET = 0x02;

    }

    public static class SType {

        /**
         * error
         */
        public static final byte TYPE_ERROR = 0x00;

        /**
         * 系统
         */
        public static final byte TYPE_SYSTEM = 0x01;

        /**
         * 控制
         */
        public static final byte TYPE_CONTROLLER = 0x02;

        /**
         * 上报
         */
        public static final byte TYPE_REPORT = 0x03;


    }

    public static class SCmd {
        /**
         * 错误
         */
        public static final byte CMD_ERROR = 0x00;

        /**
         * 心跳
         */
        public static final byte CMD_HEARTBEAT = 0x01;
        public static final byte CMD_HEARTBEAT_RESPONSE = 0x02;

        /**
         * 发现设备
         */
        public static final byte CMD_DISCOVERY_DEVICE = 0x03;
        public static final byte CMD_DISCOVERY_DEVICE_RESPONSE = 0x04;

        /**
         * 绑定设备
         */
        public static final byte CMD_BIND_DEVICE = 0x05;
        public static final byte CMD_BIND_DEVICE_RESPONSE = 0x06;
    }

    public static class SModel {

        /**
         * 成功
         */
        public static final byte MODEL_RESULT_SUCCESS = 0x00;

        /**
         * 失败
         */
        public static final byte MODEL_RESULT_FAIL = 0x01;

        /**
         * token失效
         */
        public static final byte MODEL_RESULT_TOKEN_INVALID = 0x03;

        /**
         * 开
         */
        public static final byte MODEL_SWITCH_ON = 0x01;
        /**
         * 关
         */
        public static final byte MODEL_SWITCH_OFF = 0x00;


        /**
         * 启动
         */
        public static final byte MODEL_START_UP = 0x01;
        /**
         * 结束
         */
        public static final byte MODEL_FINISH = 0x02;


        /**
         * true
         */
        public static final byte MODEL_TRUE = 0x01;


        /**
         * false
         */
        public static final byte MODEL_FALSE = 0x00;

    }

    public static class SUtil {

        public static boolean isBleProduct(byte product) {
            return (product == SCustom.PRODUCT_BLE_SOCKET);
        }

        public static boolean isTrue(byte flag) {
            return (flag == SModel.MODEL_TRUE);
        }

        public static boolean resultIsOk(byte result) {

            return (result == SModel.MODEL_RESULT_SUCCESS);
        }

        public static boolean resultTokenInvalid(byte result) {

            return (result == SModel.MODEL_RESULT_TOKEN_INVALID);
        }


        public static byte resultSuccess(boolean success) {
            return (success ? SModel.MODEL_RESULT_SUCCESS : SModel.MODEL_RESULT_FAIL);
        }

        public static boolean startup(byte param) {

            return (param == SModel.MODEL_START_UP);
        }

        public static byte startup(boolean startup) {
            return (startup ? SModel.MODEL_START_UP : SModel.MODEL_FINISH);
        }

        public static boolean on(byte param) {

            return (param == SModel.MODEL_SWITCH_ON);
        }

        public static byte on(boolean on) {
            return (on ? SModel.MODEL_SWITCH_ON : SModel.MODEL_SWITCH_OFF);
        }

    }

}
