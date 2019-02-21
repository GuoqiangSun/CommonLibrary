package cn.com.swain.baselib.util;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class CrcUtil {

    /**
     * @param buffer 需要校验的字节从头到尾全部校验
     * @return crc8
     */
    public static byte CRC8(byte[] buffer) {
        return CRC8(buffer, 0, buffer.length);
    }

    /**
     * http://www.ip33.com/crc.html
     *
     * @param buffer 需要校验的字节buffer
     * @param start  开始字节（包含）
     * @param length 结束字节（不包含）
     * @return crc8
     */
    public static byte CRC8(byte[] buffer, int start, int length) {
        int crc = 0x00;   //起始字节00
        for (int j = start; j < length; j++) {
            crc ^= buffer[j] & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x01) != 0) {
                    crc = (crc >> 1) ^ 0x8c;
                } else {
                    crc >>= 1;
                }
            }
        }
        return (byte) (crc & 0xFF);
    }

}
