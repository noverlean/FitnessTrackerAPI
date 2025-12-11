package noverlin.fitness.exceptions.custom.notFound;

import noverlin.fitness.exceptions.CustomException;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message);
    }
}
