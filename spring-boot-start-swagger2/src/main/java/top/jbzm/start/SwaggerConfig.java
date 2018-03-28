package top.jbzm.start;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jbzm.start.swagger.SwaggerProperties;

/**
 * @author jbzm
 * @date Create on 2018/3/5 15:10
 */
@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean(SwaggerProperties.class)
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }
}
