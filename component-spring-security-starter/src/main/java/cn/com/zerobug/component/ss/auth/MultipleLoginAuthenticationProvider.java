package cn.com.zerobug.component.ss.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/2/26
 */
public class MultipleLoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final Map<Class<? extends UserDetailsService>, UserDetailsService> serviceMap = new HashMap<>();

    public MultipleLoginAuthenticationProvider(List<UserDetailsService> serviceList, PasswordEncoder passwordEncoder) {
        serviceList.forEach(s -> serviceMap.put(s.getClass(), s));
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MultipleLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetailsService userDetailsService = findUserDetails(authentication);
        Assert.notNull(userDetailsService, "未找到 userDetailsService 服务");
        return userDetailsService.loadUserByUsername(username);
    }

    private UserDetailsService findUserDetails(UsernamePasswordAuthenticationToken authentication) {
        Assert.isInstanceOf(MultipleLoginAuthenticationToken.class, authentication);
        MultipleLoginAuthenticationToken token = (MultipleLoginAuthenticationToken) authentication;
        Class<? extends UserDetailsService> serviceClass = token.getAuthServiceClass();
        return serviceMap.get(serviceClass);
    }

}
