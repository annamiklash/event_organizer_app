package pjatk.pro.event_organizer_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private final long MAX_AGE_SECS = 3600;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
                        .allowedOrigins("http://localhost:8080", "http://localhost:3000")
                        .exposedHeaders("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(MAX_AGE_SECS);
            }
        };
    }
}
