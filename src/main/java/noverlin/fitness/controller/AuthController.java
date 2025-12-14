package noverlin.fitness.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noverlin.fitness.api.AuthApi;
import noverlin.fitness.dto.auth.LoginRequest;
import noverlin.fitness.dto.auth.RefreshRequest;
import noverlin.fitness.dto.auth.RegisterRequest;
import noverlin.fitness.dto.auth.TokenPair;
import noverlin.fitness.service.AuthService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService auth;

    @Override
    public TokenPair register(@Valid @RequestBody RegisterRequest registerRequest) {
        return auth.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getDeviceId());
    }


    @Override
    public TokenPair login(@Valid @RequestBody LoginRequest loginRequest) {
        return auth.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getDeviceId());
    }

    @Override
    public TokenPair refresh(@Valid @RequestBody RefreshRequest req) {
        return auth.refresh(req.getRefreshToken());
    }

    @Override
    public void logout(@Valid @RequestBody RefreshRequest req) {
        auth.logout(req.getRefreshToken());
    }
}
