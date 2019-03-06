package cn.com.swain.support.protocolEngine;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/2 0002
 * desc :
 */
public class ProtocolBuild {

    /**
     * xx有限公司
     */
    public static final class XX {

        /**
         * int32 ,前三位表示公司,后一位表示协议版本号
         */
        public static final int XX_VERSION = 0x00000000;

        /**
         * 第一版本协议,精简协议
         * <p>
         * （帧头）(1) + 有效数据长度(2) + + custom(1)+product(1) + 命令（类型 + 命令）(2) + 参数（变长）+ 校验(CRC8)(1) + （帧尾）(1)
         */
        public static final int XX_VERSION_0 = (XX_VERSION & 0xFFFFFF00) | ProtocolBuild.VERSION.VERSION_0;

        /**
         * 第二版本协议,这个版本的协议增加了序列号,token码
         * <p>
         * （帧头）(1) + 有效数据长度(2) + 版本(1)+序号（1）+token(4)
         * + custom(1)+product(1) + 命令（类型 + 命令）(2) + 参数（变长）+ 校验(CRC8)(1) + （帧尾）(1)
         */
        public static final int XX_VERSION_SEQ = (XX_VERSION & 0xFFFFFF00) | ProtocolBuild.VERSION.VERSION_SEQ;

        /**
         * 转义前	           转义后
         * STX （帧头）	    STX 转成 ESC 和 STX_ESC
         * ETX （帧尾）	    ETX 转成 ESC 和 ETX_ESC
         * ESC （转义符）	    ESC 转成 ESC 和 ESC_ESC
         */

        public static final byte STX = (byte) 0xff;
        public static final byte ETX = (byte) 0xee;
        public static final byte ESC = 0x55;

        public static final byte STX_ESC = (byte) 0xaa;
        public static final byte ETX_ESC = (byte) 0x99;
        public static final byte ESC_ESC = 0x00;
    }

    public static final class VERSION {

        /**
         * 去除前三位,得到最后一个字节表示版本
         */
        public static byte getVersion(int version) {
            return (byte) version;
        }

        /**
         * 去除前三位后，得到真实的版本号
         */
        public static int removeHighVersion(int version) {
            return (version & 0x000000FF);
        }

        /**
         * 去除最后一位,得到前三个字节表示公司
         */
        public static int getCompany(int version) {
            return (version & 0xFFFFFF00);
        }

        public static boolean isXXVersion(int version) {
            return getCompany(version) == XX.XX_VERSION;
        }

        public static byte getSTX(int version) {
            return isXXVersion(version) ? XX.STX : 0x00;
        }

        public static byte getETX(int version) {
            return isXXVersion(version) ? XX.ETX : 0x00;
        }

        /**
         * 第一版本协议,精简协议
         */
        public static final int VERSION_0 = 0x00000000;

        /**
         * 第二版本协议,这个版本的协议增加了序列号,token码
         */
        public static final int VERSION_SEQ = 0x00000001;
    }

    public static void main(String[] args) {
        System.out.println(" QXVersion 0 :" + VERSION.VERSION_0);
        System.out.println(" QXVersion seq:" + VERSION.VERSION_SEQ);

        System.out.println(" isXXVersion :" + VERSION.isXXVersion(VERSION.VERSION_SEQ));
        System.out.println(" isXXVersion :" + VERSION.isXXVersion(VERSION.VERSION_0));
        System.out.println(" isXXVersion :" + VERSION.isXXVersion(12345));
        System.out.println(" isXXVersion :" + VERSION.isXXVersion(ProtocolBuild.VERSION.VERSION_SEQ + 123456789));

        int i = 0xFFFFFF20;

        System.out.println(Integer.toHexString(i & 0xFFFFFF00));

    }

}
