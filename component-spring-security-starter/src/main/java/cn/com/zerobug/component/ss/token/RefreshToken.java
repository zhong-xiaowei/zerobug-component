package cn.com.zerobug.component.ss.token;

import java.util.Date;

/**
 * @author zhongxiaowei
 * @date 2022/3/13
 */
public interface RefreshToken {

    /**
     * 获取所有者
     * @param <T>
     * @return
     */
    <T> T getOwner();

    /**
     * 获取token
     *
     * @return 刷新令牌Value
     */
    String getValue();

    /**
     * 获取访问令牌的值
     * @return 访问令牌Value
     */
    String getAccessToken();

    /**
     * 获取过期时间
     * @return Date
     */
    Date getExpiration();

    /**
     * 是否过期
     * @return true or false
     */
    boolean isExpired();

    /**
     * 绑定访问令牌
     * @param accessToken 访问令牌
     */
    void bindingAccessToken(String accessToken);

}
