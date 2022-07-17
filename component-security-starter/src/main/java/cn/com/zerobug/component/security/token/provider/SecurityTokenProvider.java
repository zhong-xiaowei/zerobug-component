package cn.com.zerobug.component.security.token.provider;

import cn.com.zerobug.component.security.model.AuthenticatedUser;
import cn.com.zerobug.component.security.token.AccessToken;
import cn.com.zerobug.component.security.token.RefreshToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author zhongxiaowei
 * @date 2022/2/25
 */
public interface SecurityTokenProvider {

    String BEARER_TYPE = "Bearer";

    /**
     * 提取Token的值
     *
     * @param request
     * @param header
     * @return
     */
    default String extractTokenValue(HttpServletRequest request, String header) {
        Enumeration headers = request.getHeaders(header);
        String value;
        do {
            //一般只有一个
            if (!headers.hasMoreElements()) {
                return null;
            }
            value = (String) headers.nextElement();
        } while (!value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()));
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;

    }

    /**
     * 创建访问Token
     *
     * @param authenticatedUser {@link AuthenticatedUser}
     * @return AccessToken {@link AccessToken}
     */
    AccessToken createAccessToken(AuthenticatedUser authenticatedUser);

    /**
     * 创建刷新Token
     *
     * @param authenticatedUser {@link AuthenticatedUser}
     * @return RefreshToken {@link RefreshToken}
     */
    RefreshToken createRefreshToken(AuthenticatedUser authenticatedUser);

    /**
     * 验证Token
     *
     * @param tokenValue
     * @return
     */
    AuthenticatedUser verifyToken(String tokenValue);

}
