package uz.mydonation.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.enums.PaymentMethod;
import uz.mydonation.domain.model.PagedRes;
import uz.mydonation.domain.projection.DonationInfo;
import uz.mydonation.domain.request.DonationReq;
import uz.mydonation.domain.response.ExceptionRes;
import uz.mydonation.domain.response.FullStatisticRes;
import uz.mydonation.domain.response.StatisticRes;
import uz.mydonation.service.donation.DonationService;

import java.util.List;

/**
 * DonationController - Donatsiyalarni boshqarish va statistikalarni olish uchun REST API.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/donation")
public class DonationController {
    private final DonationService donationService;
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    @Hidden
    @PostMapping("/complete/{method}")
    @Operation(
            summary = "Donatsiyani to'liq amalga oshirish",
            description = "Ushbu metod donatsiyani to'liq amalga oshirish uchun so'rovlarni qabul qiladi.",
            parameters = {
                    @Parameter(name = "method", description = "To'lov usuli (CLICK yoki MIRPAY)", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "To'liq amalga oshirilgan donatsiya")
            }
    )
    public void complete(
            @RequestBody String body,
            @PathVariable PaymentMethod method
    ) {
        log.info("To'liq amalga oshirish so'rovi: {}", body);
        donationService.complete(body, method);
    }

    @PostMapping("/{streamerId}")
    @Operation(
            summary = "Donatsiya yaratish",
            description = "Ushbu metod yangi donatsiya yaratadi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Donatsiya so'rov ma'lumotlari",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DonationReq.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Muvaffaqiyatli donatsiya", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class))),
                    @ApiResponse(responseCode = "400", description = "Noto'g'ri so'rov ma'lumotlari, Donat summasi no'tog'ri", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public ResponseEntity<String> donate(
            @PathVariable Long streamerId,
            @RequestBody @Valid DonationReq donationReq
    ) {
        String response = donationService.donate(donationReq, streamerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{streamerId}")
    @Operation(
            summary = "Streamer uchun donatsiyalarni olish",
            description = "Ushbu metod berilgan streamer uchun donatsiyalar ro'yxatini qaytaradi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true),
                    @Parameter(name = "days", description = "So'rov qilinayotgan kunlar soni", required = true),
                    @Parameter(name = "page", description = "Sahifa raqami", required = true),
                    @Parameter(name = "size", description = "Sahifada ko'rsatiladigan elementlar soni", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Streamer uchun donatsiyalar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedRes.class))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public PagedRes<DonationInfo> getDonationsOfStreamer(
            @PathVariable Long streamerId,
            @RequestParam int days,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new PagedRes<>(donationService.getDonationsOfStreamer(streamerId, page, size, days));
    }

    @GetMapping
    @Operation(
            summary = "Barcha donatsiyalarni olish",
            description = "Ushbu metod barcha donatsiyalarni ro'yxatini qaytaradi.",
            parameters = {
                    @Parameter(name = "days", description = "So'rov qilinayotgan kunlar soni", required = true),
                    @Parameter(name = "page", description = "Sahifa raqami", required = true),
                    @Parameter(name = "size", description = "Sahifada ko'rsatiladigan elementlar soni", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcha donatsiyalar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedRes.class)))
            }
    )
    public PagedRes<DonationInfo> getAllDonations(
            @Valid @RequestParam @Min(1) int days,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new PagedRes<>(donationService.getAllDonations(page, size, days));
    }

    @GetMapping("/statistics")
    @Operation(
            summary = "Ma'mur uchun statistikalarni olish",
            description = "Ushbu metod ma'mur uchun statistikalarni qaytaradi.",
            parameters = {
                    @Parameter(name = "days", description = "Statistikalar uchun kunlar soni", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ma'mur uchun statistikalar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticRes.class)))
            }
    )
    public List<StatisticRes> getAllStatistics(@RequestParam int days) {
        return donationService.getStatisticsForAdmin(days);
    }

    @GetMapping("/statistics/{streamerId}")
    @Operation(
            summary = "Streamer uchun statistikalarni olish",
            description = "Ushbu metod berilgan streamer uchun statistikalarni qaytaradi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true),
                    @Parameter(name = "days", description = "Statistikalar uchun kunlar soni", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Streamer uchun statistikalar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticRes.class))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public List<StatisticRes> getStatisticsOfStreamer(
            @PathVariable Long streamerId,
            @RequestParam int days
    ) {
        return donationService.getStatisticsForStreamer(streamerId, days);
    }

    @GetMapping("/info/{streamerId}")
    @Operation(
            summary = "To'liq statistikani olish",
            description = "Ushbu metod streamer uchun to'liq statistika qaytaradi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori (optional)", required = false),
                    @Parameter(name = "days", description = "Statistikalar uchun kunlar soni", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "To'liq statistika", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullStatisticRes.class))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public FullStatisticRes getFullStatistic(
            @PathVariable(required = false) Long streamerId,
            @RequestParam int days
    ) {
        return donationService.getFullStatistic(streamerId, days);
    }

    @PostMapping("/test/{streamerId}")
    @Operation(
            summary = "Test donatsiya yaratish",
            description = "Ushbu metod test maqsadlari uchun donatsiya yaratadi.",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true),
                    @Parameter(name = "user", description = "Autentifikatsiya qilingan foydalanuvchi", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Donatsiya so'rov ma'lumotlari",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DonationReq.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public void testDonate(
            @PathVariable Long streamerId,
            @RequestBody @Valid DonationReq donationReq,
            @AuthenticationPrincipal UserEntity user
    ) {
        donationService.testDonate(donationReq, streamerId, user);
    }
}