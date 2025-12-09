package noverlin.fitness.dto.workout;

import noverlin.fitness.model.WorkoutType;

import java.time.Instant;

public class WorkoutResponse {
    Long id;

    String title;

    Instant date;

    Long duration;

    Long calories;

    WorkoutType type;
}
