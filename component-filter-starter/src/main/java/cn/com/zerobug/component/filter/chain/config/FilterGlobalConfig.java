package cn.com.zerobug.component.filter.chain.config;

import cn.com.zerobug.component.filter.chain.RequestRejectHandler;
import cn.com.zerobug.component.filter.chain.internal.*;
import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.com.zerobug.component.filter.properties.SqlSecurityProperties;
import cn.com.zerobug.component.filter.properties.XssSecurityProperties;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@Slf4j
public class FilterGlobalConfig {

    private StringRedisTemplate        stringRedisTemplate;
    private HandlerMappingIntrospector handlerMappingIntrospector;

    private RequestRejectHandler xssRequestRejectHandler;
    private RequestRejectHandler sqlRequestRejectHandler;
    private List<Filter>         filters;

    public FilterGlobalConfig() {
        this.filters = new ArrayList<>();
    }

    public static FilterGlobalConfig builder() {
        return new FilterGlobalConfig();
    }

    public FilterGlobalConfig redisTemplate(StringRedisTemplate template) {
        this.stringRedisTemplate = template;
        return this;
    }

    public FilterGlobalConfig handlerMappingIntrospector(HandlerMappingIntrospector handlerMappingIntrospector) {
        this.handlerMappingIntrospector = handlerMappingIntrospector;
        return this;
    }

    public FilterGlobalConfig xssRejectHandler(RequestRejectHandler rejectHandler) {
        this.xssRequestRejectHandler = rejectHandler;
        return this;
    }

    public FilterGlobalConfig sqlRejectHandler(RequestRejectHandler rejectHandler) {
        this.sqlRequestRejectHandler = rejectHandler;
        return this;
    }

    public FilterGlobalConfig build() {
        SqlAbstractSecurityInternalFilter sql = new SqlAbstractSecurityInternalFilter(SpringUtil.getBean(SqlSecurityProperties.class));
        sql.setRejectHandler(sqlRequestRejectHandler);
        XssAbstractSecurityInternalFilter xss = new XssAbstractSecurityInternalFilter(SpringUtil.getBean(XssSecurityProperties.class));
        xss.setRejectHandler(xssRequestRejectHandler);
        FilterGlobalConfig filterGlobalConfig = this.addFilter(new RepeatedlyInternalFilter())
                .addFilter(sql)
                .addFilter(new RepeatSubmitInternalFilter(stringRedisTemplate, handlerMappingIntrospector))
                .addFilter(xss)
                .addFilter(new DecryptInternalFilter(SpringUtil.getBean(EncryptProperties.class), handlerMappingIntrospector));
        filterGlobalConfig.filterSort();
        return filterGlobalConfig;
    }

    public FilterGlobalConfig addFilter(Filter filter) {
        log.info("添加过滤器到内部过滤链：【{}】", filter.getClass().getSimpleName());
        filters.add(filter);
        return this;
    }

    public void filterSort() {
        List<Filter> filters = this.filters;
        AnnotationAwareOrderComparator.sort(filters);
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
