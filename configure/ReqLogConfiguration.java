package cn.chan.reqlogspringbootstarter.configure;

import cn.chan.reqlogspringbootstarter.configure.intercepter.LogInterceptor;
import cn.chan.reqlogspringbootstarter.configure.property.ReqLogProperties;
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
