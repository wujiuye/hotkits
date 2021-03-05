# hotkit-json-adapter

`hotkit-json-adapter`是`hotkit-json`适配`springboot`的`starter`包，自动将`webmvc`或`webflux`的`json`解析工作交给`hotkit-json`完成，以此让整个项目使用同一套`json`解析配置。

## 使用
在项目中添加hotkit-json依赖
```xml
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>hotkit-json</artifactId>
    <version>{version}</version>
</dependency>
```
在项目中添加hotkit-json-adapter依赖
```xml
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>hotkit-json-adapter</artifactId>
    <version>{version}</version>
</dependency>
```
