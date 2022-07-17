package cn.com.zerobug.component.filter.properties;

import cn.com.zerobug.component.filter.constant.PropertiesConstant;
import cn.com.zerobug.component.filter.enums.CryptoType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@ConfigurationProperties(prefix = PropertiesConstant.FILTER_ENCRYPT_PROFIX)
public class EncryptProperties {

    /**
     * 是否开启
     */
    private boolean enable = false;

    /**
     * 加密类型
     * {@link CryptoType}
     */
    private CryptoType cryptoType;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public CryptoType getCryptoType() {
        return cryptoType;
    }

    public void setCryptoType(CryptoType cryptoType) {
        this.cryptoType = cryptoType;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
