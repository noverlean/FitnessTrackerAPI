package noverlin.fitness.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void constructorAndGetters_ShouldWorkCorrectly() {
        Instant now = Instant.now();
        ErrorResponse error = new ErrorResponse(now, 404, "NOT_FOUND", "Workout not found", "/workouts/1");

        assertEquals(now, error.getTimestamp());
        assertEquals(404, error.getStatus());
        assertEquals("NOT_FOUND", error.getCode());
        assertEquals("Workout not found", error.getMessage());
        assertEquals("/workouts/1", error.getPath());
    }
}
