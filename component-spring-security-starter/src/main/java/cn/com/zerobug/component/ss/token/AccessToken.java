package cn.com.zerobug.component.ss.token;

import cn.com.zerobug.component.ss.properties.SecurityProperties;

import java.util.Date;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/3/13
 */
public interface AccessToken {

    /**
     * 获取所有者
     *
     * @param <T>
     * @return
     */
    <T> T getOwner();

    /**
     * 获取token
     *
     * @return
     */
    String getValue();

    /**
     * 获取过期时间
     *
     * @return
     */
    Date getExpiration();

    /**
     * 获取过期的秒数
     *
     * @return
     */
    int getValiditySeconds();

    /**
     * 是否过期
     *
     * @return
     */
    boolean isExpired();

    /**
     * 获取刷新token
     *
     * @return
     */
    RefreshToken getRefreshToken();

    /**
     * 获取 附加信息
     *
     * @return
     */
    Map<String, Object> getExtra();

    /**
     * 续期
     *
     * @param properties
     * @return
     */
    AccessToken renewal(SecurityProperties properties);

}
