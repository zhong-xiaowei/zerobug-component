package cn.com.zerobug.component.filter.chain;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
public class ZerobugFilterChainManager extends GenericFilterBean {

    private List<Filter> preFilters;
    private List<Filter> postFilters;

    public ZerobugFilterChainManager(List<Filter> preFilters) {
        this.preFilters = preFilters;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        executeInternalFilter(request, response, chain);
    }

    private void executeInternalFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (preFilters == null || preFilters.size() == 0) {
            doFilter(request, response, chain);
            // TODO: 执行postFilter 后续完善
            return;
        }
        //在这个位置是做请求的拦截
        InternalFilterChain internalFilterChain = new InternalFilterChain(chain, preFilters);
        internalFilterChain.doFilter(request, response);
    }

}
