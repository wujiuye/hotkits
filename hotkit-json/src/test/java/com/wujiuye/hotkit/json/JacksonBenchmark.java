package com.wujiuye.hotkit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wujiuye.hotkit.json.jackson.JacksonParser;
import com.wujiuye.hotkit.json.model.JsonTestModel;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * json解析框架性能测试
 *
 * @author wujiuye 2020/06/22
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Threads(10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 50, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class JacksonBenchmark {

    private JacksonParser jacksonParser = new JacksonParser();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Param(value = {"{\"date\":\"2020-06-22 13:00:00\",\"flag\":true,\"id\":5,\"content\":\"json test...\"}"})
    private String jsonStr;

    @Benchmark
    @Test
    public void testJacksonSingle() {
        try {
            objectMapper.readValue(jsonStr, JsonTestModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @Test
    public void testJacksonNoSingle() {
        jacksonParser.fromJson(jsonStr, JsonTestModel.class);
    }

}
