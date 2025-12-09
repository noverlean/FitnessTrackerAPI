package noverlin.fitness.dto.workout;

import lombok.Getter;
import lombok.Setter;
import noverlin.fitness.model.WorkoutType;

import java.time.Instant;

@Getter
@Setter
public class WorkoutResponse {
    Long id;

    String title;

    Instant date;

    Long duration;

    Long calories;

    WorkoutType type;
}
