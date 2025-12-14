package noverlin.fitness.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "DTO с refresh токеном")
@Data
public class RefreshRequest {
    @Schema(description = "Refresh токен", example =    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzY1Mzg1MzAz" +
                                                        "LCJleHAiOjE3NjY2ODEzMDMsImRldmljZUlkIjoiYmFzaWMgbGFwdG9wI" +
                                                        "GRldmljZSJ9.q4IiC80W9_J1mPUeFMq3SU44v3UmbjzNosPUN9TBEFM")
    private String refreshToken;
}
