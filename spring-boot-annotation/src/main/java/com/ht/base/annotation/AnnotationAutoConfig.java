package com.ht.base.annotation;

import com.ht.base.annotation.log.aop.AnnotationProperties;
import com.ht.base.annotation.log.aop.ImageAop;
import com.ht.base.annotation.log.util.LogAopUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengyi
 * @date 11/30/18 6:14 PM
 **/
@Configuration
@EnableConfigurationProperties(AnnotationProperties.class)
@ConditionalOnProperty(value = "jbzm.annotation.enable", havingValue = "true", matchIfMissing = true)
public class AnnotationAutoConfig {

    @Bean
    public ImageAop imageAop(LogAopUtils logAopUtils) {
        return new ImageAop(logAopUtils);
    }

    @Bean
    public LogAopUtils logAopUtils() {
        return new LogAopUtils();
    }
}