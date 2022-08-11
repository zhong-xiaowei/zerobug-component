package cn.com.zerobug.component.ss.handler;

import cn.com.zerobug.common.base.api.ApiResult;
import cn.com.zerobug.common.exception.enums.BaseErrorCode;
import cn.com.zerobug.common.utils.JacksonUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理类
 * R
 *
 * @author zhongxiaowei
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        log.error("【AuthenticationEntryPointImpl.commence】=> 访问 URL:[{}] 因认证失败被拦截！", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setStatus(BaseErrorCode.UNAUTHORIZED.getCode());
        ServletUtil.write(response, JacksonUtil.toJsonString(ApiResult.of(BaseErrorCode.UNAUTHORIZED)), MediaType.APPLICATION_JSON_VALUE);
    }
}
