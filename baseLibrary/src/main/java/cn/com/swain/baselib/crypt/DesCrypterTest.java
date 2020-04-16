package cn.com.swain.baselib.crypt;

/**
 * @author: Guoqiang_Sun
 * @date: 2020/3/18 11:30
 * @description:
 */
public class DesCrypterTest {

    public static void main(String[] args) {
        test();
    }


    private static void test2() {
        final String groupMode = "ECB"; // : "CBC" ，"ECB"
        DesCrypter desCrypter = new DesCrypter(DesCrypter.DesCryptType.DES, groupMode);

        byte[] data = DesCrypter.hex2byte("0000000000000000" +
                "     1111111111111111" +
                "     2222222222222222" +
                "     3333333333333333");
        byte[] keyBytes = DesCrypter.hex2byte("32323232323232323131313131313131");


//        byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
//        byte[] keyBytes = new byte[]{0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11};


//        DES密钥长度为8个字节
        byte[] result = desCrypter.encrypt(data, keyBytes);
        System.out.println("encrypt bcd2str:" + DesCrypter.hexString(result));

        byte[] decrypt = desCrypter.decrypt(result, keyBytes);
        System.out.println("decrypt bcd2str:" + DesCrypter.hexString(decrypt));
    }


    /**
     * //两组密钥校验位不同，加密结果相同
     * 密钥1     1111111111111111    第8位都为1
     * 数据    + 0102030405060708
     * 结果    = 73331971A6E1AB01
     * -------------------------------------
     * 密钥2     1010101010101010   第8位都为0
     * 数据    + 0102030405060708
     * 结果    = 73331971A6E1AB01
     * ————————————————
     */
    private static void test() {
        final String groupMode = "ECB"; // : "CBC" ，"ECB"
        DesCrypter desCrypter = new DesCrypter(DesCrypter.DesCryptType.DES, groupMode);

        byte[] data = DesCrypter.hex2byte("0102030405060708");
        byte[] keyBytes = DesCrypter.hex2byte("1111111111111111");


//        byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
//        byte[] keyBytes = new byte[]{0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11};


//        DES密钥长度为8个字节
        byte[] result = desCrypter.encrypt(data, keyBytes);
        System.out.println("encrypt hexString:" + DesCrypter.hexString(result));
        System.out.println("encrypt bcd2str:" + DesCrypter.bcd2str(result));

        byte[] decrypt = desCrypter.decrypt(result, keyBytes);
        System.out.println("decrypt hexString:" + DesCrypter.hexString(decrypt));
        System.out.println("decrypt bcd2str:" + DesCrypter.bcd2str(decrypt));
    }

}
