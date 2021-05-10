package com.wujiuye.hotkit.json;

/**
 * 序列化配置
 *
 * @author wujiuye 2021/05/10
 */
public class SerializeConfig {

    private boolean serializeNulls = false;
    private String dateFormat;

    public boolean isSerializeNulls() {
        return serializeNulls;
    }

    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

}
