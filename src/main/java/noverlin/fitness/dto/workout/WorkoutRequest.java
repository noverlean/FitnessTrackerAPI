package noverlin.fitness.dto.workout;

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

    @NotBlank
    @Size(max = 100)
    String title;

    @PastOrPresent
    Instant date;

    @Positive
    Long duration;

    @Positive
    Long calories;

    WorkoutType type;
}
