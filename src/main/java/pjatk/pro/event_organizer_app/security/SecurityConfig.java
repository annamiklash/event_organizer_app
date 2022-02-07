package pjatk.pro.event_organizer_app.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static pjatk.pro.event_organizer_app.security.enums.AllowedUrlsEnum.*;


@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(
                REGISTER.value,
                LOGIN.value,
                LOGOUT.value,
                LOCATIONS.value,
                LOCATIONS_REVIEW.value,
                LOCATION_AVAILABILITY_1.value,
                LOCATION_AVAILABILITY_2.value,
                LOCATION_DESCRIPTIONS.value,

                CATERINGS.value,
                CUISINES.value,
                CATERINGS_REVIEW.value,
                CATERING_ITEMS.value,
                CATERING_ITEM_TYPES.value,

                SERVICES.value,
                SERVICES_AVAILABILITY.value,
                SERVICE_REVIEW.value,
                RESET_PASSWORD.value,
                RESET.value,

                EVENT_TYPES.value
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

}
