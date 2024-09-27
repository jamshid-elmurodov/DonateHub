package donatehub.config.security;

import donatehub.domain.enums.UserRole;
import donatehub.domain.exception.BaseException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import donatehub.domain.entity.UserEntity;
import donatehub.service.user.UserService;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

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
            String subToken = token.substring(7);
            Long userId = jwtProvider.extractUserId(subToken);

            UserEntity user = userService.findById(userId);

            if (!user.getEnable() && user.getFullRegisteredAt() != null) {
                throw new BaseException(
                        "Siz hali admin tomonidan tasdiqlanmadingiz iltimos admin javobini kuting",
                        HttpStatus.BAD_REQUEST
                );
            }

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            ));
        }

        filterChain.doFilter(request, response);
    }
}
