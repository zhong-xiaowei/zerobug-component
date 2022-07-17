package cn.com.zerobug.component.filter.chain;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
public class InternalFilterChain implements FilterChain {

    private final FilterChain  originalChain;
    private final List<Filter> internalFilters;
    private final int          size;

    private int currentPosition = 0;

    public InternalFilterChain(FilterChain originalChain, List<Filter> internalFilters) {
        this.originalChain = originalChain;
        this.internalFilters = internalFilters;
        this.size = internalFilters.size();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (currentPosition == this.size) {
            this.originalChain.doFilter(request, response);
            return;
        }
        this.currentPosition++;
        Filter nextFilter = this.internalFilters.get(this.currentPosition - 1);
        nextFilter.doFilter(request, response, this);
    }
}
