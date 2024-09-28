package donatehub.service.auth;

import donatehub.domain.request.AuthRequest;
import donatehub.domain.response.LoginResponse;

public interface AuthService {
    LoginResponse login(AuthRequest authRequest);
}
