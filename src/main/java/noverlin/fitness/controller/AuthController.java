package noverlin.fitness.controller;

import jakarta.validation.Valid;
import noverlin.fitness.dto.LoginRequest;
import noverlin.fitness.dto.RefreshRequest;
import noverlin.fitness.dto.RegisterRequest;
import noverlin.fitness.dto.TokenPair;
import noverlin.fitness.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public TokenPair register(@Valid @RequestBody RegisterRequest registerRequest) {
        return auth.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getDeviceId());
    }

    @PostMapping("/login")
    public TokenPair login(@Valid @RequestBody LoginRequest loginRequest) {
        return auth.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getDeviceId());
    }

    @PostMapping("/refresh")
    public TokenPair refresh(@Valid @RequestBody RefreshRequest req) {
        return auth.refresh(req.getRefreshToken());
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshRequest req) {
        auth.logout(req.getRefreshToken());
    }
}
