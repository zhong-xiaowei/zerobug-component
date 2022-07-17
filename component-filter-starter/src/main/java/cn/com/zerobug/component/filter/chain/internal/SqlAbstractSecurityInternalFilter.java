package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.SecurityHelper;
import cn.com.zerobug.component.filter.properties.SqlSecurityProperties;
import lombok.RequiredArgsConstructor;
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
@Order(100)
public class SqlAbstractSecurityInternalFilter extends AbstractSecurityInternalFilter {

    private final SqlSecurityProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (generalPrepare(request, properties)) {
            try {
                SecurityHelper.attemptFindSqlCode(request);
            } catch (Exception e) {
                rejectHandler.afterReject(request, response, e);
                return;
            }
        }
        chain.doFilter(request, response);
    }


}
