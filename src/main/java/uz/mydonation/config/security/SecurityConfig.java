package uz.mydonation.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorization -> authorization
//                        .requestMatchers(
//                                "/api/v1/donation/statistics",
//                                "/api/v1/user/disable/*",
//                                "/api/v1/user/enable/*",
//                                "/api/v1/user/verified",
//                                "/api/v1/user/not-verified",
//                                "/api/v1/user/search",
//                                "/api/v1/user/*",
//                                "/api/v1/donation").hasRole("ADMIN")
//                        .requestMatchers(streamer).hasRole("STREAMER")
//                        .requestMatchers(permits).permitAll()
                        .anyRequest().permitAll())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Qo'shimcha xavfsizlik sozlamalari uchun metod.
     *
     * @param web WebSecurity obyekti
     */
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/webjars/**"
        );
    }
}
