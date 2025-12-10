package noverlin.fitness.dto.workout;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import noverlin.fitness.model.WorkoutType;

import java.time.Instant;

@Accessors(chain = true)
@Data
public class WorkoutRequest {

    @Schema(description = "Название тренировки", example = "Утренняя пробежка")
    @NotBlank
    @Size(max = 100)
    String title;

    @Schema(description = "Время начала тренировки", example = "2025-12-09T12:15:00Z")
    @PastOrPresent
    Instant date;

    @Schema(description = "Продолжительность тренировки в миллисекундах", example = "3600000")
    @Positive
    Long duration;

    @Schema(description = "Количестко сожженых калорий", example = "434")
    @Positive
    Long calories;

    @Schema(description = "Тип тренировки", example = "CARDIO")
    WorkoutType type;
}
