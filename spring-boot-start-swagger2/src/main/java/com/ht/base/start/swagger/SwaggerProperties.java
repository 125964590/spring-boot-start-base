package com.ht.base.start.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "jbzm.swagger")
@NoArgsConstructor
public class SwaggerProperties {

    /**
     * 是否开启swagger
     **/
    private Boolean enabled;
    /**
     * 分组名字
     */
    private String groupName = "郑毅我爱你";
    /**
     * 扫描路径
     */
    private String path = "/**";
    /**
     * 标题
     **/
    private String title = "";
    /**
     * 描述
     **/
    private String description = "";
    /**
     * 版本
     **/
    private String version = "";
    /**
     * 忽略的参数类型
     **/
    private List<Class> ignoredParameterTypes = null;

    public String lol(){
        return "ll";
    }


}


