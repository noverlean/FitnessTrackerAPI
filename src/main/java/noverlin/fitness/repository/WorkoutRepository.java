package noverlin.fitness.repository;

import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long>, JpaSpecificationExecutor<Workout> {
    @Query("SELECT w FROM Workout w WHERE w.user.username = :username AND w.id = :id")
    Optional<Workout> findByIdForUserWithUsername(Long id, String username);
}