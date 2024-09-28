package donatehub.service.user;

import donatehub.domain.projections.UserInfoForView;
import donatehub.domain.projections.UserStatisticResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.projections.UserInfoForDonate;
import donatehub.domain.constants.FileType;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.projections.UserInfo;
import donatehub.domain.request.UserUpdateRequest;
import donatehub.repo.UserRepository;
import donatehub.service.cloud.CloudService;

import static donatehub.domain.constants.UserRole.STREAMER;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger log;
    private final UserRepository repo;
    private final CloudService cloudService;

    @Override
    public UserInfoForDonate findByChannelName(String channelName) {
        log.info("Kanaldan foydalanuvchi qidirilmoqda: {}", channelName);

        UserInfoForDonate info = repo.findByChannelNameIgnoreCase(channelName).orElseThrow(
                () -> {
                    log.error("Kanal topilmadi: {}", channelName);
                    return new BaseException(
                            "Kanal topilmadi",
                            HttpStatus.NOT_FOUND
                    );
                }
        );

        UserEntity user = findById(info.getId());

        if (!user.getEnable()) {
            throw new BaseException(
                    "Streamer topilmadi",
                    HttpStatus.NOT_FOUND
            );
        }

        return info;
    }

    @Override
    public Page<UserInfoForView> getUsersByEnableState(Boolean getEnables, int page, int size) {
        log.info("Foydalanuvchilarni faollik holati bo'yicha qidirish: faol - {}, sahifa - {}, o'lcham - {}", getEnables, page, size);
        return repo.getAllByEnableAndRoleOrderByFullRegisteredAt(getEnables, PageRequest.of(page, size), STREAMER);
    }

    @Override
    public Page<UserInfoForView> searchUsers(String text, Boolean action, int page, int size) {
        log.info("Foydalanuvchilarni qidirish: matn - {}, sahifa - {}, o'lcham - {}", text, page, size);
        return repo.findAllByFirstNameOrUsernameAndEnable(text, text, action, PageRequest.of(page, size));
    }

    @Override
    public UserInfo getById(Long id) {
        log.info("ID bo'yicha foydalanuvchi olinmoqda: {}", id);
        return repo.getByIdOrderByCreatedAt(id).orElseThrow(
                () -> new BaseException(
                        "Foydalanuvchi topilmadi",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public UserEntity findById(Long chatId) {
        log.info("ID bo'yicha foydalanuvchi olinmoqda: {}", chatId);
        return repo.findById(chatId).orElseThrow(
                () -> new BaseException(
                        "Foydalanuvchi topilmadi",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public void update(Long chatId, UserUpdateRequest updateReq, MultipartFile profileImg, MultipartFile bannerImg) {
        log.info("Foydalanuvchi ma'lumotlarini yangilash: ID - {}", chatId);
        UserEntity user = findById(chatId);

        user.setDescription(updateReq.getDescription());
        user.setChannelUrl(updateReq.getChannelUrl());

        if (profileImg != null && !profileImg.isEmpty()) {
            String profileImgUrl = cloudService.uploadFile(profileImg, FileType.IMAGE);
            user.setProfileImgUrl(profileImgUrl);
        }

        if (bannerImg != null && !bannerImg.isEmpty()) {
            String bannerImgUrl = cloudService.uploadFile(bannerImg, FileType.IMAGE);
            user.setBannerImgUrl(bannerImgUrl);
        }

        repo.save(user);
        log.info("Foydalanuvchi ma'lumotlari yangilandi: ID - {}", chatId);
    }

    @Override
    public void recalculateStreamerBalance(Long streamerId, Float amount) {
        log.info("Streamer balansini qayta hisoblash: ID - {}, miqdor - {}", streamerId, amount);
        UserEntity user = findById(streamerId);
        user.setBalance(user.getBalance() + amount);
        repo.save(user);
        log.info("Streamer balansiga qo'shildi: ID - {}, yangi balans - {}", streamerId, user.getBalance());
    }

    @Override
    public void enable(Long streamerId) {
        log.info("Streamer faollashtirilmoqda: ID - {}", streamerId);
        UserEntity user = findById(streamerId);
        user.setEnable(true);
        repo.save(user);
        log.info("Streamer faollashtirildi: ID - {}", streamerId);
    }

    @Override
    public void disable(Long streamerId) {
        log.info("Streamer o'chirilmoqda: ID - {}", streamerId);
        UserEntity user = findById(streamerId);
        user.setEnable(false);
        repo.save(user);
        log.info("Streamer o'chirildi: ID - {}", streamerId);
    }

    @Override
    public void offline(Long streamerId) {
        UserEntity user = findById(streamerId);
        user.setOnline(false);
        repo.save(user);
    }

    @Override
    public void online(Long streamerId) {
        UserEntity user = findById(streamerId);
        user.setOnline(true);
        repo.save(user);
    }

    @Override
    public List<UserStatisticResponse> getStatisticsOfRegister(int days) {
        return repo.getStatisticOfRegister(days);
    }

    @Override
    public List<UserStatisticResponse> getStatisticOfLastOnline(int days) {
        return repo.getStatisticOfLastOnline(days);
    }

    @Override
    public void fullRegister(Long userId, UserUpdateRequest updateReq, MultipartFile profileImg, MultipartFile bannerImg) {
        log.info("Foydalanuvchi to'liq registratsiyadan o'tmoqda: ID - {}", userId);
        UserEntity user = findById(userId);

        user.setRole(STREAMER);
        user.setFullRegisteredAt(LocalDateTime.now());

        this.update(
                userId,
                updateReq,
                profileImg,
                bannerImg
        );

        log.info("Foydalanuvchi ma'lumotlari yangilandi: ID - {}", userId);
    }
}

