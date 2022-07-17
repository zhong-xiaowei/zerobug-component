package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.com.zerobug.component.filter.annotation.Encrypt;
import cn.com.zerobug.component.filter.annotation.EncryptStrategy;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    public static final String HEADER_FOR_CIPHERTEXT = "ciphertext";
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
        Method method = methodParameter.getMethod();
        Encrypt annotation = method.getAnnotation(Encrypt.class);
        if (checkIfEnable(annotation)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) response;
        HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
        servletResponse.setHeader(HEADER_FOR_CIPHERTEXT, Boolean.TRUE.toString());
        return encryptor.responseEncrypt(properties, data, methodParameter, mediaType, aClass, request, response);
    }

    /**
     * 检查是否启用
     *
     * @param encrypt 注解
     * @return 如果配置启用，注解启用，并且不是 REQUEST_DECRYPT 策略，那么就做加密返回
     */
    private boolean checkIfEnable(Encrypt encrypt) {
        if (encrypt == null) {
            return false;
        }
        boolean enable = properties.getEnable();
        boolean open = encrypt.open();
        EncryptStrategy strategy = encrypt.strategy();
        return enable && open && !EncryptStrategy.REQUEST_DECRYPT.equals(strategy);
    }

}
