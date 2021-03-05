# hotkit-json
`JSON`适配器组件，让切换`JSON`解析框架只需要切换依赖包即可。

目前hotkit-json已适配jackson、gson，hotkit-json根据项目中导入了哪个json解析工具就使用哪个json解析框架。

* 注意：如果项目中即导入了jackson又导入了gson，那么那适配代码的执行顺序优先选择。
```java
public class JsonUtils {
    private static JsonParser chooseJsonParser;
    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            classLoader.loadClass("com.google.gson.Gson");
            chooseJsonParser = new GsonParser();
        } catch (ClassNotFoundException e) {
            try {
                classLoader.loadClass("com.fasterxml.jackson.databind.ObjectMapper");
                chooseJsonParser = new JacksonParser();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("未找到任何json包，请先在当前项目的依赖配置文件中加入 gson或fackson");
            }
        }
    }
}
```
## 使用说明
1、在项目中添加hotkit-json依赖
```xml
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>hotkit-json</artifactId>
    <version>{version}</version>
</dependency>
```
2、在项目中添加jackson或者gson的依赖
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>{version}</version>
</dependency>
```

