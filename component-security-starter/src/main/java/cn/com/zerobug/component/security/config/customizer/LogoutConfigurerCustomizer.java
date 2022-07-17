package cn.com.zerobug.component.security.config.customizer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author zhongxiaowei
 * @date 2022/3/20
 */
@RequiredArgsConstructor
public class LogoutConfigurerCustomizer implements Customizer<LogoutConfigurer<HttpSecurity>>, Ordered {

    private final LogoutSuccessHandler logoutSuccessHandler;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer.logoutSuccessHandler(logoutSuccessHandler)
                .logoutUrl("/system/logout").permitAll();
    }
}
