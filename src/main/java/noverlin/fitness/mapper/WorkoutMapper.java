package noverlin.fitness.mapper;

import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.Workout;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutResponse toDto(Workout workout);
    Workout toModel(WorkoutResponse workoutResponse);
}
