package cn.com.zerobug.component.security.config;

import cn.com.zerobug.component.security.config.customizer.LogoutConfigurerCustomizer;
import cn.com.zerobug.component.security.filter.AuthenticationTokenFilter;
import cn.com.zerobug.component.security.properties.SecurityProperties;
import cn.com.zerobug.component.security.auth.MultipleLoginAuthenticationProvider;
import cn.com.zerobug.component.security.config.customizer.AbstractAuthorizeRequestsCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * spring security配置
 *
 * @author zhongxiaowei
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class NuclearSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler                       accessDeniedHandler;
    private final AuthenticationTokenFilter                 authenticationTokenFilter;
    private final MultipleLoginAuthenticationProvider       multipleLoginAuthenticationProvider;
    private final LogoutConfigurerCustomizer                logoutConfigurerCustomizer;
    private final List<AbstractAuthorizeRequestsCustomizer> expressionInterceptUrlRegistrys;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler).and()
                .logout(logoutConfigurerCustomizer)
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .and()
                .authorizeRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistrys.forEach(item -> item.customize(expressionInterceptUrlRegistry)))
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(multipleLoginAuthenticationProvider);
    }

}
