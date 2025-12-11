package noverlin.fitness.dto.workout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import noverlin.fitness.model.WorkoutType;

import java.time.Instant;

@Getter
@Setter
public class WorkoutResponse {

    @Schema(description = "Идентификатор тренировки", example = "23")
    Long id;

    @Schema(description = "Название тренировки", example = "Утренняя пробежка")
    String title;

    @Schema(description = "Время начала тренировки", example = "2025-12-09T12:15:00Z")
    Instant date;

    @Schema(description = "Продолжительность тренировки в миллисекундах", example = "3600000")
    Long duration;

    @Schema(description = "Количестко сожженых калорий", example = "434")
    Long calories;

    @Schema(description = "Тип тренировки", example = "CARDIO")
    WorkoutType type;
}
