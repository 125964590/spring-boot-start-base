## quick start
通过导入jar包来引入权限相关代码,更改yml配置文件设置服务网关以及拦截路径

### 导入jar包
```
        <dependency>
            <groupId>com.huatu</groupId>
            <artifactId>user-center-start-old</artifactId>
            <version>1.0-${project.environment}-SNAPSHOT</version>
        </dependency>
```

### 添加相关配置
在yml中user-center下添加如下属性
![相关配置](https://ws1.sinaimg.cn/large/006rYg5Lly1fxob14yitjj313y0cw0ud.jpg)

```
public class UserCenterProperties {
    /**
     * whether to enable
     */
    private boolean enable;

    /**
     * need pass authenticated request
     */
    private String[] authPassPaths = {"/null"};

    /**
     * need authenticated request
     */
    private String[] authPaths = {"/null"};

    /**
     * http://*******
     */
    private String url;

    /**
     * default context path
     */
    private String path = "/";
}
```
其中url以及enable默认在apollo中统一配置

**auth-paths**
用来指定请求拦截路径,通过数组的方式进行配置,支持模糊配置
```
user-center:
  auth-paths:
    - /back/**
    - /end/test
```

**auth-pass-paths**
用来指定放行路径,该配置结合auth-paths使用,在配置模糊拦截的时候添加需要放行的请求路径
```
user-center:
  auth-paths:
    - /back/**
    - /end/test
  auth-pass-paths:
    - /back/test
```
如上配置将会拦截所有/back下的请求但是/back/test会被放行