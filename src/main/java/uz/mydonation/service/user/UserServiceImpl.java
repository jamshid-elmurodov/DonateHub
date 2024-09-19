package uz.mydonation.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.enums.FileType;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.projection.UserInfo;
import uz.mydonation.domain.request.UpdateUserReq;
import uz.mydonation.repo.UserRepository;
import uz.mydonation.service.file.FileService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final FileService cloudService;

    @Override
    public UserInfo findByChannelName(String channelName) {
        return repo.findByChannelNameIgnoreCase(channelName).orElseThrow(
                () -> new BaseException(
                        "Channel not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public Page<UserInfo> getUsersByEnableState(Boolean getEnables, int page, int size) {
        return repo.getAllByEnable(getEnables, PageRequest.of(page, size));
    }

    @Override
    public Page<UserInfo> searchUsers(String text, int page, int size) {
        return repo.getAllByFirstNameLikeIgnoreCaseOrUsernameLikeIgnoreCase(text, text, PageRequest.of(page, size));
    }

    @Override
    public UserInfo getById(Long chatId) {
        return repo.getByChatId(chatId).orElseThrow(
                () -> new BaseException(
                    "User not found",
                    HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public UserEntity findById(Long chatId) {
        return repo.findByChatId(chatId).orElseThrow(
                () -> new BaseException(
                        "User not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public void update(Long chatId, UpdateUserReq updateReq, MultipartFile profileImg, MultipartFile bannerImg) {
        UserEntity user = findById(chatId);

        user.setDescription(updateReq.getDescription());
        user.setChannelUrl(updateReq.getChannelUrl());
        user.setProfileImgUrl(cloudService.uploadFile(profileImg, FileType.IMAGE));
        user.setBannerImgUrl(cloudService.uploadFile(bannerImg, FileType.IMAGE));

        repo.save(user);
    }

    @Override
    public void recalculateStreamerBalance(Long streamerId, Integer amount) {
        UserEntity user = findById(streamerId);
        user.setBalance(user.getBalance() + amount);
        repo.save(user);
    }

    @Override
    public void enable(Long streamerId) {
        UserEntity user = findById(streamerId);
        user.setEnable(true);
        repo.save(user);
    }

    @Override
    public void disable(Long streamerId) {
        UserEntity user = findById(streamerId);
        user.setEnable(false);
        repo.save(user);
    }
}
