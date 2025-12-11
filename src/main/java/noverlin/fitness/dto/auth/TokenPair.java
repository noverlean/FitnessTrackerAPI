package noverlin.fitness.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "DTO пары access/refresh токенов")
@Data
@AllArgsConstructor
public class TokenPair {
    @Schema(description = "Access токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNzY1MDIyNjE1LCJleHAiOjE3NjUwMjM1MTUsInVzZXJuYW1lIjoibmV3IHVzZXIgMiIsInJvbGVzIjpbIlVTRVIiXX0.aVmC1hGhdfmnj9DwTEHOVr3LubTqN8079TryDU66ECI"
    )
    String accessToken;

    @Schema(description = "Refresh токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNzY1MDIyNjE1LCJleHAiOjE3NjYzMTg2MTUsImRldmljZUlkIjoiYmFzaWMgbGFwdG9wIGRldmljZSJ9.7oacKQgtfTCxUUsMGzN7TyOEE8LBiAmheIhk0sUii1Q"
    )
    String refreshToken;
}
