package com.wujiuye.hotkit.json;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 序列化配置
 *
 * @author wujiuye 2021/05/10
 */
@Configuration
public class HotkitSerializeConfiguration implements InitializingBean {

    @Value("${spring.hot-kit.serialize:false}")
    private boolean serializeNulls;

    @Value("${spring.hot-kit.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Override
    public void afterPropertiesSet() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setSerializeNulls(serializeNulls);
        serializeConfig.setDateFormat(dateFormat);
        JsonUtils.setDefaultSerializeConfig(serializeConfig);
    }

}
