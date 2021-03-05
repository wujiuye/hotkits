package com.wujiuye.hotkit.json.util;

/**
 * @author wujiuye 2020/04/21
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * 判断字符串是否是数字
     *
     * @param s 字符串
     * @return
     */
    public static boolean isNumber(String s) {
        int pCount = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '.') {
                if (pCount > 0) {
                    return false;
                } else {
                    pCount++;
                }
                continue;
            }
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

}
