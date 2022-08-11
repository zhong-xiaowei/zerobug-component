package cn.com.zerobug.component.security.encrypt;

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
     * @return
     */
    String requestDecrypt(String data);

    /**
     * 响应加密
     *
     * @param data
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param request
     * @param response
     * @return
     */
    Object responseEncrypt(Object data, MethodParameter methodParameter,
                           MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                           ServerHttpRequest request, ServerHttpResponse response);

}
