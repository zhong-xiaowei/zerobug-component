package cn.com.zerobug.component.security.token.store;

import cn.com.zerobug.component.cache.CacheCommand;
import cn.com.zerobug.component.security.model.SessionManager;
import cn.hutool.core.util.StrUtil;
import cn.com.zerobug.component.security.model.AuthenticatedUser;
import cn.com.zerobug.component.security.token.AccessToken;
import cn.com.zerobug.component.security.token.RefreshToken;

import java.time.Duration;
import java.util.Date;

/**
 * @author zhongxiaowei
 * @date 2022/2/27
 */
public class RedisSecurityTokenStore implements SecurityTokenStore {

    /**
     * 访问tokenkey  access_token:tokenValue
     */
    public static final String ACCESS_TOKEN_KEY = "access_token:{}";
    /**
     * session_to_access:userid
     * 通过该key找到会话绑定的访问token
     */
    public static final String SESSION_TO_ACCESS_KEY = "session_to_access:{}";
    /**
     * 刷新令牌key
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token:{}";

    private final CacheCommand   cacheCommand;
    private final SessionManager sessionManager;

    public RedisSecurityTokenStore(CacheCommand cacheCommand, SessionManager sessionManager) {
        this.cacheCommand = cacheCommand;
        this.sessionManager = sessionManager;
    }

    @Override
    public void saveAccessToken(AccessToken accessToken, AuthenticatedUser authenticatedUser) {
        String accessTokenKey = keyFormat(ACCESS_TOKEN_KEY, accessToken.getValue());
        String authAccessTokenKey = keyFormat(SESSION_TO_ACCESS_KEY, authenticatedUser.getId());
        RefreshToken refreshToken = accessToken.getRefreshToken();
        cacheCommand.setValue(accessTokenKey, accessToken, Duration.ofSeconds(accessToken.getValiditySeconds()));
        cacheCommand.setValue(authAccessTokenKey, accessToken, Duration.ofSeconds(accessToken.getValiditySeconds()));
        if (refreshToken != null) {
            saveRefreshToken(refreshToken);
        }
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        String refreshTokenKey = keyFormat(REFRESH_TOKEN_KEY, refreshToken.getValue());
        Date expiration = refreshToken.getExpiration();
        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
        cacheCommand.setValue(refreshTokenKey, refreshToken, Duration.ofSeconds(seconds));
    }

    @Override
    public void removeAccessToken(AccessToken accessToken, Boolean clearRefreshToken, Boolean clearSession) {
        String accessTokenKey = keyFormat(ACCESS_TOKEN_KEY, accessToken.getValue());
        cacheCommand.removeKey(accessTokenKey);
        if (clearRefreshToken) {
            removeRefreshToken(accessToken.getRefreshToken());
        }
        if (clearSession) {
            sessionManager.removeSession(accessToken.getOwner());
        }
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        String refreshTokenKey = keyFormat(REFRESH_TOKEN_KEY, refreshToken.getValue());
        cacheCommand.removeKey(refreshTokenKey);
    }

    @Override
    public AccessToken getAccessToken(AuthenticatedUser authenticatedUser) {
        String key = keyFormat(SESSION_TO_ACCESS_KEY, authenticatedUser.getId());
        return cacheCommand.getValue(key);
    }

    @Override
    public AccessToken loadAccessToken(String tokenValue) {
        String key = keyFormat(ACCESS_TOKEN_KEY, tokenValue);
        return cacheCommand.getValue(key);
    }

    @Override
    public AuthenticatedUser obtainAuthenticatedUser(AccessToken accessToken) {
        return sessionManager.getSession(accessToken.getOwner());
    }

    public static String keyFormat(String template, Object... args) {
        return StrUtil.format(template, args);
    }
}
