package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
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
public class AesEncryptor implements Encryptor {

    private final EncryptProperties properties;
    private final SymmetricCrypto   symmetricCrypto;

    public AesEncryptor(EncryptProperties properties) {
        this.properties = properties;
        this.symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, HexUtil.decodeHex(this.properties.getPrivateKey()));
    }

    @Override
    public Object requestDecrypt(String ciphertext) {
        return symmetricCrypto.decryptStr(ciphertext);
    }

    @Override
    public Object responseEncrypt(Object data,
                                  MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return symmetricCrypto.encryptHex(JSON.toJSONString(data));
    }

}
