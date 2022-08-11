package cn.com.zerobug.component.ss.token;

import cn.com.zerobug.component.ss.properties.SecurityProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/3/13
 */
@Setter
public class SimpleAccessToken implements AccessToken {
    private static final long serialVersionUID = 6483440576439893850L;

    private Object owner;
    private String value;
    private Date expiration;
    private RefreshToken refreshToken;
    private Map<String, Object> extra = Collections.emptyMap();

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    @JsonIgnore
    public int getValiditySeconds() {
        return expiration != null ? Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                .intValue() : 0;
    }

    @Override
    @JsonIgnore
    public boolean isExpired() {
        return expiration != null && expiration.before(new Date());
    }

    @Override
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Override
    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public AccessToken renewal(SecurityProperties properties) {
        this.setExpiration(new Date(Long.valueOf(System.currentTimeMillis() + properties.getAccessTokenValidity() * 1000)));
        return this;
    }

    @Override
    public <T> T getOwner() {
        return (T) owner;
    }

    @Override
    public String getValue() {
        return value;
    }
}
