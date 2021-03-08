package com.wujiuye.hotkit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/6/24 {描述：}
 */
public class UrlUtils {

    public static class UrlEntity {
        /**
         * 基础url
         */
        public String baseUrl;
        /**
         * url参数
         */
        public Map<String, String> params;

        public Map<String, String> getParams() {
            return params;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

    }

    /**
     * 解析url
     *
     * @param url
     * @return
     */
    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        if (StringUtils.isNullOrEmpty(url)) {
            return entity;
        }
        String[] urlParts = url.split("\\?");
        entity.setBaseUrl(urlParts[0]);
        // 没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        // 有参数
        String[] params = urlParts[1].split("&");
        entity.setParams(new HashMap<>());
        for (String param : params) {
            int index = param.indexOf("=");
            if (index == param.length() - 1 || index == -1) {
                continue;
            }
            String key = param.substring(0, index);
            String value = param.substring(index + 1);
            entity.getParams().put(key, value);
        }
        return entity;
    }

}
