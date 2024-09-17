package uz.mydonation.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uz.mydonation.config.security.JwtProvider;
import uz.mydonation.repo.UserRepository;

class TelegramOAuth2ServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private TelegramOAuth2Service telegramOAuth2Service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        telegramOAuth2Service.setBotToken("7025869146:AAE-e2-ipqfGfUaLNEWIWOoS9fUm1RBIBBs");
    }

//    @Test
//    public void testLogin_Successful() {
//        Long userId = 123456L;
//        String firstName = "John";
//        String username = "johndoe";
//        LocalDateTime authDate = LocalDateTime.now();
//        String hash = TelUtils.hmacSha256(String.format("auth_date=%s\nfirst_name=%s\nid=%d\nusername=%s",
//                authDate.toEpochSecond(ZoneOffset.UTC), firstName,userId, username), "7025869146:AAE-e2-ipqfGfUaLNEWIWOoS9fUm1RBIBBs");
//        String token = "generatedJwtToken";
//
//        UserEntity mockUser = UserEntity.builder()
//                .chatId(userId)
//                .firstName(firstName)
//                .username(username)
//                .api(UUID.nameUUIDFromBytes(username.getBytes()))
//                .build();
//
//        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);
//        when(jwtProvider.generate(anyLong())).thenReturn(token);
//
//        String resultToken = telegramOAuth2Service.login(userId, firstName, username, authDate, hash);
//
//        assertEquals(token, resultToken);
//
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//        verify(jwtProvider, times(1)).generate(anyLong());
//    }

//    @Test
//    public void testLogin_InvalidHash() {
//        Long userId = 123456L;
//        String firstName = "John";
//        String username = "johndoe";
////        LocalDateTime authDate = LocalDateTime.now();
////        String hash = "invalidHash";
////
////        Exception exception = assertThrows(BaseException.class, () -> {
////            telegramOAuth2Service.login(userId, firstName, username, authDate, hash);
////        });
//
//        String expectedMessage = "Bu request telegramdan kelmayapti";
////        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
}