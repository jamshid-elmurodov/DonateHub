package donatehub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import donatehub.domain.request.AuthReq;
import donatehub.domain.model.ExceptionRes;
import donatehub.domain.response.LoginRes;
import donatehub.service.auth.AuthService;

/**
 * AuthController - Telegram orqali foydalanuvchilarni autentifikatsiya qilish uchun REST API.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @Operation(
            summary = "Foydalanuvchini Telegram orqali autentifikatsiya qilish",
            description = "Telegramdan kelgan autentifikatsiya so'rovlarini qabul qiladi va foydalanuvchini tizimga kirishini ta'minlaydi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli autentifikatsiya", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRes.class))),
                    @ApiResponse(responseCode = "400", description = "Noto'g'ri so'rov ma'lumotlari", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionRes.class)))
            }
    )
    public LoginRes auth(
            @RequestBody @Parameter(description = "Foydalanuvchining autentifikatsiya uchun kerakli ma'lumotlar") AuthReq authReq) {
        return authService.login(authReq);
    }
}