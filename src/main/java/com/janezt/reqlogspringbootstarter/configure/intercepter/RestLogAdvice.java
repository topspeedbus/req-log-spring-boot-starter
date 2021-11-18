package com.janezt.reqlogspringbootstarter.configure.intercepter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author chan
 * @version 0.0.1
 * @time 2021/7/26 - 17:08
 * @descripe: 入参， 返回结果处理
 */
@RestControllerAdvice
public class RestLogAdvice extends RequestBodyAdviceAdapter implements ResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(RestLogAdvice.class);
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal("StopWatch-StartTime");
    @Autowired
    ObjectMapper objectMapper;
    /**
     * 128位的AESkey
     */
    private static final byte[] AES_KEY = "1111111111111111".getBytes(StandardCharsets.US_ASCII);

    public RestLogAdvice() {
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }


    /**
     * 预处理请求体 ： 解密  ps: 只能处理post提交得请求
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        // 读取加密的请求体
//        byte[] body = new byte[inputMessage.getBody().available()];
//        inputMessage.getBody().read(body);
//
//        try {
//            // 使用AES解密
//            body = this.decrypt(Base64.getDecoder().decode(body), AES_KEY);
//        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
//                | BadPaddingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//        // 使用解密后的数据，构造新的读取流
//        InputStream rawInputStream = new ByteArrayInputStream(body);
//        return new HttpInputMessage() {
//            @Override
//            public HttpHeaders getHeaders() {
//                return inputMessage.getHeaders();
//            }
//
//            @Override
//            public InputStream getBody() throws IOException {
//                return rawInputStream;
//            }
//        };
        return inputMessage;
    }

    /**
     * AES解密
     *
     * @param data
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    private Cipher getCipher(byte[] key, int model)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(model, secretKeySpec);
        return cipher;
    }


    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            HttpHeaders headers = httpInputMessage.getHeaders();

            log.info(" Headers: {}", headers);

            log.info("body Params: {}", this.objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException var7) {
            log.error("body to json error", var7);
        }

        return body;
    }

    private String getPath(HttpServletRequest request) {
        String path = request.getServletPath();
        if (!StringUtils.hasLength(path)) {
            path = request.getRequestURI();
        }

        return path;
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }


    /**
     * 处理返回结果
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Method method=methodParameter.getMethod();

        Class<?> declaringClass = method.getDeclaringClass();
        Annotation[] annotations = declaringClass.getAnnotations();

        String url=serverHttpRequest.getURI().toASCIIString();
        log.info("{}.{},url={},result={}",method.getDeclaringClass().getSimpleName(),method.getName(),url,o);
        return o;
    }
}
