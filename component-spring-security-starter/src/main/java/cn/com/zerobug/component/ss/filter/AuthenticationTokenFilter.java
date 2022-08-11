package cn.com.zerobug.component.ss.filter;

import cn.com.zerobug.common.exception.GeneralException;
import cn.com.zerobug.component.ss.model.AuthenticatedUser;
import cn.com.zerobug.component.ss.properties.SecurityProperties;
import cn.com.zerobug.component.ss.token.provider.SecurityTokenProvider;
import cn.com.zerobug.component.ss.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author zhongxiaowei
 */
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;
    private final SecurityTokenProvider securityTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tokenValue = securityTokenProvider.extractTokenValue(request, securityProperties.getHeader());
        if (StringUtils.hasText(tokenValue)) {
            AuthenticatedUser authenticatedUser;
            try {
                authenticatedUser = securityTokenProvider.verifyToken(tokenValue);
            } catch (GeneralException e) {
                authenticatedUser = loadMockUser(tokenValue);
                if (authenticatedUser == null) {
                    throw new ServletException(e);
                }
            }
            SecurityContextUtils.setLoginUser(authenticatedUser, request);
        }
        chain.doFilter(request, response);
    }

    private final AuthenticatedUser loadMockUser(String tokenValue) {
        //TODO
        return null;
    }

    private final boolean haveMockKey(String tokenValue) {
        return tokenValue.startsWith(securityProperties.getMockKey());
    }
}
