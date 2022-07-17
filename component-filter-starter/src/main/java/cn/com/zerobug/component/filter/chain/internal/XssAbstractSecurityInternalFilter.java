package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.SecurityHelper;
import cn.com.zerobug.component.filter.chain.wrapper.XssEscapeRequestWrapper;
import cn.com.zerobug.component.filter.properties.XssSecurityProperties;
import cn.com.zerobug.component.filter.enums.XssPattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhongxiaowei
 * @date 2021/12/31
 */
@RequiredArgsConstructor
@Slf4j
@Order(200)
public class XssAbstractSecurityInternalFilter extends AbstractSecurityInternalFilter {

    private final XssSecurityProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (generalPrepare(request, properties)) {
            try {
                SecurityHelper.attemptFindXssCode(request);
            } catch (Exception e) {
                if (properties.getPattern().equals(XssPattern.ESCAPE.name())) {
                    XssEscapeRequestWrapper requestWrapper = new XssEscapeRequestWrapper(request);
                    chain.doFilter(requestWrapper, response);
                    return;
                }
                rejectHandler.afterReject(request, response, e);
                return;
            }
        }
        chain.doFilter(request, response);
    }

}
