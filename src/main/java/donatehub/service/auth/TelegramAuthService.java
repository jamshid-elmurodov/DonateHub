package donatehub.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.request.AuthRequest;
import donatehub.domain.response.LoginResponse;
import donatehub.repo.UserRepository;
import donatehub.config.security.JwtProvider;
import donatehub.service.user.UserService;
import donatehub.service.widget.WidgetService;
import donatehub.utils.TelegramUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Service
@RequiredArgsConstructor
public class TelegramAuthService implements AuthService {
    private final Logger log;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final WidgetService widgetService;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        log.info("Tizimga kirish jarayoni boshlandi: {}", authRequest);

        String dataCheckString = TelegramUtils.getDataCheckString(
                authRequest.getId(),
                authRequest.getFirstName(),
                authRequest.getUsername(),
                authRequest.getAuthDate()
        );

        if (!TelegramUtils.verifyAuth(dataCheckString, botToken, authRequest.getHash())) {
            log.error("Ushbu so'rov Telegramdan emas: {}", authRequest);
            throw new BaseException("Ushbu so'rov Telegramdan emas", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findById(authRequest.getId())
                .orElseGet(() -> createNewUser(authRequest));

        updateUser(user, authRequest);

        log.info("Foydalanuvchi muvaffaqiyatli tizimga kirdi: {}", user.getId());

        return new LoginResponse(
                user.getRole(),
                jwtProvider.generateToken(user.getId()),
                jwtProvider.generateRefreshToken(user.getId())
        );
    }

    private void updateUser(UserEntity user, AuthRequest authRequest) {
        boolean updated = false;

        if (!Objects.equals(user.getFirstName(), authRequest.getFirstName())) {
            user.setFirstName(authRequest.getFirstName());
            updated = true;
        }

        if (!Objects.equals(user.getUsername(), authRequest.getUsername())) {
            user.setUsername(authRequest.getUsername());
            updated = true;
        }

        if (updated) {
            user.setLastOnlineAt(LocalDateTime.now());
            userRepository.save(user);
            log.info("Foydalanuvchi ma'lumotlari yangilandi: {}", user);
        }

    }

    private UserEntity createNewUser(AuthRequest authRequest) {
        log.warn("Foydalanuvchi topilmadi, yangi foydalanuvchi yaratilmoqda: {}", authRequest);

        UserEntity newUser = UserEntity.from(authRequest);

        userRepository.save(newUser);
        widgetService.create(newUser);

        log.info("Yangi foydalanuvchi yaratildi va widget qo'shildi: {}", newUser);

        return newUser;
    }
}