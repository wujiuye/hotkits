package com.wujiuye.hotkit.json.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wujiuye.hotkit.json.util.DateFormatException;
import com.wujiuye.hotkit.json.util.DateFormatUtils;
import com.wujiuye.hotkit.json.util.DateUtils;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日期类型的转换，解决web端时间字段使用时间戳也使用字符串的问题
 *
 * @author wujiuye 2020/05/07
 */
public class DateTypeAdapter extends TypeAdapter<Date> {

    private String serializerDatePattern;

    public DateTypeAdapter(String serializerDatePattern) {
        this.serializerDatePattern = serializerDatePattern;
    }

    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        if (date == null) {
            jsonWriter.nullValue();
        } else {
            if (StringUtils.isNullOrEmpty(serializerDatePattern)) {
                jsonWriter.value(date.getTime());
            } else {
                String dateFormatAsString = DateUtils.dateToString(date, serializerDatePattern);
                jsonWriter.value(dateFormatAsString);
            }
        }
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        String datetime = jsonReader.nextString();
        if (StringUtils.isNumber(datetime)) {
            if (datetime.length() == 10) {
                return new Date(Long.parseLong(datetime) * 1000);
            } else {
                return new Date(Long.parseLong(datetime));
            }
        }
        try {
            SimpleDateFormat sdf = DateFormatUtils.chooseSimpleDateFormat(datetime);
            return sdf.parse(datetime);
        } catch (ParseException e) {
            throw new DateFormatException(datetime);
        }
    }

}
