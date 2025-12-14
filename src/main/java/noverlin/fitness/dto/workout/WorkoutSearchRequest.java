package noverlin.fitness.dto.workout;

import lombok.Data;
import noverlin.fitness.model.WorkoutType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class WorkoutSearchRequest {

    private WorkoutType type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant toDate;

    private Long minDuration;
    private Long maxDuration;
}