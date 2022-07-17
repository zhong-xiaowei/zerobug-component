package cn.com.zerobug.component.filter.properties;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhongxiaowei
 * @date 2021/12/29
 */
public abstract class AbstractProperties implements InitializingBean, ApplicationContextAware {

    public static final String DEFAULT_INCLUDE_PATH = "/**";
    public static final String ANY = "*";
    private static final String CONTEXT_PATH = "server.servlet.context-path";
    protected static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");


    protected ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected Map<RequestMappingInfo, HandlerMethod> getHandlerMethods() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        return handlerMapping.getHandlerMethods();
    }

    /**
     * 获取url 根据注解标记类
     */
    protected abstract void resolveUrlFromAnnotationMarkClass();

    protected String joinContextPath(String originalPath) {
        return this.getContextPath() + originalPath;
    }

    protected String getContextPath() {
        String property = applicationContext.getEnvironment().getProperty(CONTEXT_PATH);
        if (StrUtil.isNotEmpty(property)) {
            return property;
        }
        return StrUtil.EMPTY;
    }

}
