package noverlin.fitness.exceptions.custom.access.exceptions;

import noverlin.fitness.exceptions.custom.access.AccessRightsException;

public class UserHasNotAccessRulesException extends AccessRightsException {
    public UserHasNotAccessRulesException() {
        super("User does not have access rights to the requested resource");
    }
    public UserHasNotAccessRulesException(String message) {
        super(message);
    }
}
