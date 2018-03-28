package top.jbzm.start.tool;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jbzm.start.web.tool.advice.AdviceExcluder;

/**
 * @author jbzm
 * @date Create on 2018/2/27 17:33
 */
@Configuration
public class WebToolConfig {
    @Bean
    @ConditionalOnMissingBean(AdviceExcluder.class)
    public AdviceExcluder adviceExcluder() {
        return new AdviceExcluder();
    }
}
