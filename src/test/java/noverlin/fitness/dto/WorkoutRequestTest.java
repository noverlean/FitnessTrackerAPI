package noverlin.fitness.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.model.WorkoutType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validWorkoutRequest_ShouldPassValidation() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Morning Run")
                .setDate(Instant.now())
                .setDuration(3600000L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankTitle_ShouldFailValidation() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle(" ")
                .setDate(Instant.now())
                .setDuration(3600000L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void tooLongTitle_ShouldFailValidation() {
        String longTitle = "A".repeat(101);
        WorkoutRequest request = new WorkoutRequest()
                .setTitle(longTitle)
                .setDate(Instant.now())
                .setDuration(3600000L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void futureDate_ShouldFailValidation() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Morning Run")
                .setDate(Instant.now().plusSeconds(3600)) // будущее время
                .setDuration(3600000L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("date")));
    }

    @Test
    void negativeDuration_ShouldFailValidation() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Morning Run")
                .setDate(Instant.now())
                .setDuration(-100L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void negativeCalories_ShouldFailValidation() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Morning Run")
                .setDate(Instant.now())
                .setDuration(3600000L)
                .setCalories(-50L)
                .setType(WorkoutType.CARDIO);

        Set<ConstraintViolation<WorkoutRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("calories")));
    }
}

