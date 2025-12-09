package noverlin.fitness.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "DTO с refresh токеном")
@Data
public class RefreshRequest {
    String refreshToken;
}
