package donatehub.controller;

import donatehub.domain.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Widget", description = "Vidjetlar bilan bog'liq barcha operatsiyalarni ta'minlovchi API.")
public class WidgetController {
    private final WidgetService widgetService;

    @Operation(
            summary = "Widget ma'lumotlarini yangilash",
            description = "Berilgan streamer identifikatori asosida widget video va audio fayllarini yangilaydi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Yangilanishi kerak bo'lgan streamer identifikatori", required = true, example = "123"),
                    @Parameter(name = "videoFile", description = "Yangilanishi kerak bo'lgan video fayli", required = true, schema = @Schema(type = "string", format = "binary")),
                    @Parameter(name = "audioFile", description = "Yangilanishi kerak bo'lgan audio fayli", required = true, schema = @Schema(type = "string", format = "binary"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Widget muvaffaqiyatli yangilandi"),
                    @ApiResponse(responseCode = "400", description = "Noto'g'ri so'rov ma'lumotlari", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Streamer topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("/{streamerId}")
    public void update(@PathVariable Long streamerId,
                       @RequestParam MultipartFile videoFile,
                       @RequestParam MultipartFile audioFile) {
        widgetService.update(streamerId, videoFile, audioFile);
    }
}

