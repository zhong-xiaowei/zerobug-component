package cn.com.zerobug.component.security.token;

import cn.com.zerobug.component.security.model.AuthenticatedUser;

import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/3/18
 */
@FunctionalInterface
public interface ExtraPropertyRegistry {

    /**
     * 注册额外属性
     * @param authenticatedUser
     * @param accessToken
     * @return
     */
    Map<String, Object> register(AuthenticatedUser authenticatedUser, AccessToken accessToken);

}
