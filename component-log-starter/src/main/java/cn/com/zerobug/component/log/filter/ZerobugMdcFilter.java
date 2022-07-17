package cn.com.zerobug.component.log.filter;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import cn.com.zerobug.common.context.GlobalContextHolder;
import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhongxiaowei
 * @date 2022/4/3
 */
public class ZerobugMdcFilter extends MDCInsertingServletFilter {

    public static final String HEADER_FOR_TRACE_ID = "X-TRACE-ID";

    public static final String MDC_TRACE_ID = "trace";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            return;
        }
        String traceId = ((HttpServletRequest) request).getHeader(HEADER_FOR_TRACE_ID);
        if (!StringUtils.hasLength(traceId)) {
            // 如果前端没有传递，那么后端自动生成
            traceId = IdUtil.fastSimpleUUID();
        }
        GlobalContextHolder.setTraceId(traceId);
        MDC.put(MDC_TRACE_ID, traceId);
        try {
            super.doFilter(request, response, chain);
        } finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }
}
