package noverlin.fitness.specification;

import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.model.Workout;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSpecifications {

    public Specification<Workout> build(String username, WorkoutSearchRequest searchRequest) {
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

        return spec;
    }
}
