package donatehub.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.enums.UserRole;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class TelegramAuthServiceService implements AuthService {
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

        String dataCheckString = TelegramUtils.createDataCheckString(authReq.getId(), authReq.getFirstName(), authReq.getUsername(), authReq.getAuthDate());

//        if (!TelegramUtils.verifyAuth(dataCheckString, botToken, authReq.getHash())) {
//            log.error("Telegramdan kelmagan request: {}", authReq);
//            throw new BaseException(
//                    "Bu request telegramdan kelmayapti",
//                    HttpStatus.BAD_REQUEST
//            );
//        }

        UserEntity user;

        try {
            user = userService.findById(authReq.getId());

            user.setLastOnlineAt(LocalDateTime.now());

            user = checkForUpdates(user, authReq);

            log.info("Foydalanuvchi topildi: {}", user);
        } catch (BaseException e) {
            log.warn("Foydalanuvchi topilmadi, yangi foydalanuvchi yaratilmoqda: {}", authReq);

            user = userRepository.save(UserEntity.builder()
                    .chatId(authReq.getId())
                    .online(false)
                    .enable(false)
                    .firstName(authReq.getFirstName())
                    .username(authReq.getUsername())
                    .balance(0f)
                    .role(UserRole.STREAMER)
                    .api(UUID.randomUUID().toString())
                    .lastOnlineAt(LocalDateTime.now())
                    .build());

            widgetService.create(user);

            log.info("Yangi foydalanuvchi yaratildi va widget qo'shildi: {}", user);
        }

        log.info("Foydalanuvchi muvaffaqiyatli login qildi: {}", user.getChatId());

        return new LoginRes(
                jwtProvider.generate(user.getChatId()),
                user.getRole()
        );
    }

    private UserEntity checkForUpdates(UserEntity user, AuthReq authReq) {
        if (!Objects.equals(user.getFirstName(), authReq.getFirstName())){
            user.setFirstName(authReq.getFirstName());
        }

        if (!Objects.equals(user.getUsername(), authReq.getUsername())){
            user.setUsername(authReq.getUsername());
        }

        return userRepository.save(user);
    }
}