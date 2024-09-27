package donatehub.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.exception.BaseException;
import donatehub.domain.request.AuthReq;
import donatehub.domain.response.LoginRes;
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
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final WidgetService widgetService;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public LoginRes login(AuthReq authReq) {
        log.info("Login jarayoni boshlandi: {}", authReq);

        String dataCheckString = TelegramUtils.getDataCheckString(
                authReq.getId(),
                authReq.getFirstName(),
                authReq.getUsername(),
                authReq.getAuthDate()
        );

//        if (!TelegramUtils.verifyAuth(dataCheckString, botToken, authReq.getHash())) {
//            log.error("Telegramdan kelmagan request: {}", authReq);
//            throw new BaseException("Bu so'rov telegramdan kelmayapti", HttpStatus.BAD_REQUEST);
//        }

        UserEntity user;
        try {
            user = userService.findById(authReq.getId());
            updateUser(user, authReq);
        } catch (BaseException e){
            user = createNewUser(authReq);
        }

        log.info("Foydalanuvchi muvaffaqiyatli login qildi: {}", user.getId());

        return new LoginRes(
                user.getRole(),
                jwtProvider.generateToken(user.getId()),
                jwtProvider.generateRefreshToken(user.getId())
        );
    }

    private void updateUser(UserEntity user, AuthReq authReq) {
        boolean updated = false;

        if (!Objects.equals(user.getFirstName(), authReq.getFirstName())) {
            user.setFirstName(authReq.getFirstName());
            updated = true;
        }

        if (!Objects.equals(user.getUsername(), authReq.getUsername())) {
            user.setUsername(authReq.getUsername());
            updated = true;
        }

        if (updated) {
            user.setLastOnlineAt(LocalDateTime.now());
            userRepository.save(user);
            log.info("Foydalanuvchi ma'lumotlari yangilandi: {}", user);
        }

    }

    private UserEntity createNewUser(AuthReq authReq) {
        log.warn("Foydalanuvchi topilmadi, yangi foydalanuvchi yaratilmoqda: {}", authReq);

        UserEntity newUser = UserEntity.from(authReq);

        userRepository.save(newUser);
        widgetService.create(newUser);

        log.info("Yangi foydalanuvchi yaratildi va widget qo'shildi: {}", newUser);

        return newUser;
    }
}