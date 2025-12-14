package noverlin.fitness.exceptions.custom.notFound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User with same username was not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
