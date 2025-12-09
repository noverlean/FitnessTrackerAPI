package noverlin.fitness.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.mapper.WorkoutMapper;
import noverlin.fitness.model.Workout;
import noverlin.fitness.model.WorkoutType;
import noverlin.fitness.repository.UserRepository;
import noverlin.fitness.repository.WorkoutRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@Service
public class WorkoutService {

    private WorkoutRepository workoutRepository;
    private UserRepository userRepository;
    private WorkoutMapper workoutMapper;

    public Page<WorkoutResponse> search(
            @NotNull String username,
            WorkoutType type,
            Instant fromDate,
            Instant toDate,
            Long minDuration,
            Long maxDuration,
            Pageable pageable
    ) {
        Specification<Workout> spec = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("user").get("username"), username)
        );

        if (type != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }
        if (fromDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate));
        }
        if (toDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate));
        }
        if (minDuration != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), minDuration));
        }
        if (maxDuration != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("duration"), maxDuration));
        }

        Page<Workout> workouts = workoutRepository.findAll(spec, pageable);
        return workouts.map(workoutMapper::toDto);
    }
}
