package uz.mydonation.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.enums.UserRole;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.request.AuthReq;
import uz.mydonation.domain.response.LoginRes;
import uz.mydonation.repo.UserRepository;
import uz.mydonation.config.security.JwtProvider;
import uz.mydonation.service.user.UserService;
import uz.mydonation.service.widget.WidgetService;
import uz.mydonation.utils.TelegramUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class TelegramOAuth2Service implements OAuth2 {
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
            log.info("Foydalanuvchi topildi: {}", user);
        } catch (BaseException e) {
            log.warn("Foydalanuvchi topilmadi, yangi foydalanuvchi yaratilmoqda: {}", authReq);
            user = userRepository.save(UserEntity.builder()
                    .chatId(authReq.getId())
                    .online(false)
                    .enable(false)
                    .firstName(authReq.getFirstName())
                    .username(authReq.getUsername())
                    .balance(0)
                    .role(UserRole.STREAMER)
                    .api(UUID.randomUUID())
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
}