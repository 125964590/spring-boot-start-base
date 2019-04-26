package com.ht.base.annotation.log.aop;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengyi
 * @date 11/30/18 6:15 PM
 **/
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "jbzm.annotation")
public class AnnotationProperties {
    private Boolean enable = false;

    private Log log;

    @Data
    class Log {
        private Boolean enable = true;
    }
}

