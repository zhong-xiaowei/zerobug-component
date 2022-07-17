package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.wrapper.RepeatedlyRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RepeatedlyInternalFilter extends BaseInternalFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String ct;
        if ((ct = request.getContentType()) != null
                && ct.contains(MediaType.APPLICATION_JSON_VALUE)) {
            RepeatedlyRequestWrapper wrapper = null;
            try {
                wrapper = new RepeatedlyRequestWrapper(request);
            } catch (IOException e) {
                log.warn("包装请求失败！原因：{}", e.getMessage());
            }
            chain.doFilter(wrapper, response);
            return;
        }
        chain.doFilter(request, response);
    }
}

