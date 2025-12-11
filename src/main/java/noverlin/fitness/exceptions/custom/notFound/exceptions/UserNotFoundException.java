package noverlin.fitness.exceptions.custom.notFound.exceptions;

import noverlin.fitness.exceptions.custom.notFound.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User with same username was not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
