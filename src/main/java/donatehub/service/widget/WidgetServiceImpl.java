package donatehub.service.widget;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entity.WidgetEntity;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.enums.FileType;
import donatehub.domain.exception.BaseException;
import donatehub.repo.WidgetRepository;
import donatehub.service.cloud.CloudService;

@Service
@RequiredArgsConstructor
public class WidgetServiceImpl implements WidgetService {
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final WidgetRepository repo;
    private final CloudService cloudService;

    @Value("${default.donation.video.url}")
    private String videoUrl;

    @Value("${default.donation.audio.url}")
    private String audioUrl;

    @Override
    public WidgetEntity create(UserEntity streamer) {
        log.info("Yangi widget yaratilyapti: streamerId - {}", streamer.getId());
        WidgetEntity widget = new WidgetEntity(streamer.getId(), videoUrl, audioUrl, 5);
        WidgetEntity savedWidget = repo.save(widget);
        log.info("Widget yaratildi: {}", savedWidget);
        return savedWidget;
    }

    @Override
    public void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile) {
        log.info("Widget yangilanishi: streamerId - {}", streamerId);

        WidgetEntity donationSetting = repo.findByStreamerId(streamerId);

        if (donationSetting == null) {
            log.error("Widget topilmadi: streamerId - {}", streamerId);
            throw new BaseException(
                    "Widget topilmadi",
                    HttpStatus.NOT_FOUND
            );
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = cloudService.uploadFile(videoFile, FileType.VIDEO);
            donationSetting.setVideoUrl(videoUrl);
            log.info("Video URL yangilandi: {}", videoUrl);
        }

        if (audioFile != null && !audioFile.isEmpty()) {
            String audioUrl = cloudService.uploadFile(audioFile, FileType.AUDIO);
            donationSetting.setAudioUrl(audioUrl);
            log.info("Audio URL yangilandi: {}", audioUrl);
        }

        repo.save(donationSetting);
        log.info("Widget yangilandi: {}", donationSetting);
    }

    @Override
    public WidgetEntity getDonationWidgetOfStreamer(Long streamerId) {
        log.info("Widget olinmoqda: streamerId - {}", streamerId);
        WidgetEntity widget = repo.findByStreamerId(streamerId);
        if (widget == null) {
            log.error("Widget topilmadi: streamerId - {}", streamerId);
            throw new BaseException(
                    "Widget topilmadi",
                    HttpStatus.NOT_FOUND
            );
        }
        log.info("Widget topildi: {}", widget);
        return widget;
    }
}

