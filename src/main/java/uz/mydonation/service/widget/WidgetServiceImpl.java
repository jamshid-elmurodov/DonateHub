package uz.mydonation.service.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.DonationWidgetEntity;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.enums.FileType;
import uz.mydonation.repo.WidgetRepository;
import uz.mydonation.service.file.FileService;
import uz.mydonation.service.user.UserService;

@Service
@RequiredArgsConstructor
public class WidgetServiceImpl implements WidgetService {
    private final WidgetRepository repo;
    private final UserService userService;
    private final FileService cloudService;

    @Value("${default.donation.video.url}")
    private String videoUrl;

    @Value("${default.donation.audio.url}")
    private String audioUrl;

    @Override
    public void create(Long streamerId) {
        UserEntity streamer = userService.findById(streamerId);

        repo.save(new DonationWidgetEntity(streamer, videoUrl, audioUrl, 5));
    }

    @Override
    public void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile) {
        DonationWidgetEntity donationSetting = repo.findByStreamerId(streamerId);

        donationSetting.setVideoUrl(cloudService.uploadFile(videoFile, FileType.VIDEO));
        donationSetting.setAudioUrl(cloudService.uploadFile(audioFile, FileType.AUDIO));

        repo.save(donationSetting);
    }

    @Override
    public DonationWidgetEntity getDonationWidgetOfStreamer(Long streamerId) {
        return repo.findByStreamerId(streamerId);
    }
}