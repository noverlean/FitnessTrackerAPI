package noverlin.fitness.mapper;

import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutResponse toDto(Workout workout);
    Workout toModel(WorkoutRequest workoutResponse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateModelFromDto(WorkoutRequest dto, @MappingTarget Workout workout);
}
