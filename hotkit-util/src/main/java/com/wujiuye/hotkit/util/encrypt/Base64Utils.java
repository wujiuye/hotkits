package com.wujiuye.hotkit.util.encrypt;

import java.util.Base64;

/**
 * Base64编解码工具
 *
 * @author wujiuye 2020/06/15
 */
public class Base64Utils {

    /**
     * Base64编码.
     */
    public static String encode(byte[] input) {
        return new String(Base64.getEncoder().encode(input));
    }

    /**
     * Base64解码.
     */
    public static String decode(String input) {
        byte[] bytes = Base64.getDecoder().decode(input);
        return new String(bytes);
    }

}
