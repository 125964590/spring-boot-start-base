package com.ht.base.start.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "jbzm.swagger")
@NoArgsConstructor
public class SwaggerProperties {

    /**
     * enable swagger
     **/
    private Boolean enabled = true;
    /**
     * group name
     */
    private String groupName = "郑毅我爱你";
    /**
     * package scan path (no *)
     */
    private String path = "com.demo";
    /**
     * title
     **/
    private String title = "我曹牛逼";
    /**
     * description
     **/
    private String description = "6666666666666";
    /**
     * version
     **/
    private String version = "v 0.0.1";
    /**
     * ignored parameter types
     **/
    private List<Class> ignoredParameterTypes = null;


}


