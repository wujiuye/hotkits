package com.wujiuye.hotkit.util;

/**
 * @author wujiuye 2020/04/21
 */
public class StringUtils {

    public static final String EMPTY = "";

    public static String emptyStr() {
        return EMPTY;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isNullOrEmpty(s);
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    private static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            return str2 == null;
        } else if (null == str2) {
            return false;
        } else {
            return ignoreCase ? str1.toString().equalsIgnoreCase(str2.toString()) : str1.equals(str2);
        }
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

    /**
     * 转为驼峰命名
     *
     * @param str 字符串
     * @return
     */
    public static String toHumpString(String str) {
        StringBuilder builder = new StringBuilder();
        int offset = 'A' - 'a';
        for (int i = 0; i < str.length(); ) {
            char ch = str.charAt(i);
            if (i == 0 && ch >= 'a' && ch <= 'z') {
                builder.append((char) (ch + offset));
                i++;
            } else if (ch == '_') {
                char aCh = str.charAt(i + 1);
                if (aCh >= 'a' && aCh <= 'z') {
                    builder.append((char) (aCh + offset));
                } else {
                    builder.append(aCh);
                }
                i += 2;
            } else {
                builder.append(ch);
                i++;
            }
        }
        return builder.toString();
    }

}
