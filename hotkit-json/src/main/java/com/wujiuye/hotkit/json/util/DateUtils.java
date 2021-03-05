package com.wujiuye.hotkit.json.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wujiuye 2020/04/23
 */
public class DateUtils {

    /**
     * 日期转字符串
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

}
