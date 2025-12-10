package noverlin.fitness.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import noverlin.fitness.dto.ErrorResponse;
import noverlin.fitness.exceptions.custom.access.AccessRightsException;
import noverlin.fitness.exceptions.custom.ConflictException;
import noverlin.fitness.exceptions.custom.notFound.NotFoundException;
import noverlin.fitness.exceptions.custom.token.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(HttpStatus status, String code, String message, HttpServletRequest request) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                code,
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_JSON",
                "Invalid JSON format",
                request.getDescription(false)
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        return ResponseEntity.badRequest().body(buildError(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                ex.getBindingResult().getFieldError() != null
                        ? ex.getBindingResult().getFieldError().getDefaultMessage()
                        : "Validation failed",
                request
        ));
    }

    @ExceptionHandler({ EntityNotFoundException.class, NotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex,
                                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                ex.getMessage(),
                request
        ));
    }

    @ExceptionHandler({ BadCredentialsException.class, TokenException.class })
    public ResponseEntity<ErrorResponse> handleBadCredentials(RuntimeException ex,
                                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                ex.getMessage(),
                request
        ));
    }

    @ExceptionHandler(AccessRightsException.class)
    public ResponseEntity<ErrorResponse> handleAccessRights(AccessRightsException ex,
                                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildError(
                HttpStatus.FORBIDDEN,
                "FORBIDDEN",
                ex.getMessage(),
                request
        ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex,
                                                       HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                "CONFLICT",
                ex.getMessage(),
                request
        ));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomException ex,
                                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                ex.getMessage(),
                request
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex,
                                                       HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                request
        ));
    }
}

