# hotkits
Java开发通用组件集合。

## 版本信息
* 2021.001.RC 2021年的第一个发布版本，添加`hotkit-json`、`hotkit-redis`、`hotkit-util`、`hotkit-spring-common`组件；

## 该项目包含的组件 
* hotkit-json：`json`适配器组件，让切换`json`解析框架只需要切换依赖包即可。使用方式见该模块下的[README.MD](hotkit-json/README.MD)
* hotkit-json-adapter：hotkit-json-adapter是`hotkit-json`适配`springboot`的`starter`包，自动将`webmvc`或`webflux`的`json`解析工作交给`hotkit-json`完成，以此让整个项目使用同一套`json`解析配置。使用方式见该模块下的[README.MD](hotkit-json-adapter/README.MD)
* hotkit-redis：Redis客户端适配器组件，让切换Redis客户端框架只需要切换依赖包即可，提供反应式编程API。使用方式见该模块下的[README.MD](hotkit-redis/README.MD)
* hotkit-util：封装通用工具类，如加解密、SPI、反射、字符串、集合、多线程编程等
* hotkit-spring-common：提供基于轮询的动态配置接口，实现自定义注解自动注入的后置处理器、多环境隔离的容器工厂
