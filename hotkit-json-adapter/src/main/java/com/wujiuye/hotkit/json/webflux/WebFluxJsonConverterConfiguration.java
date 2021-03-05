package com.wujiuye.hotkit.json.webflux;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wujiuye.hotkit.json.JsonUtils;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Map;

/**
 * 响应式webflux配置
 *
 * @author wujiuye 2020/10/29
 */
@Conditional(WebFluxCondition.class)
@Configuration
public class WebFluxJsonConverterConfiguration implements WebFluxConfigurer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(
                new Jackson2JsonEncoder(objectMapper) {

                    @Override
                    public DataBuffer encodeValue(Object value, DataBufferFactory bufferFactory, ResolvableType valueType, MimeType mimeType, Map<String, Object> hints) {
                        byte[] bytes = JsonUtils.toJsonString(value).getBytes();
                        DataBuffer buffer = bufferFactory.allocateBuffer(bytes.length);
                        return buffer.write(bytes);
                    }

                }
        );

        configurer.defaultCodecs().jackson2JsonDecoder(
                new Jackson2JsonDecoder(objectMapper) {

                    @Override
                    public Object decode(DataBuffer dataBuffer, ResolvableType targetType, MimeType mimeType, Map<String, Object> hints) throws DecodingException {
                        try {
                            return JsonUtils.fromJson(dataBuffer.asInputStream(), targetType.getType());
                        } catch (Throwable ex) {
                            throw new RuntimeException("解码异常，targetType=" + targetType.getType());
                        } finally {
                            DataBufferUtils.release(dataBuffer);
                        }
                    }
                }
        );
    }

}
