package uz.mydonation.domain.projection;

/**
 * Projection for {@link uz.mydonation.domain.entity.UserEntity}
 */
public interface UserInfo {
    Long getChatId();

    String getDescription();

    String getChannelUrl();

    String getChannelName();

    String getProfileImgUrl();

    String getBannerImgUrl();

    Boolean isOnline();

    Integer getBalance();
}