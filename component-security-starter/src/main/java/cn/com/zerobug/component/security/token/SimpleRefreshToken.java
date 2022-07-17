package cn.com.zerobug.component.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhongxiaowei
 * @date 2022/3/13
 */
@Setter
public class SimpleRefreshToken implements RefreshToken {

    private Object owner;
    private String value;
    private String accessToken;
    private Date expiration;

    @Override
    public <T> T getOwner() {
        return (T) owner;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    @JsonIgnore
    public boolean isExpired() {
        return expiration != null && expiration.before(new Date());
    }

    @Override
    public void bindingAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
