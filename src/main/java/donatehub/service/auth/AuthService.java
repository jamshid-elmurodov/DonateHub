package donatehub.service.auth;

import donatehub.domain.request.AuthRequest;
import donatehub.domain.request.RefreshTokenRequest;
import donatehub.domain.response.LoginResponse;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResponse login(AuthRequest authRequest);

    LoginResponse refreshToken(RefreshTokenRequest refreshToken);
}
