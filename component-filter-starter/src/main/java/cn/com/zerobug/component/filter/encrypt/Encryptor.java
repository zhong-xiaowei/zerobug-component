package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public interface Encryptor {

    /**
     * 请求解密
     * @param data
     * @param properties
     * @return
     */
    Object requestDecrypt(Object data, EncryptProperties properties);

    /**
     * 响应加密
     *
     * @param properties
     * @param data
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param request
     * @param response
     * @return
     */
    Object responseEncrypt(EncryptProperties properties, Object data, MethodParameter methodParameter,
                           MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                           ServerHttpRequest request, ServerHttpResponse response);

}
