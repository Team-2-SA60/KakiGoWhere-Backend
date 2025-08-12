package team2.kakigowherebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(
                        h ->
                                h.contentSecurityPolicy(
                                                csp ->
                                                        csp.policyDirectives(
                                                                "default-src 'self'; object-src"
                                                                    + " 'none'; base-uri 'self';"
                                                                    + " frame-ancestors 'none'"))
                                        .frameOptions(f -> f.deny()))
                .csrf(csrf -> csrf.disable()) // keep your current behavior if you don’t use
                // CSRF tokens
                .authorizeHttpRequests(reg -> reg.anyRequest().permitAll());
        return http.build();
    }
}
