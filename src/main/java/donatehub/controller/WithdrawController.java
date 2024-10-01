package donatehub.controller;

import donatehub.domain.projections.WithdrawFullStatistic;
import donatehub.domain.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import donatehub.domain.constants.WithdrawStatus;
import donatehub.domain.response.PagedResponse;
import donatehub.domain.projections.WithdrawInfo;
import donatehub.service.withdraw.WithdrawService;

/**
 * WithdrawController - Pul chiqarish (withdraw) ma'lumotlarini boshqarish uchun REST API.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/withdraw")
@Tag(name = "Withdraw", description = "Pul chiqarish so'rovlarini boshqarish uchun API.")
public class WithdrawController {
    private final WithdrawService withdrawService;
    private final Logger log;

    @Operation(
            summary = "Foydalanuvchilar uchun pul chiqarish so'rovini yaratadi",
            description = "Ushbu metod berilgan streamer identifikatori asosida pul chiqarish so'rovini yaratadi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true, example = "123"),
                    @Parameter(name = "amount", description = "Chiqariladigan summa", required = true, example = "100.50"),
                    @Parameter(name = "cardNumber", description = "Kredit kartasi raqami", required = true, example = "1234-5678-9012-3456")
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pul chiqarish so'rovi muvaffaqiyatli yaratildi"),
                    @ApiResponse(responseCode = "400", description = "Noto'g'ri so'rov ma'lumotlari", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Streamer topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("/{streamerId}")
    public void create(@PathVariable Long streamerId,
                       @RequestParam Float amount,
                       @RequestParam String cardNumber) {
        withdrawService.create(streamerId, amount, cardNumber);
    }

    @Operation(
            summary = "Admin uchun withdraw statusini complete'ga o'zgartiradi",
            description = "Ushbu metod withdraw so'rovining statusini yangilaydi.",
            parameters = {
                    @Parameter(name = "withdrawId", description = "Withdraw identifikatori", required = true, example = "456")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdraw statusi muvaffaqiyatli yangilandi"),
                    @ApiResponse(responseCode = "404", description = "Withdraw so'rovi topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("/complete/{withdrawId}")
    public void setStatusToComplete(@PathVariable Long withdrawId) {
        log.info("Complete uchun so'rov {}", withdrawId);
        withdrawService.setStatus(withdrawId, WithdrawStatus.COMPLETED);
    }

    @Operation(
            summary = "Admin uchun withdraw statusini cancel'ga o'zgartiradi",
            description = "Ushbu metod withdraw so'rovining statusini yangilaydi.",
            parameters = {
                    @Parameter(name = "withdrawId", description = "Withdraw identifikatori", required = true, example = "456")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdraw statusi muvaffaqiyatli yangilandi"),
                    @ApiResponse(responseCode = "404", description = "Withdraw so'rovi topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("/cancel/{withdrawId}")
    public void setStatusCanceled(@PathVariable Long withdrawId) {
        log.info("Cancel uchun so'rov {}", withdrawId);
        withdrawService.setStatus(withdrawId, WithdrawStatus.CANCELED);
    }

    @Operation(
            summary = "Admin uchun withdraw so'rovlarini statusiga qarab qaytaradi",
            description = "Ushbu metod berilgan parametrlar asosida withdraw so'rovlarini qaytaradi.",
            parameters = {
                    @Parameter(name = "page", description = "Sahifa raqami", required = true, example = "0"),
                    @Parameter(name = "size", description = "Sahifa o'lchami", required = true, example = "10"),
                    @Parameter(name = "status", description = "Withdraw statusi", required = true, example = "COMPLETED")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdraw so'rovlari muvaffaqiyatli qaytarildi"),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping
    public PagedResponse<WithdrawInfo> getWithdrawsByStatus(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam WithdrawStatus status) {
        return new PagedResponse<>(withdrawService.getWithdrawsByStatus(page, size, status));
    }

    @Operation(
            summary = "Streamer uchun withdraw so'rovlarini statusiga qarab qaytaradi",
            description = "Ushbu metod berilgan streamer identifikatori asosida withdraw so'rovlarini qaytaradi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true, example = "123"),
                    @Parameter(name = "page", description = "Sahifa raqami", required = true, example = "0"),
                    @Parameter(name = "size", description = "Sahifa o'lchami", required = true, example = "10"),
                    @Parameter(name = "status", description = "Withdraw statusi", required = true, example = "PENDING")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdraw so'rovlari muvaffaqiyatli qaytarildi"),
                    @ApiResponse(responseCode = "404", description = "Streamer topilmadi yoki so'rovlar topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{streamerId}")
    public PagedResponse<WithdrawInfo> getWithdrawsOfStreamerByStatus(
            @PathVariable Long streamerId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam WithdrawStatus status) {
        return new PagedResponse<>(withdrawService.getWithdrawsOfStreamerByStatus(streamerId, page, size, status));
    }

    @GetMapping("/full-statistic")
    @Operation(
            summary = "Adming uchun withdraw statistikasini olish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdraw statistikasi muvaffaqiyatli olingan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WithdrawFullStatistic.class))),
            }
    )
    public WithdrawFullStatistic getFullStatistic() {
        return withdrawService.getFullStatistic();
    }
}
