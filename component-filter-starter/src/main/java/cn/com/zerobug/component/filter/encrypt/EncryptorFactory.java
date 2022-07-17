package cn.com.zerobug.component.filter.encrypt;

import cn.com.zerobug.component.filter.properties.EncryptProperties;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public class EncryptorFactory {

    public static Encryptor generate(EncryptProperties properties) {
        Encryptor encryptor = null;
        switch (properties.getCryptoType()) {
            case AES:
                encryptor = new AesEncryptor(properties);
                break;
            case SM4:
                encryptor = new Sm4Encryptor(properties);
                break;
            case RSA:
                encryptor = new RsaEncryptor(properties);
                break;
            case SM2:
                encryptor = new Sm2Encryptor(properties);
                break;
            default:
        }
        return encryptor;
    }

}
