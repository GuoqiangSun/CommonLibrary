package cn.com.swain.baselib.crypt;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.com.swain.baselib.util.StrUtil;


/**
 * @author: Guoqiang_Sun
 * @date: 2020/3/18 11:23
 * @description:
 */
public class DesCrypter {
    private DesCryptType type;
    private String groupMode; //分组模式 ECB、CBC、OFB、CFB
    private String fillMode; //填充模式 NoPadding、PKCS5Padding
    private byte[] initVector;

    public DesCrypter(DesCryptType type) {
        this(type, "ECB", "PKCS5Padding");
    }

    public DesCrypter(DesCryptType type, String groupMode) {
        this(type, groupMode, "PKCS5Padding");
    }

    public DesCrypter(DesCryptType type, String groupMode, String fillMode) {

        if (StrUtil.isBlank(groupMode)) {
            groupMode = "ECB";
        }
        if (StrUtil.isBlank(fillMode)) {
            fillMode = "PKCS5Padding";
        }
        this.type = type;
        this.groupMode = groupMode;
        this.fillMode = fillMode;
        initVector = hexStringToByte("0000000000000000");
    }

    /**
     * 生成随机密钥key
     *
     * @return
     */
    public byte[] generateRandomKey() {
        byte[] result = null;
        String des = getDesString();
        SecretKey key = generateSecretKey(des, hexStringToByte("11111111111111111111111111111111"));
        if (key != null) {
            result = key.getEncoded();
        }
        return result;
    }

    private String getDesString() {
        String des = "";
        if (this.type == DesCryptType.DES) {
            des = "DES";
        } else {
            des = "DESede";
        }

        return des;
    }

    /**
     * 使用DES算法加密data
     *
     * @param data     带加密的明文
     * @param keyBytes 密钥
     * @return 密文
     */
    public byte[] encrypt(byte[] data, byte[] keyBytes) {
        byte[] result = null;
        String des = getDesString();
        try {
            //生成密钥
            SecretKey key = generateSecretKey(des, keyBytes);
            if (key == null) {
                return null;
            }

            //加密
            String cipherStr = getCipherStr(data.length, groupMode, fillMode);
            Cipher cipher = Cipher.getInstance(cipherStr);
            if (groupMode.equals("ECB")) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initVector));
            }
            result = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设置分组模式的初始向量
     *
     * @param data
     * @return
     */
    public boolean setInitVector(byte[] data) {
        if (data == null || data.length != 8) {
            return false;
        }
        System.arraycopy(data, 0, initVector, 0, 8);
        return true;
    }

    private String getCipherStr(int length, String groupMode, String fillMode) {
        String des = getDesString();
        String currFillMode = fillMode;
        if (fillMode.equals("PKCS5Padding") && length % 8 == 0) {
            //数据长度刚好是8的倍数，不填充
            currFillMode = "NoPadding";
        }
        return des + "/" + groupMode + "/" + currFillMode;
    }

    /**
     * 使用DES算法加密data
     *
     * @param data     待解密的密文
     * @param keyBytes 密钥
     * @return 明文
     */
    public byte[] decrypt(byte[] data, byte[] keyBytes) {
        byte[] result = null;
        String des = getDesString();
        try {
            //生成密钥
            SecretKey key = generateSecretKey(des, keyBytes);
            if (key == null) {
                return null;
            }

            //解密
            String cipherStr = getCipherStr(data.length, groupMode, fillMode);
            Cipher cipher = Cipher.getInstance(cipherStr);
            if (groupMode.equals("ECB")) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initVector));
            }
            result = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private SecretKey generateSecretKey(String des, byte[] keyBytes) {
        //第一种，原值返回
        SecretKey key = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(des);
            key = factory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        第二种，原值返回
//        key = new SecretKeySpec(keyBytes, des);

        //第三种，随机生成
//        try {
//            KeyGenerator generator = KeyGenerator.getInstance(des);
//            int keyLen = getKeyLength();
//            generator.init(keyLen, new SecureRandom(keyBytes));
//            key = generator.generateKey();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        System.out.println("生成的DES密钥为：" + (key != null ? bcd2str(key.getEncoded()) : "null"));
        return key;
    }

    private int getKeyLength() {
        int len = 56;
        switch (this.type) {
            case DES:
                len = 56;
                break;
            case DES_2:
                len = 112;
                break;
            case DES_3:
                len = 168;
                break;
        }
        return len;
    }

    /**
     * 获取当前key的校验值
     *
     * @param key
     * @return 4个字节校验结果
     */
    public byte[] getCheckValue(byte[] key) {
        byte[] zero = hexStringToByte("0000000000000000");
        byte[] result = encrypt(zero, key);
        if (result != null && result.length > 4) {
            //截取四个字节
            byte[] tmp = new byte[4];
            System.arraycopy(result, 0, tmp, 0, 4);
            result = tmp;
        }
        return result;
    }

    public enum DesCryptType {
        DES,
        DES_2,
        DES_3
    }

    static char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String bcd2str(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);

        for (int i = 0; i < b.length; ++i) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }

        return sb.toString();
    }


    /**
     * string to bcd.
     * "0102"->0x01 0x02
     *
     * @param asc
     * @return
     */
    public static byte[] str2bcd(final String asc) {
        String tmpStr = asc;
        if (tmpStr.length() % 2 != 0) {
            tmpStr = "0" + asc;
        }
        int len = tmpStr.length() / 2;
        byte[] bbt = new byte[len];
        for (int p = 0; p < len; ++p) {
            bbt[p] = (byte) ((getCharValue(tmpStr.getBytes()[2 * p]) << 4) +
                    getCharValue(tmpStr.getBytes()[2 * p + 1]));
        }

        return bbt;
    }


    private static int getCharValue(byte srcByte) {
        int ret;
        if (srcByte >= 'a' && srcByte <= 'z') {
            ret = srcByte - 'a' + 10;
        } else if (srcByte >= 'A' && srcByte <= 'Z') {
            ret = srcByte - 'A' + 10;
        } else {
            ret = srcByte - '0';
        }
        return ret;
    }


    public static byte[] hexStringToByte(String hexStr) {
        int len = hexStr.length();
        if ((len & 0x01) == 0x01) {
            throw new IllegalArgumentException("hexStr is invalid: " + hexStr);
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (Short.valueOf(hexStr.substring(i, i + 2).toString(), 16) & 0xff);
        }
        return data;
    }


    public static String hexString(byte[] b) {
        if (b == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(128);

        for (byte bs : b) {
            if ((bs & 0xFF) <= 0x0F) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(bs & 0xFF).toUpperCase());
        }

        return sb.toString();
    }


    public static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(), 0, s.length() >> 1);
        } else {
            // Padding left zero to make it even size #Bug raised by tommy
            return hex2byte("0" + s);
        }
    }

    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
        }
        return d;
    }

}

