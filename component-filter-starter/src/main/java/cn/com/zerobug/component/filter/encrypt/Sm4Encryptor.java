package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public class Sm4Encryptor implements Encryptor {

    private final EncryptProperties properties;
    private       SM4               sm4;

    public Sm4Encryptor(EncryptProperties properties) {
        Assert.notEmpty(properties.getPrivateKey(), "私钥不能为空！");
        this.properties = properties;
        this.sm4 = SmUtil.sm4(HexUtil.decodeHex(properties.getPrivateKey()));
    }

    @Override
    public Object requestDecrypt(String ciphertext) {
        return sm4.decryptStr(ciphertext);
    }

    @Override
    public Object responseEncrypt(Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        return sm4.encryptHex(JSON.toJSONString(data));
    }

}
