package top.jbzm.start.web.tool.advice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jbzm
 * @date Create on 2018/3/5 11:18
 */
@Data
@ConfigurationProperties(prefix = "jbzm.web.tool")
public class WebToolProperties {
    private boolean result;
    private boolean exception;
}
