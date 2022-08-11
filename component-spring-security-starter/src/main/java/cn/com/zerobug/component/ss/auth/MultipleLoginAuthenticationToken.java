package cn.com.zerobug.component.ss.auth;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author zhongxiaowei
 * @date 2022/3/7
 */
@Getter
public class MultipleLoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Class<? extends UserDetailsService> authServiceClass;

    public MultipleLoginAuthenticationToken(Object principal, Object credentials, Supplier<Class<? extends UserDetailsService>> supplier) {
        super(principal, credentials);
        authServiceClass = supplier.get();
    }

    public MultipleLoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Supplier<Class<? extends UserDetailsService>> supplier) {
        super(principal, credentials, authorities);
        authServiceClass = supplier.get();
    }
}
