package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.matcher.AntRequestMatcher;
import cn.hutool.core.util.StrUtil;
import cn.com.zerobug.component.filter.properties.AbstractSecurityProperties;
import cn.com.zerobug.component.filter.chain.matcher.SimpleRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author zhongxiaowei
 * @date 2021/12/31
 */
public abstract class AbstractSecurityInternalFilter extends BaseInternalFilter {

    /**
     * 通用准备
     *
     * @param request
     * @param properties
     * @return
     */
    protected boolean generalPrepare(HttpServletRequest request, AbstractSecurityProperties properties) {
        boolean allowed;
        if (allowed = properties.getEnable()) {
            Set<String> ignorePaths  = properties.getIgnorePaths();
            String      includePaths = properties.getIncludePaths();
            if (!ignorePaths.isEmpty()) {
                String[] patterns = ignorePaths.toArray(new String[0]);
                if (simpleMatch(request, patterns) || antMatch(request, patterns)) {
                    allowed = false;
                }
            }
            if (!AntRequestMatcher.MATCH_ALL.equals(includePaths)) {
                String[] patterns = includePaths.split(StrUtil.COMMA);
                if (simpleMatch(request, patterns) || antMatch(request, patterns)) {
                    return true;
                }
            }
        }
        return allowed;
    }

    protected boolean simpleMatch(HttpServletRequest request, String[] patterns) {
        SimpleRequestMatcher matcher = new SimpleRequestMatcher(patterns);
        return matcher.matches(request);
    }

    protected boolean antMatch(HttpServletRequest request, String[] patterns) {
        AntRequestMatcher matcher = new AntRequestMatcher(patterns);
        return matcher.matches(request);
    }

}
