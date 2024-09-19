package uz.mydonation.service.user;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.projection.UserInfo;
import uz.mydonation.domain.request.UpdateUserReq;

public interface UserService {
    UserInfo findByChannelName(String channelName);

    Page<UserInfo> getUsersByEnableState(Boolean getEnables, int page, int size);

    Page<UserInfo> searchUsers(String text, int page, int size);

    UserInfo getById(Long chatId);

    UserEntity findById(Long chatId);

    void recalculateStreamerBalance(Long streamerId, Integer amount);

    void update(Long userId, UpdateUserReq updateReq, MultipartFile profileImg, MultipartFile bannerImg);

    void enable(Long streamerId);

    void disable(Long streamerId);
}
