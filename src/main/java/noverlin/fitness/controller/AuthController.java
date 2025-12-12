package noverlin.fitness.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noverlin.fitness.dto.auth.LoginRequest;
import noverlin.fitness.dto.auth.RefreshRequest;
import noverlin.fitness.dto.auth.RegisterRequest;
import noverlin.fitness.dto.auth.TokenPair;
import noverlin.fitness.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "API пользовательской авторизации")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService auth;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрирует пользователя, если данные валидны - сразу авторизует пользователя, выдавая пару access/refresh токенов"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public TokenPair register(@Valid @RequestBody RegisterRequest registerRequest) {
        return auth.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getDeviceId());
    }


    @Operation(
            summary = "Авторизация пользователя",
            description = "Авторизует пользователя, если данные валидны - авторизует пользователя, выдавая пару access/refresh токенов"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public TokenPair login(@Valid @RequestBody LoginRequest loginRequest) {
        return auth.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getDeviceId());
    }

    @Operation(
            summary = "Обновление токенов",
            description = "Ротация refresh и выдача новой пары"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/refresh")
    public TokenPair refresh(@Valid @RequestBody RefreshRequest req) {
        return auth.refresh(req.getRefreshToken());
    }

    @Operation(
            summary = "Логаут",
            description = "Отзыв refresh токена. Пользователь выходит из аккаунта"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshRequest req) {
        auth.logout(req.getRefreshToken());
    }
}
