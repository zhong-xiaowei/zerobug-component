package cn.com.zerobug.component.security.token.store;

import cn.com.zerobug.component.security.model.AuthenticatedUser;
import cn.com.zerobug.component.security.token.AccessToken;
import cn.com.zerobug.component.security.token.RefreshToken;

/**
 * @author zhongxiaowei
 * @date 2022/2/27
 */
public interface SecurityTokenStore {

    /**
     * 保存访问Token
     *
     * @param accessToken
     * @param authenticatedUser
     * @return AccessToken
     */
    void saveAccessToken(AccessToken accessToken, AuthenticatedUser authenticatedUser);

    /**
     * 保存刷新token
     *
     * @param refreshToken
     * @return RefreshToken
     */
    void saveRefreshToken(RefreshToken refreshToken);

    /**
     * 删除访问Token
     *
     * @param accessToken
     * @param clearRefreshToken
     * @param clearSession
     * @return
     */
    void removeAccessToken(AccessToken accessToken, Boolean clearRefreshToken, Boolean clearSession);

    /**
     * 删除刷新token
     *
     * @param refreshToken
     * @return
     */
    void removeRefreshToken(RefreshToken refreshToken);

    /**
     * 获取访问token
     *
     * @param authenticatedUser
     * @return
     */
    AccessToken getAccessToken(AuthenticatedUser authenticatedUser);

    /**
     * 加载 accessToken
     *
     * @param tokenValue
     * @return
     */
    AccessToken loadAccessToken(String tokenValue);

    /**
     * 获得认证用户
     *
     * @param accessToken
     * @return
     */
    AuthenticatedUser obtainAuthenticatedUser(AccessToken accessToken);
}
