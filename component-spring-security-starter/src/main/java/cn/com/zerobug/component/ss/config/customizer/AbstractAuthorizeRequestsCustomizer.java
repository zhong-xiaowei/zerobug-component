package cn.com.zerobug.component.ss.config.customizer;

import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author zhongxiaowei
 * @date 2022/2/24
 */
public abstract class AbstractAuthorizeRequestsCustomizer implements Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry>, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

}
