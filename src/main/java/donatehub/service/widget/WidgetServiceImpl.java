package donatehub.service.widget;

import donatehub.domain.projections.WidgetInfo;
import donatehub.domain.request.WidgetCreateRequest;
import donatehub.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entities.WidgetEntity;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.constants.FileType;
import donatehub.domain.exceptions.BaseException;
import donatehub.repo.WidgetRepository;
import donatehub.service.cloud.CloudService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WidgetServiceImpl implements WidgetService {
    private final Logger log;
    private final WidgetRepository repo;
    private final CloudService cloudService;
    private final UserService userService;

    @Value("${default.donation.video.url}")
    private String videoUrl;

    @Value("${default.donation.audio.url}")
    private String audioUrl;

    @Override
    public WidgetEntity create(Long streamerId, WidgetCreateRequest widgetCreateRequest, MultipartFile videoFile, MultipartFile audioFile) {
        UserEntity streamer = userService.findById(streamerId);

        log.info("Yangi widget yaratilyapti: streamerId - {}", streamerId);

        if (widgetCreateRequest.getMinAmount() < 1000) {
            throw new BaseException("Minimal miqdor 1000 so'm", HttpStatus.BAD_REQUEST);
        }

        WidgetEntity widget = WidgetEntity
                .builder()
                .streamer(streamer)
                .minDonateAmount(widgetCreateRequest.getMinAmount())
                .time(widgetCreateRequest.getTime())
                .build();

        if (videoFile == null || videoFile.isEmpty()) {
            widget.setVideoUrl(videoUrl);
        }

        if (audioFile == null || audioFile.isEmpty()) {
            widget.setAudioUrl(audioUrl);
        }

        WidgetEntity savedWidget;

        try {
            savedWidget = repo.save(widget);
        } catch (DataIntegrityViolationException e){
            throw new BaseException(
                    "Bu miqdor bilan allaqachon widget yaratilgan , ID: " + streamerId + ", miqdor: " + widgetCreateRequest.getMinAmount(),
                    HttpStatus.BAD_REQUEST
            );
        }

        log.info("Widget yaratildi: {}", savedWidget.getId());

        return savedWidget;
    }

    @Override
    public void update(Long widgetId, MultipartFile videoFile, MultipartFile audioFile, Long userId) {
        log.info("Widget yangilanishi: widgetId - {}", widgetId);

        WidgetEntity widget = findById(widgetId);

        if (!Objects.equals(widget.getStreamer().getId(), userId)) {
            throw new BaseException(
                    "Siz bu widgeti yangilay olmaysiz, ID: " + widgetId,
                    HttpStatus.FORBIDDEN
            );
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = cloudService.uploadFile(videoFile, FileType.VIDEO);
            widget.setVideoUrl(videoUrl);
            log.info("Video URL yangilandi: {}", videoUrl);
        }

        if (audioFile != null && !audioFile.isEmpty()) {
            String audioUrl = cloudService.uploadFile(audioFile, FileType.AUDIO);
            widget.setAudioUrl(audioUrl);
            log.info("Audio URL yangilandi: {}", audioUrl);
        }

        repo.save(widget);
        log.info("Widget yangilandi: {}", widget);
    }

    @Override
    public WidgetEntity getWidgetOfStreamer(Long streamerId, Float amount) {
        log.info("Widget olinmoqda: streamerId - {}", streamerId);

        List<WidgetEntity> widgets = repo.getAllByStreamerIdOrderByMinDonateAmountDesc(streamerId);

        for (WidgetEntity widget : widgets) {
            if (widget.getMinDonateAmount() <= amount) {
                return widget;
            }
        }

        throw new BaseException(
                "Widget topilmadi, streamerId: " + streamerId,
                HttpStatus.NOT_FOUND
        );
    }

    @Override
    public List<WidgetInfo> getAllByStreamerId(Long streamerId) {
        userService.findById(streamerId);

        return repo.getAllByStreamerId(streamerId);
    }

    private WidgetEntity findById(Long widgetId) {
        return repo.findById(widgetId).orElseThrow(
                () -> new BaseException(
                        "Widget topilmadi, id: " + widgetId,
                        HttpStatus.NOT_FOUND
                )
        );
    }
}

