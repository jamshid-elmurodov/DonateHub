package donatehub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                        .requestMatchers(
                                "/api/v1/donation/statistics",
                                "/api/v1/donation/full-statistic",
                                "/api/v1/log/*",
                                "/api/v1/info/**",
                                "/api/v1/user/verified",
                                "/api/v1/user/not-verified",
                                "/api/v1/search",
                                "/api/v1/user/enable/**",
                                "/api/v1/user/disable/**",
                                "/api/v1/user/statistic/register",
                                "/api/v1/user/statistic/online",
                                "/api/v1/user/full-statistic",
                                "/api/v1/withdraw/complete/**",
                                "/api/v1/withdraw/cancel/**",
                                "/api/v1/withdraw",
                                "/api/v1/withdraw/full-statistic").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth", "/api/v1/donation/**", "/api/v1/donation/complete/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}