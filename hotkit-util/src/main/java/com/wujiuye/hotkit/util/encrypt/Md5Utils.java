package com.wujiuye.hotkit.util.encrypt;

import java.security.MessageDigest;

/**
 * MD5编码工具(MD5不能解密)
 *
 * @author wujiuye 2020/06/15
 */
public class Md5Utils {

    private final static char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 加密
     *
     * @param s 原文
     * @return
     */
    public static String encode(String s) {
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = DIGITS_LOWER[byte0 >>> 4 & 0xf];
                str[k++] = DIGITS_LOWER[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
