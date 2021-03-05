package com.wujiuye.hotkit.json.util;

/**
 * @author wujiuye 2021/01/14
 */
public class XssFilterUtils {

    /**
     * script的正则表达式
     */
    private final static String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    /**
     * style的正则表达式
     */
    private final static String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    /**
     * HTML标签的正则表达式
     */
    private final static String regExHtml = "<[^>]+>";

    /**
     * 防XSS攻击过滤器
     *
     * @param sourceStr 原字符串
     * @return 过滤后的字符串
     */
    public static String xssFilter(String sourceStr) {
        if (StringUtils.isNullOrEmpty(sourceStr)) {
            return sourceStr;
        }
        return sourceStr.replaceAll(regExScript, "")
                .replaceAll(regExStyle, "")
                .replaceAll(regExHtml, "");
    }

}
