package com.wujiuye.hotkit.json.webmvc;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Iterator;
import java.util.List;

/**
 * 替换spring mvc消息序列化反序列化框架
 *
 * @author wujiuye 2020/08/15
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
 */
@Conditional(WebMvcCondition.class)
@Configuration(proxyBeanMethods = false)
public class WebMvcJsonConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        Iterator<HttpMessageConverter<?>> iterable = messageConverters.iterator();
        AllEncompassingFormHttpMessageConverter formHttpMessageConverter = null;
        while (iterable.hasNext()) {
            HttpMessageConverter<?> converter = iterable.next();
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                iterable.remove();
            }
            if (converter instanceof AllEncompassingFormHttpMessageConverter) {
                iterable.remove();
                formHttpMessageConverter = new AllEncompassingFormHttpMessageConverter() {
                    @Override
                    public void addPartConverter(HttpMessageConverter<?> partConverter) {
                        if (partConverter instanceof MappingJackson2HttpMessageConverter) {
                            partConverter = messageConverter();
                        }
                        super.addPartConverter(partConverter);
                    }
                };
            }
        }
        if (formHttpMessageConverter != null) {
            messageConverters.add(formHttpMessageConverter);
        }
        messageConverters.add(messageConverter());
    }

    private JsonHttpMessageConverter messageConverter() {
        return new JsonHttpMessageConverter();
    }

}
