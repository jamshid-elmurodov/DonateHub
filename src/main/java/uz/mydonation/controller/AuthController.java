package uz.mydonation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.mydonation.domain.request.AuthReq;
import uz.mydonation.domain.response.LoginRes;
import uz.mydonation.service.auth.OAuth2;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2 oAuth2;

    @PostMapping
    public LoginRes auth(@RequestBody AuthReq authReq){
        return oAuth2.login(authReq);
    }

    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("test");
    }
}
