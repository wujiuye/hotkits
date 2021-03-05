package com.wujiuye.hotkit.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * double保留两位小时
 *
 * @author wujiuye 2020/07/10
 */
class DoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (aDouble == null) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeNumber(BigDecimal.valueOf(aDouble)
                .setScale(2, BigDecimal.ROUND_DOWN)
                .doubleValue());
    }

}

/**
 * double保留两位小时
 *
 * @author wujiuye 2020/07/10
 */
class DoubleDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String value = jsonParser.getText();
        if (StringUtils.isNullOrEmpty(value) || "null".equalsIgnoreCase(value)) {
            return null;
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

}
