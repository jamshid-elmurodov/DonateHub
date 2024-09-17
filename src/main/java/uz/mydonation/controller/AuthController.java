package uz.mydonation.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import uz.mydonation.service.auth.OAuth2;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2 oAuth2;

    @Hidden
    @GetMapping
    public String auth(@RequestParam Long id,
                       @RequestParam("first_name") String firstName,
                       @RequestParam String username,
                       @RequestParam("auth_date") Long authDate,
                       @RequestParam String hash
    ){
        return oAuth2.login(id, firstName, username, authDate, hash);
    }

    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("test");
    }
}
