package cn.com.zerobug.component.filter.config;

import cn.com.zerobug.component.filter.chain.NuclearFilterChainManager;
import cn.com.zerobug.component.filter.chain.config.FilterGlobalConfig;
import cn.com.zerobug.component.filter.properties.EncryptProperties;
import cn.com.zerobug.component.filter.properties.SqlSecurityProperties;
import cn.com.zerobug.component.filter.properties.XssSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * @author zhongxiaowei
 * @date 2021/12/29
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {
        SqlSecurityProperties.class,
        XssSecurityProperties.class,
        EncryptProperties.class
})
public class NuclearFilterChainAutoConfiguration {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(NuclearFilterChainManager nuclearFilterChainManage) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("zerobug-filter-chain");
        registrationBean.setFilter(nuclearFilterChainManage);
        return registrationBean;
    }

    /**
     * 链路管理器
     *
     * @return {@link NuclearFilterChainManager}
     */
    @Bean
    @ConditionalOnMissingBean(NuclearFilterChainManager.class)
    public NuclearFilterChainManager nuclearFilterChainManage(FilterGlobalConfig filterGlobalConfig) {
        NuclearFilterChainManager chainManage = new NuclearFilterChainManager(filterGlobalConfig.getFilters());
        return chainManage;
    }

    @Bean
    @ConditionalOnMissingBean(FilterGlobalConfig.class)
    public FilterGlobalConfig filterGlobalConfig(StringRedisTemplate redisTemplate, HandlerMappingIntrospector handlerMappingIntrospector) {
        return FilterGlobalConfig.builder()
                .redisTemplate(redisTemplate)
                .handlerMappingIntrospector(handlerMappingIntrospector)
                .build();
    }

}
