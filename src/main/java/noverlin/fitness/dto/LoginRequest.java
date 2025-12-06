package noverlin.fitness.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Имя не должно быть пустым")
    String username;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Пароль должен содержать хотя бы одну букву и одну цифру"
    )
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    String password;

    @NotBlank(message = "Идентификатор устройства не должен быть пустым")
    String deviceId;
}
