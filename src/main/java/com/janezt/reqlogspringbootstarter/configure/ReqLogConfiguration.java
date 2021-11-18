package com.janezt.reqlogspringbootstarter.configure;

import com.janezt.reqlogspringbootstarter.configure.intercepter.LogInterceptor;
import com.janezt.reqlogspringbootstarter.configure.property.ReqLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chan
 * @version 0.0.1
 * @time 2021/7/26 - 19:51
 */
@EnableConfigurationProperties({ReqLogProperties.class})
@Configuration
@ConditionalOnProperty(
        value = {"component.req.logs.enabled"},
        matchIfMissing = true
)
public class ReqLogConfiguration {
    public ReqLogConfiguration() {
    }

    @Bean
    public LogInterceptor sysLogInterceptor() {
        return new LogInterceptor();
    }
}
