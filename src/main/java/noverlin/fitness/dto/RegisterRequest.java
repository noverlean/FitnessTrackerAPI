package noverlin.fitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "DTO регистрации (копирует DTO авторизации)")
@Data
public class RegisterRequest {
    @Schema(description = "Имя пользователя", example = "nickname2005")
    @NotBlank(message = "Имя не должно быть пустым")
    String username;

    @Schema(description = "Пароль пользователя", example = "secretPassword123")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Пароль должен содержать хотя бы одну букву и одну цифру"
    )
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    String password;

    @Schema(description = "Отличающий идентификатор устройства, нужен для различия сессий пользователя", example = "Samsung S21 Ultra")
    @NotBlank(message = "Идентификатор устройства не должен быть пустым")
    String deviceId;
}
