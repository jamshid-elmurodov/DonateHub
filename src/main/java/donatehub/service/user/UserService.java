package donatehub.service.user;

import donatehub.domain.projections.UserInfoForView;
import donatehub.domain.projections.UserStatisticResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.projections.UserInfoForDonate;
import donatehub.domain.projections.UserInfo;
import donatehub.domain.request.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserInfoForDonate findByChannelName(String channelName);

    Page<UserInfoForView> getUsersByEnableState(Boolean getEnables, int page, int size);

    Page<UserInfoForView> searchUsers(String text, Boolean action, int page, int size);

    UserInfo getById(Long id);

    UserEntity findById(Long id);

    void recalculateStreamerBalance(Long streamerId, Float amount);

    void update(Long userId, UserUpdateRequest updateReq, MultipartFile profileImg, MultipartFile bannerImg);

    void setEnable(Long streamerId, boolean action);

    void setOnline(Long streamerId, boolean action);

    List<UserStatisticResponse> getStatisticsOfRegister(int days);

    List<UserStatisticResponse> getStatisticOfLastOnline(int days);

    void fullRegister(UserEntity user, UserUpdateRequest updateReq, MultipartFile profileImg, MultipartFile bannerImg);
}
