package donatehub.service.auth;

import donatehub.domain.request.AuthReq;
import donatehub.domain.response.LoginRes;

public interface AuthService {
    LoginRes login(AuthReq authReq);
}
