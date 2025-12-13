package noverlin.fitness.specification;

import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.model.Workout;
import noverlin.fitness.model.WorkoutType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WorkoutSpecificationsTest {

    @InjectMocks
    private WorkoutSpecifications workoutSpecifications;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void build_ShouldReturnNonNullSpec() {
        WorkoutSearchRequest request = new WorkoutSearchRequest();
        request.setType(WorkoutType.CARDIO);

        Specification<Workout> spec = workoutSpecifications.build("testuser", request);

        assertNotNull(spec);
    }

}
