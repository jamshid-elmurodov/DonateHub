package donatehub.controller;

import donatehub.domain.response.LogDataRes;
import donatehub.domain.response.LogRes;
import donatehub.utils.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LogController - Log fayllarni boshqarish va ularni ko'rish uchun REST API.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
@Tag(name = "Log", description = "Log fayllarni boshqarish uchun API metodlari")
public class LogController {
    private final LogService logService;
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");

    @GetMapping
    @Operation(
            summary = "Log fayllar ro'yxatini olish",
            description = "Ushbu metod tizimda mavjud bo'lgan barcha log fayllar ro'yxatini qaytaradi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Log fayllar ro'yxati muvaffaqiyatli olindi"),
                    @ApiResponse(responseCode = "500", description = "Server xatosi yuz berdi")
            }
    )
    public List<LogRes> fileList() {
        log.info("Loglar ro'yxati olinmoqda");
        return logService.getLogFileList();
    }

    @GetMapping("/{fileName}")
    @Operation(
            summary = "Log fayl ma'lumotlarini olish",
            description = "Ushbu metod ko'rsatilgan log fayl nomiga qarab, uning ma'lumotlarini qaytaradi.",
            parameters = {
                    @Parameter(name = "fileName", description = "Log fayl nomi (masalan: application.log)", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Log fayl ma'lumotlari muvaffaqiyatli olindi"),
                    @ApiResponse(responseCode = "404", description = "Log fayl topilmadi"),
                    @ApiResponse(responseCode = "500", description = "Server xatosi yuz berdi")
            }
    )
    public List<LogDataRes> getDataOfLog(@PathVariable String fileName) {
        log.info("Log fayl ma'lumotlari olinmoqda: fayl nomi {}", fileName);
        return logService.getDataOfLog(fileName);
    }
}
