package donatehub.service.user;

import donatehub.domain.projection.UserInfoForView;
import donatehub.domain.response.UserStatisticRes;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.projection.UserInfoForDonate;
import donatehub.domain.projection.UserInfo;
import donatehub.domain.request.UserUpdateReq;

import java.util.List;

public interface UserService {
    UserInfoForDonate findByChannelName(String channelName);

    Page<UserInfoForView> getUsersByEnableState(Boolean getEnables, int page, int size);

    Page<UserInfoForView> searchUsers(String text, Boolean action, int page, int size);

    UserInfo getById(Long id);

    UserEntity findById(Long id);

    void recalculateStreamerBalance(Long streamerId, Float amount);

    void update(Long userId, UserUpdateReq updateReq, MultipartFile profileImg, MultipartFile bannerImg);

    void enable(Long streamerId);

    void disable(Long streamerId);

    void offline(Long streamerId);

    void online(Long streamerId);

    List<UserStatisticRes> getStatisticsOfRegister(int days);

    List<UserStatisticRes> getStatisticOfLastOnline(int days);

    void fullRegister(Long userId, UserUpdateReq updateReq, MultipartFile profileImg, MultipartFile bannerImg);
}
