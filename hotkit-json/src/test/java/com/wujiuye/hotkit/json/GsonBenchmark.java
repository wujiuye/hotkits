package com.wujiuye.hotkit.json;

import com.google.gson.Gson;
import com.wujiuye.hotkit.json.gson.GsonParser;
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
@Threads(10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 50, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class GsonBenchmark {

    private GsonParser gsonParser = new GsonParser();
    private static final Gson gson = new Gson();

    @Param(value = {"{\"date\":\"2020-06-22 13:00:00\",\"flag\":true,\"id\":5,\"content\":\"json test...\"}"})
    private String jsonStr;

    @Benchmark
    @Test
    public void testGsonSingle() {
        gson.fromJson(jsonStr, JsonTestModel.class);
    }

    /**
     * 非单例性能太差，所以改为单例了
     */
    @Benchmark
    @Test
    public void testGsonNoSingle() {
        gsonParser.fromJson(jsonStr, JsonTestModel.class);
    }

}
