package noverlin.fitness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    Instant timestamp;
    int status;
    String code;
    String message;
    String path;
}
