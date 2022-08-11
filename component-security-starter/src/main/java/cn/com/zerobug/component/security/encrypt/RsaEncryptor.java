package cn.com.zerobug.component.security.encrypt;

import cn.com.zerobug.common.utils.JacksonUtil;
import cn.com.zerobug.component.security.properties.EncryptProperties;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public class RsaEncryptor implements Encryptor {

    private final EncryptProperties properties;
    private final RSA               rsa;

    public RsaEncryptor(EncryptProperties properties) {
        this.properties = properties;
        this.rsa = new RSA(properties.getPrivateKey(), properties.getPrivateKey());
    }

    @Override
    public String requestDecrypt(String ciphertext) {
        return rsa.decryptStr(ciphertext, KeyType.PrivateKey);
    }

    @Override
    public Object responseEncrypt(Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        return rsa.encryptHex(JacksonUtil.toJsonString(data), KeyType.PrivateKey);
    }

}
