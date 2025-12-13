package noverlin.fitness.service;

import lombok.RequiredArgsConstructor;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.exceptions.custom.access.UserHasNotAccessRulesException;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.exceptions.custom.notFound.WorkoutNotFoundException;
import noverlin.fitness.jwt.CurrentUserProvider;
import noverlin.fitness.mapper.WorkoutMapper;
import noverlin.fitness.model.User;
import noverlin.fitness.model.Workout;
import noverlin.fitness.repository.UserRepository;
import noverlin.fitness.repository.WorkoutRepository;
import noverlin.fitness.specification.WorkoutSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;
    private final WorkoutSpecifications workoutSpecifications;

    public Page<WorkoutResponse> search(WorkoutSearchRequest searchRequest, Pageable pageable) {
        String username = CurrentUserProvider.getCurrentUsername();

        Specification<Workout> spec = workoutSpecifications.build(username, searchRequest);
        Page<Workout> workouts = workoutRepository.findAll(spec, pageable);
        return workouts.map(workoutMapper::toDto);
    }

    public WorkoutResponse findById(Long id) {
        String username = CurrentUserProvider.getCurrentUsername();
        Workout workout = workoutRepository.findByIdForUserWithUsername(id, username)
                .orElseThrow(WorkoutNotFoundException::new);
        return workoutMapper.toDto(workout);
    }

    public WorkoutResponse createForUser(WorkoutRequest workoutRequest) {
        String username = CurrentUserProvider.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Workout newWorkout = workoutMapper.toModel(workoutRequest);
        newWorkout.setUser(user);
        workoutRepository.save(newWorkout);

        return workoutMapper.toDto(newWorkout);
    }

    public WorkoutResponse update(Long id, WorkoutRequest workoutRequest) {
        String username = CurrentUserProvider.getCurrentUsername();
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);

        if (!workout.getUser().getUsername().equals(username)) {
            throw new UserHasNotAccessRulesException();
        }

        workoutMapper.updateModelFromDto(workoutRequest, workout);
        workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    public void delete(Long id) {
        String username = CurrentUserProvider.getCurrentUsername();
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);

        if (!workout.getUser().getUsername().equals(username)) {
            throw new UserHasNotAccessRulesException();
        }

        workoutRepository.delete(workout);
    }
}
