package pjatk.pro.event_organizer_app.security.model;


import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Value
public class AuthenticationToken extends AbstractAuthenticationToken implements Serializable {

    UserCredentials userCredentials;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserCredentials userCredentials) {
        super(authorities);
        this.userCredentials = userCredentials;
    }

    @Override
    public UserCredentials getCredentials() {
        return userCredentials;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
