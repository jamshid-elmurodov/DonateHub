package uz.mydonation.config.security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.service.user.UserService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            Long userId = jwtProvider.extractId(token.substring(7));

            UserEntity user = userService.findById(userId);

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            if (!user.getEnable()){
                authorities = List.of();
            }

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    authorities
            ));
        }

        filterChain.doFilter(request, response);
    }
}
