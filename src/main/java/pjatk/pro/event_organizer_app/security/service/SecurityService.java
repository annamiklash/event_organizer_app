package pjatk.pro.event_organizer_app.security.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.enums.UserTypeEnum;
import pjatk.pro.event_organizer_app.security.model.AuthenticationToken;
import pjatk.pro.event_organizer_app.security.model.UserCredentials;
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity;
import pjatk.pro.event_organizer_app.user.model.User;
import pjatk.pro.event_organizer_app.user.model.dto.LoginDto;
import pjatk.pro.event_organizer_app.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityService {

   private final PasswordEncoderSecurity passwordEncoderUtility;

   private final UserService userService;

    public boolean isPasswordMatch(LoginDto loginDto) {
        final String passwordFromRequest = loginDto.getPassword();

        final User userFromDb = userService.getUserByEmail(loginDto.getEmail());
        final String hashedPasswordFromDB = userFromDb.getPassword();

        return passwordEncoderUtility.doPasswordsMatch(passwordFromRequest, hashedPasswordFromDB);
    }

    public void buildSecurityContext(LoginDto loginDto, HttpServletRequest request) {
        final User user = userService.getUserByEmail(loginDto.getEmail());
        final UserCredentials userCredentials = UserCredentials.builder()
                .login(loginDto.getEmail())
                .userId(user.getId())
                .userType(user.getType())
                .build();

        final List<String> authorities = getAuthorities(userCredentials);
        final List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final AuthenticationToken authenticationToken = new AuthenticationToken(grantedAuthorities, userCredentials);
        authenticationToken.setAuthenticated(true);
        final SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        final HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public UserCredentials getUserCredentials() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final AuthenticationToken authentication = (AuthenticationToken) context.getAuthentication();
        return authentication.getCredentials();
    }

    private List<String> getAuthorities(UserCredentials userCredentials) {
        final List<String> authorities = new ArrayList<>();
        if (userCredentials.getUserType().equals(UserTypeEnum.BUSINESS.getValue())) {
            authorities.add("BUSINESS");
        } else if (userCredentials.getUserType().equals(UserTypeEnum.CUSTOMER.getValue())) {
            authorities.add("CUSTOMER");
        } else if (userCredentials.getUserType().equals(UserTypeEnum.ADMIN.getValue())) {
            authorities.add("ADMIN");
        }

        return authorities;
    }

}
