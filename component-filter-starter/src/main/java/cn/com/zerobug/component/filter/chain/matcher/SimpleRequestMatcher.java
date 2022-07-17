package cn.com.zerobug.component.filter.chain.matcher;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@Data
public class SimpleRequestMatcher implements RequestMatcher {

    private String[] patterns;

    public SimpleRequestMatcher(String[] patterns) {
        this.patterns = patterns;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        for (String pattern : patterns) {
            if (requestUri.equals(pattern)) {
                return true;
            }
        }
        return false;
    }
}
