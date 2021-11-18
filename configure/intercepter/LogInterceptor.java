package cn.chan.reqlogspringbootstarter.configure.intercepter;

import cn.chan.reqlogspringbootstarter.configure.property.ReqLogProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chan
 * @version 0.0.1
 * @time 2021/7/26 - 19:26
 */
public class LogInterceptor implements RequestBodyAdvice, AsyncHandlerInterceptor {
        private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
        private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal("StopWatch-StartTime");
        @Autowired
        ObjectMapper objectMapper;
        @Autowired
        private ReqLogProperties properties;

        public LogInterceptor() {
        }

        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            long beginTime = System.currentTimeMillis();
            this.startTimeThreadLocal.set(beginTime);
            String path = this.getPath(request);
            log.info("Start: {}", path);
            if (null != handler && handler instanceof HandlerMethod) {
                Map<String, String[]> params = request.getParameterMap();
                Map<String, String> headerMap = this.getHeaderMap(request);
                log.info("invoke {}, Params: {}, Headers: {}", path, this.objectMapper.writeValueAsString(params), this.objectMapper.writeValueAsString(headerMap));
            }

            return true;
        }

        private Map<String, String> getHeaderMap(HttpServletRequest request) {
            Map<String, String> headerMap = new HashMap();
            Enumeration headerNames = request.getHeaderNames();

            while(headerNames.hasMoreElements()) {
                String name = (String)headerNames.nextElement();
                if (!isUpperOrLowerCase(name.charAt(0))) {
                    String value = request.getHeader(name);
                    headerMap.put(name, value);
                }
            }

            return headerMap;
        }

        public static Boolean isUpperOrLowerCase(char c) {
            int cint = c - 0;
            if (cint <= 90 && cint >= 65) {
                return true;
            } else {
                return cint <= 122 && cint >= 97 ? false : null;
            }
        }

        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            try {
                long endTime = System.currentTimeMillis();
                long beginTime = this.startTimeThreadLocal.get();
                long consumeTime = endTime - beginTime;
                String path = this.getPath(request);
                log.info("End {}, executeTime: {} ms", path, consumeTime);
                if (consumeTime > (long)this.properties.getSlowReqElapsed()) {
                    log.warn("slow api ,{}, executeTime {} ms", path, consumeTime);
                }
            } finally {
                this.startTimeThreadLocal.remove();
            }

        }

        public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
            return o;
        }

        public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
            return httpInputMessage;
        }

        public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
            try {
                log.info("body Params: {}", this.objectMapper.writeValueAsString(body));
            } catch (JsonProcessingException var7) {
                log.error("body to json error", var7);
            }

            return body;
        }

        private String getPath(HttpServletRequest request) {
            String path = request.getServletPath();
            if (StringUtils.hasText(path)) {
                path = request.getRequestURI();
            }
            return path;
        }
    }

