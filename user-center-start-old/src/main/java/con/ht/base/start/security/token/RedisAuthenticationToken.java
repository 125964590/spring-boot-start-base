package con.ht.base.start.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author zhengyi
 * @date 11/18/18 1:04 AM
 **/
public class RedisAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private Object principal;

    public RedisAuthenticationToken(String token) {
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    public RedisAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}