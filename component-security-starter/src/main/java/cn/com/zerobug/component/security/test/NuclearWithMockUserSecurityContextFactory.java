package cn.com.zerobug.component.security.test;

import cn.com.zerobug.component.security.model.AuthenticatedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/4/12
 */
public class NuclearWithMockUserSecurityContextFactory implements WithSecurityContextFactory<NuclearWithMockUser> {

    private static final String USER = "USER";

    @Override
    public SecurityContext createSecurityContext(NuclearWithMockUser withUser) {
        Assert.notNull(withUser.id(), "id must not be null");
        Assert.notNull(withUser.username(), () -> withUser + " cannot have null username on both username and value properties");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : withUser.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        if (grantedAuthorities.isEmpty()) {
            for (String role : withUser.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        } else if (!(withUser.roles().length == 1 && USER.equals(withUser.roles()[0]))) {
            String errMsg = "You cannot define roles attribute " + Arrays.asList(withUser.roles()) + " with authorities attribute " + Arrays.asList(withUser.authorities());
            throw new IllegalStateException(errMsg);
        }
        AuthenticatedUser principal = AuthenticatedUser.builder()
                .id(withUser.id())
                .username(withUser.username())
                .password(withUser.password())
                .authorities(Collections.unmodifiableSet(AuthenticatedUser.sortAuthorities(grantedAuthorities)))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
                principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }


}
