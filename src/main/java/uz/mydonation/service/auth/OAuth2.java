package uz.mydonation.service.auth;

import uz.mydonation.domain.request.AuthReq;
import uz.mydonation.domain.response.LoginRes;

public interface OAuth2 {
    LoginRes login(AuthReq authReq);
}
