package cn.com.zerobug.component.ss.utils;

import cn.com.zerobug.component.ss.model.AuthenticatedUser;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author zhongxiaowei
 * @date 2022/2/25
 */
public class SecurityContextUtils {


    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static AuthenticatedUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof AuthenticatedUser ? (AuthenticatedUser) authentication.getPrincipal() : null;
    }

    /**
     * 获得当前用户id
     *
     * @return 用户id
     */
    @Nullable
    public static Long getLoginUserId() {
        AuthenticatedUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 获取当前用户名
     * @return
     */
    @Nullable
    public static String getLoginUsername() {
        AuthenticatedUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    /**
     * 获得当前用户的角色id数组
     *
     * @return 角色编号数组
     */
    @Nullable
    public static Set<Long> getLoginUserRoleIds() {
        AuthenticatedUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getRoleIds() : null;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request 请求
     */
    public static void setLoginUser(AuthenticatedUser loginUser, HttpServletRequest request) {
        Authentication authentication = buildAuthentication(loginUser, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static Authentication buildAuthentication(AuthenticatedUser loginUser, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

}
