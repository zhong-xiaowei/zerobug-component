package cn.com.zerobug.component.ss.handler;

import cn.com.zerobug.common.base.api.ApiResult;
import cn.com.zerobug.common.exception.enums.BaseErrorCode;
import cn.com.zerobug.common.utils.JacksonUtil;
import cn.com.zerobug.component.ss.utils.SecurityContextUtils;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问一个uri资源地址时，因没有访问权限被拦截，就会进入该类
 *
 * @author zhongxiaowei
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        log.warn("【AccessDeniedHandlerImpl.handle】=> 访问 URL({}) 时，用户({}) 权限不够", request.getRequestURI(),
                SecurityContextUtils.getLoginUserId(), e);
        response.setCharacterEncoding("UTF-8");
        ServletUtil.write(response, JacksonUtil.toJsonString(ApiResult.of(BaseErrorCode.FORBIDDEN)), MediaType.APPLICATION_JSON_VALUE);
    }

}
