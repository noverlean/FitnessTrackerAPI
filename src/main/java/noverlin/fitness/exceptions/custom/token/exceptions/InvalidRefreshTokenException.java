package noverlin.fitness.exceptions.custom.token.exceptions;

import noverlin.fitness.exceptions.custom.token.TokenException;

public class InvalidRefreshTokenException extends TokenException {
    public InvalidRefreshTokenException() {
        super("Refresh token invalid");
    }
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
