package com.wujiuye.hotkit.json.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * @author wujiuye 2020/04/26
 */
public class DateFormatUtils {

    public static String datePattern_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static String datePattern_yyyyMMdd = "yyyy-MM-dd";
    public static String datePattern_yyyyMMdd2 = "yyyy.MM.dd";
    public static String datePattern_yyyyMMdd3 = "yyyy年MM月dd日";

    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(datePattern_yyyyMMddHHmmss);
    public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(datePattern_yyyyMMdd);
    public static DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern(datePattern_yyyyMMdd2);
    public static DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern(datePattern_yyyyMMdd3);

    public static DateTimeFormatter chooseDateTimeFormatter(String str) {
        if (str.contains(" ")) {
            if (str.contains("-")) {
                return formatter1;
            }
        } else {
            if (str.contains("-")) {
                return formatter2;
            } else if (str.contains(".")) {
                return formatter3;
            } else if (str.contains("年")) {
                return formatter4;
            }
        }
        throw new DateFormatException(str);
    }

    public static SimpleDateFormat chooseSimpleDateFormat(String str) {
        if (str.contains(" ")) {
            if (str.contains("-")) {
                return new SimpleDateFormat(datePattern_yyyyMMddHHmmss);
            }
        } else {
            if (str.contains("-")) {
                return new SimpleDateFormat(datePattern_yyyyMMdd);
            } else if (str.contains(".")) {
                return new SimpleDateFormat(datePattern_yyyyMMdd2);
            } else if (str.contains("年")) {
                return new SimpleDateFormat(datePattern_yyyyMMdd3);
            }
        }
        throw new DateFormatException(str);
    }

}
