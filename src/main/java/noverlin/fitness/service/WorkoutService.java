package noverlin.fitness.service;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.exceptions.custom.access.UserHasNotAccessRulesException;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.exceptions.custom.notFound.WorkoutNotFoundException;
import noverlin.fitness.mapper.WorkoutMapper;
import noverlin.fitness.model.User;
import noverlin.fitness.model.Workout;
import noverlin.fitness.model.WorkoutType;
import noverlin.fitness.repository.UserRepository;
import noverlin.fitness.repository.WorkoutRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    public Page<WorkoutResponse> search(
            @NotNull String username,
            WorkoutSearchRequest searchRequest,
            Pageable pageable
    ) {
        Specification<Workout> spec = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("user").get("username"), username)
        );

        if (searchRequest.getType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), searchRequest.getType()));
        }
        if (searchRequest.getFromDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), searchRequest.getFromDate()));
        }
        if (searchRequest.getToDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("date"), searchRequest.getToDate()));
        }
        if (searchRequest.getMinDuration() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), searchRequest.getMinDuration()));
        }
        if (searchRequest.getMaxDuration() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("duration"), searchRequest.getMaxDuration()));
        }

        Page<Workout> workouts = workoutRepository.findAll(spec, pageable);
        return workouts.map(workoutMapper::toDto);
    }

    public WorkoutResponse findById(Long id, String username) {
        Workout workout = workoutRepository.findByIdForUserWithUsername(id, username)
                .orElseThrow(WorkoutNotFoundException::new);
        return workoutMapper.toDto(workout);
    }

    public WorkoutResponse createForUser(String username, WorkoutRequest workoutRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Workout newWorkout = workoutMapper.toModel(workoutRequest);
        newWorkout.setUser(user);
        workoutRepository.save(newWorkout);

        return workoutMapper.toDto(newWorkout);
    }

    public WorkoutResponse update(Long id, String username, WorkoutRequest workoutRequest) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);

        if (!workout.getUser().getUsername().equals(username)) {
            throw new UserHasNotAccessRulesException();
        }

        workoutMapper.updateModelFromDto(workoutRequest, workout);
        workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    public void delete(Long id, String username) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);

        if (!workout.getUser().getUsername().equals(username)) {
            throw new UserHasNotAccessRulesException();
        }

        workoutRepository.delete(workout);
    }
}
