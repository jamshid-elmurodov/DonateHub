package donatehub.controller;

import donatehub.domain.entities.UserEntity;
import donatehub.domain.projections.*;
import donatehub.domain.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import donatehub.domain.response.PagedResponse;
import donatehub.domain.request.UserUpdateRequest;
import donatehub.domain.response.ExceptionResponse;
import donatehub.service.user.UserService;

import java.util.List;
import java.util.UUID;

/**
 * UserController - Foydalanuvchilarni boshqarish va foydalanuvchi ma'lumotlarini olish uchun REST API.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Foydalanuvchilarni boshqarish va foydalanuvchi ma'lumotlarini olish uchun API")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Foydalanuvchi ma'lumotlarini olish",
            description = "Berilgan foydalanuvchi identifikatori asosida foydalanuvchi ma'lumotlarini qaytaradi.",
            parameters = @Parameter(name = "userId", description = "Foydalanuvchi identifikatori", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Streamer topilsa data qaytariladi", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserInfo.class))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/info/{userId}")
    public UserInfo getUserInfo(@PathVariable Long userId){
        return userService.getById(userId);
    }

    @Hidden
    @GetMapping("/token/{api}")
    public ModelAndView showDonationPage(@PathVariable UUID api) {
        ModelAndView modelAndView = new ModelAndView("donation");
        modelAndView.addObject("api", api);
        return modelAndView;
    }

    @Operation(
            summary = "Kanal nomi asosida streamer ma'lumotlarini olish",
            description = "Berilgan kanal nomi asosida streamer ma'lumotlarini qaytaradi.",
            parameters = @Parameter(name = "channelName", description = "Kanal nomi", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User ma'lumotlar donat uchun", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserInfoForDonate.class))),
                    @ApiResponse(responseCode = "404", description = "Noto'g'ri so'rov ma'lumotlari, Streamer topilmasa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{channelName}")
    public UserInfoForDonate getStreamerByChannelName(@PathVariable String channelName){
        return userService.findByChannelName(channelName);
    }

    @Operation(
            summary = "Tasdiqlangan foydalanuvchilarni olish",
            description = "Sahifa raqami va sahifada ko'rsatiladigan elementlar soniga asoslanib tasdiqlangan foydalanuvchilarni qaytaradi.",
            parameters = {
                    @Parameter(name = "page", description = "Sahifa raqami", required = true),
                    @Parameter(name = "size", description = "Sahifada ko'rsatiladigan elementlar soni", required = true)
            }
    )
    @GetMapping("/enabled")
    public PagedResponse<UserInfoForView> getApprovedUsers(@RequestParam int page, @RequestParam int size){
        return new PagedResponse<>(userService.getUsersByEnableState(true, page, size));
    }

    @Operation(
            summary = "Tasdiqlanmagan foydalanuvchilarni olish",
            description = "Sahifa raqami va sahifada ko'rsatiladigan elementlar soniga asoslanib tasdiqlanmagan foydalanuvchilarni qaytaradi.",
            parameters = {
                    @Parameter(name = "page", description = "Sahifa raqami", required = true),
                    @Parameter(name = "size", description = "Sahifada ko'rsatiladigan elementlar soni", required = true)
            }
    )
    @GetMapping("/disabled")
    public PagedResponse<UserInfoForView> getNotApproved(@RequestParam int page, @RequestParam int size){
        return new PagedResponse<>(userService.getUsersByEnableState(false, page, size));
    }

    @Operation(
            summary = "Foydalanuvchilarni qidirish",
            description = "Berilgan matn asosida foydalanuvchilarni qidiradi.",
            parameters = {
                    @Parameter(name = "text", description = "Qidiruv matni", required = true),
                    @Parameter(name = "page", description = "Sahifa raqami", required = true),
                    @Parameter(name = "size", description = "Sahifada ko'rsatiladigan elementlar soni", required = true),
                    @Parameter(name = "enable", description = "Status", required = true),
            }
    )
    @GetMapping("/search")
    public PagedResponse<UserInfoForView> searchEnables(@RequestParam String text, @RequestParam boolean enable, @RequestParam int page, @RequestParam int size){
        return new PagedResponse<>(userService.searchUsers(text, enable, page, size));
    }

    @Operation(
            summary = "Foydalanuvchi ma'lumotlarini yangilash",
            description = "Foydalanuvchi ma'lumotlarini yangilaydi va profil va banner rasmlarini yuklaydi.",
            parameters = @Parameter(name = "userId", description = "Foydalanuvchi identifikatori", required = true),
            requestBody = @RequestBody(
                    description = "Foydalanuvchi yangilash so'rovi ma'lumotlari",
                    content = @Content(mediaType = "multipart/form-data")
            )
    )
    @PutMapping("/{userId}")
    public void update(@PathVariable Long userId,
                       @RequestPart(value = "update", required = false) UserUpdateRequest updateReq,
                       @RequestPart("profileImg") MultipartFile profileImg,
                       @RequestPart("bannerImg") MultipartFile bannerImg) {
        System.out.println(updateReq.getChannelName());
        userService.update(userId, updateReq, profileImg, bannerImg);
    }

    @Operation(
            summary = "Streamerni faollashtirish",
            description = "Berilgan streamer identifikatori asosida streamer statusini faollashtiradi.",
            parameters = @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true)
    )
    @PutMapping("/enable/{streamerId}")
    public void enable(@PathVariable @NotNull(message = "User id null bo'lishi mumkin emas") Long streamerId){
        userService.setEnable(streamerId, true);
    }

    @Operation(
            summary = "Streamerni o'chirish",
            description = "Berilgan streamer identifikatori asosida streamer statusini o'chiradi.",
            parameters = @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true)
    )
    @PutMapping("/disable/{streamerId}")
    public void disable(@PathVariable @NotNull(message = "User id null bo'lishi mumkin emas") Long streamerId){
        userService.setEnable(streamerId, false);
    }

    @Operation(
            summary = "Streamer offline qilish",
            description = "Berilgan streamer identifikatori asosida streamer statusini offline qiladi."
    )
    @PutMapping("/offline/{streamerId}")
    public void offline(@PathVariable @NotNull(message = "User id null bo'lishi mumkin emas") Long streamerId){
        userService.setOnline(streamerId, false);
    }

    @Operation(
            summary = "Streamer setOnline qilish",
            description = "Berilgan streamer identifikatori asosida streamer statusini setOnline qiladi."
    )
    @PutMapping("/online/{streamerId}")
    public void online(@PathVariable @NotNull(message = "User id null bo'lishi mumkin emas") Long streamerId){
        userService.setOnline(streamerId, true);
    }

    @Operation(
            summary = "Ro'yxatdan o'tgan foydalanuvchilar sonini olish",
            description = "Berilgan kunlar soniga asoslanib ro'yxatdan o'tgan foydalanuvchilar sonini qaytaradi.",
            parameters = @Parameter(name = "days", description = "So'rov qilinayotgan kunlar soni", required = true)
    )
    @GetMapping("/statistic/register")
    public List<UserStatistic> getStatisticsOfRegister(@RequestParam int days){
        return userService.getStatisticsOfRegister(days);
    }

    @Operation(
            summary = "Onlayn foydalanuvchilar sonini olish",
            description = "Berilgan kunlar soniga asoslanib onlayn foydalanuvchilar sonini qaytaradi.",
            parameters = @Parameter(name = "days", description = "So'rov qilinayotgan kunlar soni. Masalan, 30 - o'tgan 30 kunlik davr uchun", required = true, example = "30")
    )
    @GetMapping("/statistic/online")
    public List<UserStatistic> getStatisticOfLastOnline(@RequestParam int days){
        return userService.getStatisticOfLastOnline(days);
    }

    @Operation(
            summary = "Streamerni to'liq register qilish",
            parameters = {
                    @Parameter(name = "streamerId", description = "Streamer identifikatori", required = true),
                    @Parameter(name = "updateReq", description = "Foydalanuvchi yangilash so'rovi", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUpdateRequest.class))),
                    @Parameter(name = "profileImg", description = "Foydalanuvchi profil rasmi", required = true, schema = @Schema(type = "string", format = "binary")),
                    @Parameter(name = "bannerImg", description = "Foydalanuvchi banner rasmi", required = true, schema = @Schema(type = "string", format = "binary"))
            },
            requestBody = @RequestBody(
                    description = "Foydalanuvchi yangilash so'rovi",
                    content = @Content(mediaType = "multipart/form-data")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Streamerni to'liq register qilish muvaffaqiyatli amalga oshirildi"),
                    @ApiResponse(responseCode = "404", description = "Streamer topilmadi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Serverda xato yuz berdi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("/register")
    public void fullRegister(@RequestPart UserUpdateRequest updateReq,
                             @RequestPart("profileImg") MultipartFile profileImg,
                             @RequestPart("bannerImg") MultipartFile bannerImg,
                             @AuthenticationPrincipal UserEntity user){
        userService.fullRegister(user, updateReq, profileImg, bannerImg);
    }

    @Operation(
            summary = "User haqidagi ma'lumotlarni olish"
    )
    @GetMapping("/me")
    public UserProfileResponse getMe(@AuthenticationPrincipal UserEntity user){
        return new UserProfileResponse(user);
    }

    @GetMapping("/full-statistic")
    @Operation(
            summary = "Umumiy statistika olish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Umumiy statistikalar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserFullStatistic.class)))
            }
    )
    public UserFullStatistic getFullStatistic() {
        return userService.getFullStatistic();
    }

    @GetMapping("/profit")
    @Operation(

    )
    public ProfitStatistic getProfitStatistic() {
        return userService.getProfitStatistic();
    }
}
