package noverlin.fitness.dto;

import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.WorkoutType;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutResponseTest {

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(1L);
        response.setTitle("Morning Run");
        response.setDate(Instant.now());
        response.setDuration(3600000L);
        response.setCalories(500L);
        response.setType(WorkoutType.CARDIO);

        assertEquals(1L, response.getId());
        assertEquals("Morning Run", response.getTitle());
        assertEquals(3600000L, response.getDuration());
        assertEquals(500L, response.getCalories());
        assertEquals(WorkoutType.CARDIO, response.getType());
    }
}
