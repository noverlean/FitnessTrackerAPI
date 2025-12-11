package noverlin.fitness.exceptions.custom.token.exceptions;

import noverlin.fitness.exceptions.custom.token.TokenException;

public class RefreshTokenNotFoundException extends TokenException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
