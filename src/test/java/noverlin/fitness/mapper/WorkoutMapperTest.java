package noverlin.fitness.mapper;

import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.Media;
import noverlin.fitness.model.User;
import noverlin.fitness.model.Workout;
import noverlin.fitness.model.WorkoutType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutMapperTest {

    private final WorkoutMapper workoutMapper = Mappers.getMapper(WorkoutMapper.class);


    @Test
    void workoutMapper_toDto_ShouldMapCorrectly() {
        User user = new User();
        user.setUsername("testuser");

        Workout workout = new Workout()
                .setId(10L)
                .setUser(user)
                .setTitle("Morning Run")
                .setDate(Instant.now())
                .setDuration(3600000L)
                .setCalories(500L)
                .setType(WorkoutType.CARDIO);

        WorkoutResponse dto = workoutMapper.toDto(workout);

        assertEquals(workout.getId(), dto.getId());
        assertEquals(workout.getTitle(), dto.getTitle());
        assertEquals(workout.getDate(), dto.getDate());
        assertEquals(workout.getDuration(), dto.getDuration());
        assertEquals(workout.getCalories(), dto.getCalories());
        assertEquals(workout.getType(), dto.getType());
    }

    @Test
    void workoutMapper_toModel_ShouldMapCorrectly() {
        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Evening Yoga")
                .setDate(Instant.now())
                .setDuration(1800000L)
                .setCalories(200L)
                .setType(WorkoutType.STRENGTH);

        Workout workout = workoutMapper.toModel(request);

        assertEquals(request.getTitle(), workout.getTitle());
        assertEquals(request.getDate(), workout.getDate());
        assertEquals(request.getDuration(), workout.getDuration());
        assertEquals(request.getCalories(), workout.getCalories());
        assertEquals(request.getType(), workout.getType());
    }

    @Test
    void workoutMapper_updateModelFromDto_ShouldUpdateFields() {
        Workout workout = new Workout()
                .setId(10L)
                .setTitle("Old Title")
                .setDuration(1000L)
                .setCalories(100L)
                .setType(WorkoutType.CARDIO);

        WorkoutRequest request = new WorkoutRequest()
                .setTitle("Updated Title")
                .setDuration(2000L)
                .setCalories(300L)
                .setType(WorkoutType.STRENGTH);

        workoutMapper.updateModelFromDto(request, workout);

        assertEquals("Updated Title", workout.getTitle());
        assertEquals(2000L, workout.getDuration());
        assertEquals(300L, workout.getCalories());
        assertEquals(WorkoutType.STRENGTH, workout.getType());
    }
}
