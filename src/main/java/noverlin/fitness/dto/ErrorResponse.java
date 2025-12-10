package noverlin.fitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Время происхождения ошибки", example = "2025-12-10T14:00:24.909513800Z")
    Instant timestamp;

    @Schema(description = "Статус HTTP ответа", example = "401")
    int status;

    @Schema(description = "Краткое имя ошибки", example = "UNAUTHORIZED")
    String code;

    @Schema(description = "Сообщение ошибки", example = "User does not have access rights to the requested resource")
    String message;

    @Schema(description = "Путь эндпоинта в котором произошла ошибка", example = "/workouts/9")
    String path;
}
