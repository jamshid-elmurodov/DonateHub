package donatehub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import donatehub.service.widget.WidgetService;

/**
 * WidgetController - Widget (vidjet) ma'lumotlarini boshqarish uchun REST API.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/widget")
@Tag(name = "Widget")
public class WidgetController {
    private final WidgetService widgetService;

    @Operation(
            summary = "Widget ma'lumotlarini yangilash",
            description = "Berilgan streamer identifikatori asosida widget video va audio fayllarini yangilaydi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true),
                    @Parameter(name = "videoFile", description = "Video fayli", required = true),
                    @Parameter(name = "audioFile", description = "Audio fayli", required = true)
            }
    )
    @PutMapping("/{streamerId}")
    public void update(@PathVariable Long streamerId,
                       @RequestParam MultipartFile videoFile,
                       @RequestParam MultipartFile audioFile){
        widgetService.update(streamerId, videoFile, audioFile);
    }
}
