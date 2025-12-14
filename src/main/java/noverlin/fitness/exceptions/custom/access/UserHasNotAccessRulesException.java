package noverlin.fitness.exceptions.custom.access;

public class UserHasNotAccessRulesException extends AccessRightsException {
    public UserHasNotAccessRulesException() {
        super("User does not have access rights to the requested resource");
    }
    public UserHasNotAccessRulesException(String message) {
        super(message);
    }
}
