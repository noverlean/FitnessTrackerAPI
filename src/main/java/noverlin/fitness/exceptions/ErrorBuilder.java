package noverlin.fitness.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import noverlin.fitness.dto.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ErrorBuilder {

    static ErrorResponse buildError(HttpStatus status, String code, String message, HttpServletRequest request) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                code,
                message,
                request.getRequestURI()
        );
    }
}
