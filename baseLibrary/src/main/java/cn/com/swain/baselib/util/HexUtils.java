package cn.com.swain.baselib.util;

/**
 * author: Guoqiang_Sun
 * date: 2018/11/27 0027
 * Desc:
 */
public class HexUtils {


    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度(列如 'abc')，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 字符串转十六进制数组
     * <p>
     * aaff010922880a0faf  转 0xaa 0xff 0x01 0x09 0x22 0x88 0x0a 0x0f 0xaf
     *
     * @param data aaff010922880a0faf
     * @return byte[]
     *  @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度(列如 'abc')，将抛出运行时异常
     */
    public static byte[] decodeHex(String data) {
        return decodeHex(data.toCharArray());
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }


    /**
     * long类型转 byte[]
     */
    public static byte[] longToByte(long number) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((number >> 8 * 7) & 0xFF);
        bytes[1] = (byte) ((number >> 8 * 6) & 0xFF);
        bytes[2] = (byte) ((number >> 8 * 5) & 0xFF);
        bytes[3] = (byte) ((number >> 8 * 4) & 0xFF);
        bytes[4] = (byte) ((number >> 8 * 3) & 0xFF);
        bytes[5] = (byte) ((number >> 8 * 2) & 0xFF);
        bytes[6] = (byte) ((number >> 8) & 0xFF);
        bytes[7] = (byte) (number & 0xFF);
        return bytes;
    }

    /**
     * byte[] 转long
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[7] & 0xff;// 最低位
        long s1 = b[6] & 0xff;
        long s2 = b[5] & 0xff;
        long s3 = b[4] & 0xff;
        long s4 = b[3] & 0xff;// 最低位
        long s5 = b[2] & 0xff;
        long s6 = b[1] & 0xff;
        long s7 = b[0] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * int 转 byte[]
     */
    public static byte[] intToByte(int number) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((number >> 24) & 0xFF);
        bytes[1] = (byte) ((number >> 16) & 0xFF);
        bytes[2] = (byte) ((number >> 8) & 0xFF);
        bytes[3] = (byte) (number & 0xFF);
        return bytes;
    }

    /**
     * byte[] 转 int
     */
    public static int byteToInt(byte[] b) {
        return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
    }

    /**
     * short 转 byte[]
     */
    public static byte[] shortToByte(short number) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((number >> 8) & 0xFF);
        bytes[1] = (byte) (number & 0xFF);
        return bytes;
    }

    /**
     * byte[] 转 short
     */
    public static short byteToShort(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }


    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(currentTimeMillis);

        byte[] bytes = longToByte(currentTimeMillis);
        System.out.println(StrUtil.toString(bytes));

        char[] chars = encodeHex(bytes);
        for (char c : chars) {
            System.out.print(c);
        }
        System.out.println();


        String s = "ab";
        System.out.println();
        byte[] bytes4 = decodeHex(s);
        System.out.println(StrUtil.toString(bytes4));

        byte[] bytes3 = decodeHex(chars);
        System.out.println(StrUtil.toString(bytes3));

        long l = byteToLong(bytes);
        System.out.println(l);

        int currentTimeMillisInt = (int) currentTimeMillis;
        System.out.println(currentTimeMillisInt);

        byte[] bytes1 = intToByte(currentTimeMillisInt);
        System.out.println(StrUtil.toString(bytes1));

        int i = byteToInt(bytes1);
        System.out.println(i);

        short currentTimeMillisShort = (short) currentTimeMillisInt;
        System.out.println(currentTimeMillisShort);

        byte[] bytes2 = shortToByte(currentTimeMillisShort);
        System.out.println(StrUtil.toString(bytes2));

        short i1 = byteToShort(bytes2);
        System.out.println(i1);


    }


}
