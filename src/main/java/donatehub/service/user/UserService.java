package donatehub.service.user;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.projection.UserInfoForDonate;
import donatehub.domain.projection.UserInfo;
import donatehub.domain.request.UserUpdateReq;

public interface UserService {
    UserInfoForDonate findByChannelName(String channelName);

    Page<UserInfo> getUsersByEnableState(Boolean getEnables, int page, int size);

    Page<UserInfo> searchUsers(String text, int page, int size);

    UserInfo getById(Long chatId);

    UserEntity findById(Long chatId);

    void recalculateStreamerBalance(Long streamerId, Float amount);

    void update(Long userId, UserUpdateReq updateReq, MultipartFile profileImg, MultipartFile bannerImg);

    void enable(Long streamerId);

    void disable(Long streamerId);

    void offline(Long streamerId);

    void online(Long streamerId);
}
