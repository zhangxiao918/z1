package com.skymobi.cac.maopao.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author Archibald.Wang
 */
public class DESUtils {

    public static final String PASSWORD_CRYPT_KEY = "__jDlog_";

    private final static String algorithm = "DES/ECB/NoPadding";

    /**
     * 加密
     * 
     * @param src
     *            长度为8的倍数
     * @param key
     *            长度为8的倍数
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(algorithm);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey);

        // 获取数据并加密正式执行加密操作
        return cipher.doFinal(src);

    }

    /**
     * 解密
     * 
     * @param src
     *            长度为8的倍数
     * @param key
     *            长度为8的倍数
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(algorithm);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey);

        // 正式执行解密操作
        return cipher.doFinal(src);

    }

    public final static byte[] encrypt(String source, byte[] key) {

        try {

            //补足8位
            byte[] sourceBytes = source.getBytes();
            int paddingSize = 8 - (sourceBytes.length % 8);
            byte[] paddingBytes = new byte[paddingSize];
            for (int i = 0; i < paddingBytes.length; i++) {
                paddingBytes[i] = (byte) 0x0;
            }
            sourceBytes = ArrayUtils.addAll(sourceBytes, paddingBytes);

            return encrypt(sourceBytes, key);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public final static byte[] decrypt(String data, byte[] key) {
        try {
            return decrypt(StringUtils.hex2byte(data.getBytes()), key);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] argv) throws Exception {
        String source = "Don't tell anybody!";
        byte[] encrypted = DESUtils.encrypt(source, PASSWORD_CRYPT_KEY.getBytes());
        byte[] decrypted = DESUtils.decrypt(encrypted, PASSWORD_CRYPT_KEY.getBytes());

        System.out.println("source:" + source);
        System.out.println("encrypted:" + StringUtils.byte2Hex(encrypted));
        System.out.println("decrypted:" + new String(decrypted));
    }
}

