package com.wujiuye.hotkit.json;

import com.wujiuye.hotkit.json.gson.GsonParser;
import com.wujiuye.hotkit.json.jackson.JacksonParser;
import com.wujiuye.hotkit.json.model.JsonTestModel;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * json解析框架性能测试
 *
 * @author wujiuye 2020/06/22
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Threads(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class JsonBenchmark {

    private GsonParser gsonParser = new GsonParser();
    private JacksonParser jacksonParser = new JacksonParser();

    @Param(value = {
            "{\"date\":\"2020-06-22 13:00:00\",\"flag\":true,\"id\":5,\"content\":\"json test...\"}",
            "{\"date\":\"2020-06-22 13:00:00\",\"content\":\"json test...\"}",
            "{\"flag\":true,\"id\":5,\"content\":\"json test...\"}"})
    private String jsonStr;

    @Benchmark
    @Test
    public void testGson() {
        // System.out.println("current Thread:" + Thread.currentThread().getName() + "==>" + gsonParser.hashCode());
        gsonParser.fromJson(jsonStr, JsonTestModel.class);
    }

    @Benchmark
    @Test
    public void testJackson() {
        // System.out.println("current Thread:" + Thread.currentThread().getName() + "==>" + jacksonParser.hashCode());
        jacksonParser.fromJson(jsonStr, JsonTestModel.class);
    }

}
