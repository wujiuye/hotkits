package com.wujiuye.hotkit.json.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Double类型适配器
 *
 * @author wujiuye 2020/07/10
 */
public class DoubleTypeAdapter extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        String doublev = in.nextString();
        if (StringUtils.isNullOrEmpty(doublev) || "null".equalsIgnoreCase(doublev)) {
            return null;
        }
        return new BigDecimal(doublev).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

}
