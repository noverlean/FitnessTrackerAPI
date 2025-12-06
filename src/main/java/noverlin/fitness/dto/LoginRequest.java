package noverlin.fitness.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
    String deviceId;
}
