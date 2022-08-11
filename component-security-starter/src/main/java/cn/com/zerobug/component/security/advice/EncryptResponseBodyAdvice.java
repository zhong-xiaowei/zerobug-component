package cn.com.zerobug.component.security.advice;

import cn.com.zerobug.component.security.annotation.Encrypt;
import cn.com.zerobug.component.security.encrypt.Encryptor;
import cn.com.zerobug.component.security.encrypt.EncryptorFactory;
import cn.com.zerobug.component.security.properties.EncryptProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static cn.com.zerobug.component.security.annotation.EncryptStrategy.REQUEST_DECRYPT;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@RestControllerAdvice
public class EncryptResponseBodyAdvice extends CachingBodyAdvice implements ResponseBodyAdvice<Object> {

    public static final String HEADER_FOR_ENCRYPTED = "Encrypted";

    private final EncryptProperties properties;
    private Encryptor encryptor;

    public EncryptResponseBodyAdvice(EncryptProperties properties) {
        this.properties = properties;
        if (properties.getEnable()){
            this.encryptor = EncryptorFactory.generate(properties);
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Encrypt annotation = getAnnotationFromCache(methodParameter, Encrypt.class);
        if (Objects.nonNull(annotation)) {
            return annotation.open() && annotation.strategy() != REQUEST_DECRYPT;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) response;
        HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
        servletResponse.setHeader(HEADER_FOR_ENCRYPTED, Boolean.TRUE.toString());
        return encryptor.responseEncrypt(data, methodParameter, mediaType, aClass, request, response);
    }

}
