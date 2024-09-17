package uz.mydonation.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.conscrypt.NativeCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.repo.UserRepository;
import uz.mydonation.config.security.JwtProvider;
import uz.mydonation.service.user.UserService;
import uz.mydonation.service.widget.WidgetService;
import uz.mydonation.utils.TelegramUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class TelegramOAuth2Service implements OAuth2 {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final WidgetService widgetService;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String login(Long userId, String firstName, String username, Long authDate, String hash) {
        String dataCheckString = TelegramUtils.createDataCheckString(userId, firstName, username, authDate);

//        if (!TelegramUtils.verifyAuth(dataCheckString, botToken, hash)) {
//            throw new BaseException(
//                    "Bu request telegramdan kelmayapti",
//                    HttpStatus.BAD_REQUEST
//            );
//        }

        UserEntity user;

        try {
            user = userService.findById(userId);
        } catch (BaseException e){
            user = userRepository.save(UserEntity.builder()
                            .chatId(userId)
                            .online(false)
                            .enable(false)
                            .firstName(firstName)
                            .username(username)
                            .balance(0)
                            .build());

            widgetService.create(userId);
        }

        return jwtProvider.generate(user.getChatId());
    }
}
