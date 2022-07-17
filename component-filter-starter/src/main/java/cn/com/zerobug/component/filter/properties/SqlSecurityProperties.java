package cn.com.zerobug.component.filter.properties;

import cn.com.zerobug.component.filter.annotation.FilterIgnore;
import cn.com.zerobug.component.filter.annotation.IgnoreType;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReUtil;
import cn.com.zerobug.component.filter.constant.PropertiesConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: zhongxiaowei
 * @email: <a>zhongxiaowei.nice@gmail.com</a>
 * @date: 2021/7/8
 * @version: 1.0
 */
@Slf4j
@ConfigurationProperties(prefix = PropertiesConstant.FILTER_SQL_PROFIX)
public class SqlSecurityProperties extends AbstractSecurityProperties {

    /**
     * 是否开启
     */
    private boolean enable = true;
    /**
     * 过滤包含的路径
     */
    private String includePaths = DEFAULT_INCLUDE_PATH;
    /**
     * 需要排除的路径
     */
    private Set<String> ignorePaths;

    @Override
    public void afterPropertiesSet() {
        if (ignorePaths == null) {
            ignorePaths = new HashSet<>();
        }
        pathProcessing();
        this.resolveUrlFromAnnotationMarkClass();
        log.info("SQL注入攻击拦截 排除路径 【{}】", ignorePaths);
    }

    private void pathProcessing() {
        if (!DEFAULT_INCLUDE_PATH.equals(includePaths)) {
            String[] paths = this.includePaths.split(",");
            for (int i = 0; i < paths.length; i++) {
                paths[i] = joinContextPath(paths[i]);
            }
        }
        ignorePaths = ignorePaths.stream().map(m -> joinContextPath(m)).collect(Collectors.toSet());
    }

    @Override
    protected void resolveUrlFromAnnotationMarkClass() {
        String contextPath = getContextPath();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = getHandlerMethods();
        Set<String> ignoreUrl = new HashSet<>();
        handlerMethods.keySet().forEach(k -> {
            HandlerMethod handlerMethod = handlerMethods.get(k);
            FilterIgnore  filterIgnore  = AnnotationUtil.getAnnotation(handlerMethod.getMethod(), FilterIgnore.class);
            Optional.ofNullable(filterIgnore)
                    .ifPresent(item -> {
                        IgnoreType type = item.type();
                        if (type.equals(IgnoreType.SQL) || type.equals(IgnoreType.ALL)) {
                            k.getPatternsCondition()
                                    .getPatterns()
                                    .forEach(url -> ignoreUrl.add(contextPath + ReUtil.replaceAll(url, PATTERN, ANY)));
                        }
                    });
        });
        this.ignorePaths.addAll(ignoreUrl);
    }

    @Override
    public boolean getEnable() {
        return this.enable;
    }

    @Override
    public String getIncludePaths() {
        return this.includePaths;
    }

    @Override
    public Set<String> getIgnorePaths() {
        return this.ignorePaths;
    }

    public void setEnable(final boolean enable) {
        this.enable = enable;
    }

    public void setIncludePaths(String includePaths) {
        this.includePaths = includePaths;
    }

    public void setIgnorePaths(Set<String> ignorePaths) {
        this.ignorePaths = ignorePaths;
    }
}
