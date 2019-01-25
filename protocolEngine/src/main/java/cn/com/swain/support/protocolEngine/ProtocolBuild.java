package cn.com.swain.support.protocolEngine;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/2 0002
 * desc :
 */
public class ProtocolBuild {


    public static final class QX {

        public static final int VERSION = 0x000000;

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

        public static boolean isQXVersion(int version) {
            return version >>> 8 == QX.VERSION;
        }

        public static byte getSTX(int version) {
            return isQXVersion(version) ? QX.STX : 0x00;
        }

        public static byte getETX(int version) {
            return isQXVersion(version) ? QX.ETX : 0x00;
        }

        // 亓行

        /**
         * QX第一版本协议,精简协议
         */
        public static final int VERSION_0 = (QX.VERSION << 8) & 0xFF;

        /**
         * QX第二版本协议,这个版本的协议增加了序列号,token码
         */
        public static final int VERSION_SEQ = ((QX.VERSION << 8) & 0xFF) | 0x01;
    }

    public static void main(String[] args) {
        System.out.println(" QXVersion 0 :" + VERSION.VERSION_0);
        System.out.println(" QXVersion seq:" + VERSION.VERSION_SEQ);


        System.out.println(" isQXVersion :" + VERSION.isQXVersion(VERSION.VERSION_SEQ));
        System.out.println(" isQXVersion :" + VERSION.isQXVersion(VERSION.VERSION_0));
        System.out.println(" isQXVersion :" + VERSION.isQXVersion(12345));
        System.out.println(" isQXVersion :" + VERSION.isQXVersion(ProtocolBuild.VERSION.VERSION_SEQ + 123456789));


    }

}
