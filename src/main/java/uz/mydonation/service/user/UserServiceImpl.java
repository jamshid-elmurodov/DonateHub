package uz.mydonation.service.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.enums.FileType;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.projection.UserInfo;
import uz.mydonation.domain.request.UserUpdateReq;
import uz.mydonation.repo.UserRepository;
import uz.mydonation.service.file.FileService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final UserRepository repo;
    private final FileService cloudService;

    @Override
    public UserInfo findByChannelName(String channelName) {
        log.info("Kanaldan foydalanuvchi qidirilmoqda: {}", channelName);
        return repo.findByChannelNameIgnoreCase(channelName).orElseThrow(
                () -> {
                    log.error("Kanal topilmadi: {}", channelName);
                    return new BaseException(
                            "Kanal topilmadi",
                            HttpStatus.NOT_FOUND
                    );
                }
        );
    }

    @Override
    public Page<UserInfo> getUsersByEnableState(Boolean getEnables, int page, int size) {
        log.info("Foydalanuvchilarni faollik holati bo'yicha qidirish: faol - {}, sahifa - {}, o'lcham - {}", getEnables, page, size);
        return repo.getAllByEnable(getEnables, PageRequest.of(page, size));
    }

    @Override
    public Page<UserInfo> searchUsers(String text, int page, int size) {
        log.info("Foydalanuvchilarni qidirish: matn - {}, sahifa - {}, o'lcham - {}", text, page, size);
        return repo.getAllByFirstNameLikeIgnoreCaseOrUsernameLikeIgnoreCase(text, text, PageRequest.of(page, size));
    }

    @Override
    public UserInfo getById(Long chatId) {
        log.info("ID bo'yicha foydalanuvchi olinmoqda: {}", chatId);
        return repo.getByChatId(chatId).orElseThrow(
                () -> {
                    log.error("Foydalanuvchi topilmadi: {}", chatId);
                    return new BaseException(
                            "Foydalanuvchi topilmadi",
                            HttpStatus.NOT_FOUND
                    );
                }
        );
    }

    @Override
    public UserEntity findById(Long chatId) {
        log.info("ID bo'yicha foydalanuvchi olinmoqda: {}", chatId);
        return repo.findByChatId(chatId).orElseThrow(
                () -> {
                    log.error("Foydalanuvchi topilmadi: {}", chatId);
                    return new BaseException(
                            "Foydalanuvchi topilmadi",
                            HttpStatus.NOT_FOUND
                    );
                }
        );
    }

    @Override
    public void update(Long chatId, UserUpdateReq updateReq, MultipartFile profileImg, MultipartFile bannerImg) {
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
}

