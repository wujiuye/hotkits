# hotkit-redis
`Redis`客户端适配器组件，让切换Redis客户端框架只需要切换依赖包即可，提供反应式编程API。

目前hotkit-redis已适配jedis、lettuce，hotkit-redis根据项目中导入了哪个redis客户端框架自动适配。

## 提供的特性
* 1、API见[RedisUtils](src/main/java/com/wujiuye/hotkit/redis/RedisUtils.java)工具类；
* 2、支持Spring自动注入特性，使用[@RedisReference](src/main/java/com/wujiuye/hotkit/redis/autowired/RedisReference.java)注解；
* 3、支持多DB切换，使用[@RedisDataSource](src/main/java/com/wujiuye/hotkit/redis/multidb/RedisDataSource.java)注解，或者编程式切换；
* 4、支持反应式编程，请使用[ReactiveRedisCacheTemplate](src/main/java/com/wujiuye/hotkit/redis/template/ReactiveRedisCacheTemplate.java)操作redis，且仅支持声明式切换DB，默认使用DB为0；

## 使用说明
1、在项目中添加hotkit-redis依赖
```xml
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>hotkit-redis</artifactId>
    <version>{version}</version>
</dependency>
```
2、在项目中添加jedis或者lettuce的依赖
```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-coreE</artifactId>
    <version>{version}</version>
</dependency>
```
3、由于hotkit-redis依赖hotkit-json，所以如果项目中没有导入json解析框架，需要添加json解析框架依赖

在项目中添加jackson或者gson的依赖
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>{version}</version>
</dependency>
```

