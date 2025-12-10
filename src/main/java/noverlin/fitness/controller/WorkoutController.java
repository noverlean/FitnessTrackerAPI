package noverlin.fitness.controller;

import com.sun.security.auth.UserPrincipal;
import lombok.AllArgsConstructor;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.WorkoutType;
import noverlin.fitness.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@AllArgsConstructor
@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public Page<WorkoutResponse> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) WorkoutType type,
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate,
            @RequestParam(required = false) Long minDuration,
            @RequestParam(required = false) Long maxDuration,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        String username = userDetails.getUsername();
        return workoutService.search(username, type, fromDate, toDate, minDuration, maxDuration, pageable);
    }

    @GetMapping("/{id}")
    public WorkoutResponse getWorkoutById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return workoutService.findById(id, username);
    }

    @PostMapping
    public WorkoutResponse createWorkout(@RequestBody WorkoutRequest workoutRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return workoutService.createForUser(userDetails.getUsername(), workoutRequest);
    }

    @PutMapping("/{id}")
    public WorkoutResponse updateWorkout(@PathVariable Long id, @RequestBody WorkoutRequest workoutRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return workoutService.update(id, userDetails.getUsername(), workoutRequest);
    }
}
