package donatehub.config.security;

import donatehub.domain.exceptions.BaseException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import donatehub.domain.entities.UserEntity;
import donatehub.service.user.UserService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final Logger logger;

    public JwtFilter(JwtProvider jwtProvider, UserService userService, @Qualifier("log") Logger logger) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.logger = logger;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        String clientIp = request.getRemoteAddr();
        String requestUri = request.getRequestURI();

        logger.info("Client IP: {} \n Request URI: {}", clientIp, requestUri);

        if (token != null && token.startsWith("Bearer ")) {
            String subToken = token.substring(7);
            Long userId = jwtProvider.extractUserId(subToken);

            UserEntity user = userService.findById(userId);

            if (user.getFullRegisteredAt() == null) {
                response.setStatus(HttpStatus.CONFLICT.value());
                response.getWriter().write("Siz to'liq registratsiyadan o'tmagansiz iltimos registratsiyani amalga oshiring");
                return;
            }

            if (!user.getEnable()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("Siz hali admin tomonidan tasdiqlanmadingiz iltimos admin javobini kuting");
                return;
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
