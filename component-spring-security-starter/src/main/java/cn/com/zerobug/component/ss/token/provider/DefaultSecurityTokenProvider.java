package cn.com.zerobug.component.ss.token.provider;

import cn.com.zerobug.component.ss.exception.InvalidTokenException;
import cn.com.zerobug.component.ss.model.AuthenticatedUser;
import cn.com.zerobug.component.ss.model.SessionManager;
import cn.com.zerobug.component.ss.properties.SecurityProperties;
import cn.com.zerobug.component.ss.token.*;
import cn.com.zerobug.component.ss.token.store.SecurityTokenStore;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author zhongxiaowei
 * @date 2022/3/7
 */
@RequiredArgsConstructor
public class DefaultSecurityTokenProvider implements SecurityTokenProvider {

    public static final float RENEWAL_THRESHOLD = 0.3f;

    private final SecurityTokenStore    tokenStore;
    private final SecurityProperties    properties;
    private final SessionManager        sessionManager;
    private       ExtraPropertyRegistry extraPropertyRegistry;

    @Override
    public AccessToken createAccessToken(AuthenticatedUser authenticatedUser) {
        AccessToken  accessToken  = tokenStore.getAccessToken(authenticatedUser);
        RefreshToken refreshToken = null;
        if (accessToken != null) {
            if (accessToken.isExpired()) {
                //删除这个访问token
                refreshToken = accessToken.getRefreshToken();
                tokenStore.removeAccessToken(accessToken, false, true);
                accessToken = null;
            }
        }
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authenticatedUser);
        } else if (refreshToken.isExpired()) {
            refreshToken = createRefreshToken(authenticatedUser);
        }
        if (accessToken == null) {
            accessToken = buildAccessToken(authenticatedUser, refreshToken);
        }
        refreshToken.bindingAccessToken(accessToken.getValue());
        final Integer sessionExpire = accessToken.getValiditySeconds();
        sessionManager.saveSession(authenticatedUser, () -> sessionExpire);
        tokenStore.saveAccessToken(accessToken, authenticatedUser);
        return accessToken;
    }

    @Override
    public RefreshToken createRefreshToken(AuthenticatedUser authenticatedUser) {
        SimpleRefreshToken simpleRefreshToken = new SimpleRefreshToken();
        simpleRefreshToken.setValue(IdUtil.fastSimpleUUID());
        simpleRefreshToken.setExpiration(new Date(System.currentTimeMillis() + (properties.getRefreshTokenValidity() * 1000L)));
        simpleRefreshToken.setOwner(authenticatedUser.getId());
        return simpleRefreshToken;
    }

    @Override
    public AuthenticatedUser verifyToken(String tokenValue) {
        AccessToken accessToken = tokenStore.loadAccessToken(tokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("TOKEN 是无效的!");
        }
        if (accessToken.isExpired()) {
            throw new InvalidTokenException("TOKEN 已过期!");
        }
        AuthenticatedUser authenticatedUser = tokenStore.obtainAuthenticatedUser(accessToken);
        if (authenticatedUser == null) {
            throw new InvalidTokenException("TOKEN 是无效的！");
        }
        // 为了防止频繁刷新token 如果访问token过期时间少于 1/3 那么进行自动续期
        int validitySeconds = accessToken.getValiditySeconds();
        if ((validitySeconds / properties.getAccessTokenValidity()) <= RENEWAL_THRESHOLD) {
            accessToken = accessToken.renewal(properties);
            tokenStore.saveAccessToken(accessToken, authenticatedUser);
        }
        return authenticatedUser;
    }

    private AccessToken buildAccessToken(AuthenticatedUser authenticatedUser, RefreshToken refreshToken) {
        String            accessTokenValue  = IdUtil.fastSimpleUUID();
        SimpleAccessToken simpleAccessToken = new SimpleAccessToken();
        simpleAccessToken.setValue(accessTokenValue);
        simpleAccessToken.setRefreshToken(refreshToken);
        simpleAccessToken.setOwner(authenticatedUser.getId());
        simpleAccessToken.setExpiration(new Date(System.currentTimeMillis() + (properties.getAccessTokenValidity() * 1000)));
        if (extraPropertyRegistry != null) {
            simpleAccessToken.setExtra(extraPropertyRegistry.register(authenticatedUser, simpleAccessToken));
        }
        return simpleAccessToken;
    }

}
