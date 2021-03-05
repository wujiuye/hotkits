package com.wujiuye.hotkit.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wujiuye.hotkit.json.util.DateFormatException;
import com.wujiuye.hotkit.json.util.DateFormatUtils;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期序列化
 *
 * @author wujiuye 2020/05/08
 */
class DateSerializer extends JsonSerializer<Date> {

    private SimpleDateFormat sdf;

    public DateSerializer(String datePattern) {
        if (StringUtils.isNullOrEmpty(datePattern)) {
            this.sdf = null;
            return;
        }
        this.sdf = new SimpleDateFormat(datePattern);
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (sdf == null) {
            jsonGenerator.writeNumber(date.getTime());
        } else {
            jsonGenerator.writeString(sdf.format(date));
        }
    }

}

/**
 * 日期反序列化
 *
 * @author wujiuye 2020/05/08
 */
class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String datetime = jsonParser.getText();
        if (StringUtils.isNumber(datetime)) {
            if (datetime.length() == 10) {
                return new Date(Long.parseLong(datetime) * 1000);
            } else {
                return new Date(Long.parseLong(datetime));
            }
        }
        SimpleDateFormat sfg = DateFormatUtils.chooseSimpleDateFormat(datetime);
        try {
            return sfg.parse(datetime);
        } catch (ParseException e) {
            throw new DateFormatException(datetime);
        }
    }

}

