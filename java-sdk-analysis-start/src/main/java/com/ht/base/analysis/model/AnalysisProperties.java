package com.ht.base.analysis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengyi
 * @date 2018-12-25 17:46
 **/
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "jbzm.analysis")
public class AnalysisProperties {

    private String saServerUrl = "https://datax-api.huatu.com/sa?project=default";
    private boolean saWriteData = true;
    private boolean enable = false;


}