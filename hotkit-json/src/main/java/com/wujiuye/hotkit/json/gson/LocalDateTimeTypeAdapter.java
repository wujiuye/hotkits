package com.wujiuye.hotkit.json.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wujiuye.hotkit.json.util.DateFormatUtils;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime日期类型的转换
 *
 * @author wujiuye 2020/05/08
 */
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    private int zone = 8;
    private DateTimeFormatter serializerFormatter;

    public LocalDateTimeTypeAdapter(String serializerDatePattern) {
        if (StringUtils.isNullOrEmpty(serializerDatePattern)) {
            serializerFormatter = null;
            return;
        }
        this.serializerFormatter = DateTimeFormatter.ofPattern(serializerDatePattern);
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime time) throws IOException {
        if (serializerFormatter == null) {
            jsonWriter.value(time.toInstant(ZoneOffset.ofHours(zone)).toEpochMilli());
        } else {
            jsonWriter.value(time.format(serializerFormatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String datetime = jsonReader.nextString();
        if (StringUtils.isNumber(datetime)) {
            long times = Long.parseLong(datetime);
            if (datetime.length() == 13) {
                times /= 1000;
            }
            return LocalDateTime.ofEpochSecond(times, 0, ZoneOffset.ofHours(zone));
        } else {
            DateTimeFormatter formatter = DateFormatUtils.chooseDateTimeFormatter(datetime);
            return LocalDateTime.parse(datetime, formatter);
        }
    }

}
