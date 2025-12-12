package noverlin.fitness.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import noverlin.fitness.dto.auth.LoginRequest;
import noverlin.fitness.dto.auth.RefreshRequest;
import noverlin.fitness.dto.auth.RegisterRequest;
import noverlin.fitness.dto.auth.TokenPair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "API пользовательской авторизации")
@RequestMapping("/auth")
public interface AuthApi {

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
    TokenPair register(@Valid @RequestBody RegisterRequest registerRequest);


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
    TokenPair login(@Valid @RequestBody LoginRequest loginRequest);

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
    TokenPair refresh(@Valid @RequestBody RefreshRequest req);

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
    void logout(@Valid @RequestBody RefreshRequest req);
}
