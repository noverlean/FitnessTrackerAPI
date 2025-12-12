package noverlin.fitness.exceptions.custom.notFound;

public class WorkoutNotFoundException extends NotFoundException {
    public WorkoutNotFoundException() {
        super("Workout was not found");
    }
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}

