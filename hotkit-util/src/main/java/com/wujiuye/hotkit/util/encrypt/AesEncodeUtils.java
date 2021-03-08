package com.wujiuye.hotkit.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author wujiuye aes加密工具
 */
public final class AesEncodeUtils {

    private static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key getKey(String keySeed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keySeed.getBytes());
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(secureRandom);
        return generator.generateKey();
    }

    /**
     * 加密
     *
     * @param input 原文
     * @param key   密钥
     * @return
     */
    public static String aesEncode(String input, String key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return new String(HexUtils.encodeHex(cipher.doFinal(input.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param input 密文
     * @param key   密钥
     * @return
     */
    public static String aesDecode(String input, String key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            return new String(cipher.doFinal(HexUtils.decodeHex(input.toCharArray())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
