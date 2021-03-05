package com.wujiuye.hotkit.json.util;

/**
 * 日期格式化异常
 *
 * @author wujiuye 2020/09/09
 */
public class DateFormatException extends RuntimeException {

    private String dateStr;

    public DateFormatException(String dateStr) {
        super("日期格式化异常！");
        this.dateStr = dateStr;
    }

    @Override
    public String getLocalizedMessage() {
        return this.getMessage();
    }

    @Override
    public String getMessage() {
        return "日期格式化异常，日期字符串：" + dateStr;
    }

}
