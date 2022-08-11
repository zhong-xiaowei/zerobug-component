package cn.com.zerobug.component.security.advice;

import cn.com.zerobug.component.security.annotation.Encrypt;
import cn.com.zerobug.component.security.encrypt.Encryptor;
import cn.com.zerobug.component.security.encrypt.EncryptorFactory;
import cn.com.zerobug.component.security.properties.EncryptProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static cn.com.zerobug.component.security.annotation.EncryptStrategy.RESPONSE_ENCRYPT;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@RestControllerAdvice
public class DecryptRequestBodyAdvice extends CachingBodyAdvice implements RequestBodyAdvice {

    private final EncryptProperties properties;
    private Encryptor encryptor;

    public DecryptRequestBodyAdvice(EncryptProperties properties) {
        this.properties = properties;
        if (properties.getEnable()) {
            this.encryptor = EncryptorFactory.generate(properties);
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Encrypt annotation = getAnnotationFromCache(methodParameter, Encrypt.class);
        if (Objects.nonNull(annotation)) {
            return annotation.open() && annotation.strategy() != RESPONSE_ENCRYPT;
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            String ciphertext = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            String decryptedContent = encryptor.requestDecrypt(ciphertext);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedContent.getBytes(StandardCharsets.UTF_8));
            return new MappingJacksonInputMessage(inputStream, inputMessage.getHeaders());
        } catch (Exception e) {
            throw new RuntimeException("RestEncryptHandler request error.", e);
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}