package cn.com.zerobug.component.filter.chain.matcher;

import lombok.Data;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@Data
public class AntRequestMatcher implements RequestMatcher {

    public static final  String         MATCH_ALL        = "/**";
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private String[] patterns;

    public AntRequestMatcher(String[] patterns) {
        this.patterns = patterns;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        for (String pattern : patterns) {
            if (ANT_PATH_MATCHER.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
