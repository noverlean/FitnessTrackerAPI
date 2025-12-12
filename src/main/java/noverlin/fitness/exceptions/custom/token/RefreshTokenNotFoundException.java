package noverlin.fitness.exceptions.custom.token;

public class RefreshTokenNotFoundException extends TokenException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
