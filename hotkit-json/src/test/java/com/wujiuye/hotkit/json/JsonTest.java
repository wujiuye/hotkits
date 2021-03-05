package com.wujiuye.hotkit.json;

import com.wujiuye.hotkit.json.gson.GsonParser;
import com.wujiuye.hotkit.json.jackson.JacksonParser;
import com.wujiuye.hotkit.json.model.JsonTestModel;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonTest {

    @Test
    public void testJackson() {
        System.out.println(new JacksonParser()
                .toJsonString(new JsonTestModel().setDv(123123.12312312).setContent("这个i不悱不发==xxx"), true, null));
    }

    @Test
    public void testGson() {
        System.out.println(new GsonParser()
                .toJsonString(new JsonTestModel().setDv(0.121312312).setContent("这个i不悱不发==xxx"), true, null));
    }

    @Test
    public void testJackson2() {
        System.out.println(new JacksonParser().fromJson("{\"dv\":0.1223213213}", JsonTestModel.class));
    }

    @Test
    public void testGson2() {
        System.out.println(new GsonParser().fromJson("{\"dv\":0.1223213213,\"content\":\"这个i不悱不发==xxx\"}", JsonTestModel.class));
    }

    public static class Object1 {
        private transient String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Object2 extends Object1 {

    }

    @Test
    public void testGsonMap() {
        // gson bug
        Map<String, Object> data = new HashMap<String, Object>() {{
            put("xxx", "yyy");
        }};
        System.out.println(JsonUtils.toJsonString(data));

        HashMap<String, Object> data2 = new HashMap<>();
        data2.put("xxx", new JsonTestModel().setId(10));
        System.out.println(JsonUtils.toJsonString(data2));

        String v = JsonUtils.toJsonString(data2);
        Map<String, JsonTestModel> v2 = JsonUtils.fromJsonMap(v, new TypeReference<Map<String, JsonTestModel>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
        System.out.println(v2.get("xxx"));

        Object1 object = new Object2();
        object.setName("xxxxx");
        System.out.println(JsonUtils.toJsonString(object));
    }

}
