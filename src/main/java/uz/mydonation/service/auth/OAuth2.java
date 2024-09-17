package uz.mydonation.service.auth;

public interface OAuth2 {
    String login(Long userId, String firstName, String username, Long authDate, String hash);
}
