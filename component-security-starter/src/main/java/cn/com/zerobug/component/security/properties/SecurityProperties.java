package cn.com.zerobug.component.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhongxiaowei
 * @date 2022/2/26
 */
@Data
@ConfigurationProperties(value = SecurityProperties.SECURITY_PROPERTIES_PREFIX)
public class SecurityProperties {

    public static final String SECURITY_PROPERTIES_PREFIX = "zerobug.security";
    public static final String DEFUALT_TOKEN_HEADER = "Authorization";
    /**
     * 默认 2 小时
     */
    public static final Integer DEFAULT_ACCESS_TOKEN_VALIDITY = 7200;
    /**
     * 默认 7 天
     */
    public static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY = 604800;

    /**
     * TOKEN 头
     */
    private String header = DEFUALT_TOKEN_HEADER;
    /**
     * 开启mock登录
     */
    private boolean mockEnable = false;
    /**
     * mock登录的key
     */
    private String mockKey;
    /**
     * 访问token有效期
     */
    private Integer accessTokenValidity = DEFAULT_ACCESS_TOKEN_VALIDITY;
    /**
     * 刷新token的有效期
     */
    private Integer refreshTokenValidity = DEFAULT_REFRESH_TOKEN_VALIDITY;

}
