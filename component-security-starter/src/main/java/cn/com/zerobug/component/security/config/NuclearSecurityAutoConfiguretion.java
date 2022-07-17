package cn.com.zerobug.component.security.config;

import cn.com.zerobug.component.security.auth.MultipleLoginAuthenticationProvider;
import cn.com.zerobug.component.security.config.customizer.LogoutConfigurerCustomizer;
import cn.com.zerobug.component.security.filter.AuthenticationTokenFilter;
import cn.com.zerobug.component.security.handler.AccessDeniedHandlerImpl;
import cn.com.zerobug.component.security.handler.AuthenticationEntryPointImpl;
import cn.com.zerobug.component.security.handler.LogoutSuccessHandlerImpl;
import cn.com.zerobug.component.security.model.SessionManager;
import cn.com.zerobug.component.security.properties.SecurityProperties;
import cn.com.zerobug.component.security.token.provider.DefaultSecurityTokenProvider;
import cn.com.zerobug.component.security.token.provider.SecurityTokenProvider;
import cn.com.zerobug.component.security.token.store.RedisSecurityTokenStore;
import cn.com.zerobug.component.security.token.store.SecurityTokenStore;
import cn.com.zerobug.component.cache.CacheCommand;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/3/20
 */
@Configuration
@Import(NuclearSecurityConfigurerAdapter.class)
public class NuclearSecurityAutoConfiguretion {

    @Resource
    private CacheCommand cacheCommand;


    /**
     * token 存储器
     * 默认redis存储
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SecurityTokenProvider.class)
    public SecurityTokenStore securityTokenStore() {
        return new RedisSecurityTokenStore(cacheCommand, sessionManager());
    }

    /**
     * token 提供者，默认实现
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SecurityTokenProvider.class)
    public SecurityTokenProvider securityTokenProvider(SecurityProperties properties) {
        return new DefaultSecurityTokenProvider(securityTokenStore(), properties, sessionManager());
    }

    /**
     * Session管理器
     * 做一些 session方面的增删改查
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        return new SessionManager(cacheCommand);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(LogoutConfigurerCustomizer.class)
    public LogoutConfigurerCustomizer logoutConfigurerCustomizer() {
        return new LogoutConfigurerCustomizer(logoutSuccessHandler());
    }

    @Bean
    public MultipleLoginAuthenticationProvider multipleLoginAuthenticationProvider(List<UserDetailsService> serviceList) {
        return new MultipleLoginAuthenticationProvider(serviceList, bCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(SecurityProperties properties, SecurityTokenProvider securityTokenProvider) {
        return new AuthenticationTokenFilter(properties, securityTokenProvider);
    }
}
