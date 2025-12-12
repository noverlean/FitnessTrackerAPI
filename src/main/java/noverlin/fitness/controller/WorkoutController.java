package noverlin.fitness.controller;

import lombok.RequiredArgsConstructor;
import noverlin.fitness.api.WorkoutApi;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WorkoutController implements WorkoutApi {

    private final WorkoutService workoutService;

    @Override
    public Page<WorkoutResponse> getAll(
            WorkoutSearchRequest searchRequest,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return workoutService.search(searchRequest, pageable);
    }

    @Override
    public WorkoutResponse getWorkoutById(@PathVariable Long id) {
        return workoutService.findById(id);
    }

    @Override
    public WorkoutResponse createWorkout(@RequestBody WorkoutRequest workoutRequest) {
        return workoutService.createForUser(workoutRequest);
    }

    @Override
    public WorkoutResponse updateWorkout(@PathVariable Long id, @RequestBody WorkoutRequest workoutRequest) {
        return workoutService.update(id, workoutRequest);
    }

    @Override
    public ResponseEntity<Object> deleteWorkout(@PathVariable Long id) {
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
