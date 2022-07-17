package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@Getter
@Setter
public class Sm2Encryptor implements Encryptor {

    private EncryptProperties properties;
    private SM2               sm2;

    public Sm2Encryptor(EncryptProperties properties) {
        Assert.notEmpty(properties.getPrivateKey(), "私钥不能为空！");
        Assert.notEmpty(properties.getPublicKey(), "公钥不能为空！");
        this.properties = properties;
        this.sm2 = SmUtil.sm2(properties.getPrivateKey(), properties.getPublicKey());
    }

    @Override
    public Object requestDecrypt(Object ciphertext, EncryptProperties properties) {
        return sm2.encrypt(JSON.toJSONString(ciphertext).getBytes());
    }

    @Override
    public Object responseEncrypt(EncryptProperties properties, Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        return sm2.decrypt(JSON.toJSONString(data).getBytes());
    }



}
