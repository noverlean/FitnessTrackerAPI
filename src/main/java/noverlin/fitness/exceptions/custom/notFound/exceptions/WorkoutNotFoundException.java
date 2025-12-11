package noverlin.fitness.exceptions.custom.notFound.exceptions;

import noverlin.fitness.exceptions.custom.notFound.NotFoundException;

public class WorkoutNotFoundException extends NotFoundException {
    public WorkoutNotFoundException() {
        super("Workout was not found");
    }
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}

