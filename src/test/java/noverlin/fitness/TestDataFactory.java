package noverlin.fitness;

import noverlin.fitness.model.RefreshToken;
import noverlin.fitness.model.User;

import java.time.Instant;
import java.util.Set;

public class TestDataFactory {

    public static User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPasswordHash("password-hash");
        user.setRoles(Set.of("User"));
        return user;
    }

    public static RefreshToken createValidRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setDeviceId("device-id");
        token.setRevoked(false);
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        token.setToken("refresh-token");
        return token;
    }
}

