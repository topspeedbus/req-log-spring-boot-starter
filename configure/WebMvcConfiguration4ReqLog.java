package cn.chan.reqlogspringbootstarter.configure;


import cn.chan.reqlogspringbootstarter.configure.intercepter.LogInterceptor;
import cn.chan.reqlogspringbootstarter.configure.property.ReqLogProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Mvc配置
 *
 * @author chan
 * @Date 2021/11/19 2:01 下午
 */
@EnableConfigurationProperties({ReqLogProperties.class})
@Configuration
@ConditionalOnProperty(
        value = {"component.req.logs.enabled"},
        matchIfMissing = true
)
public class WebMvcConfiguration4ReqLog implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    /**
     * 增加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }
}
