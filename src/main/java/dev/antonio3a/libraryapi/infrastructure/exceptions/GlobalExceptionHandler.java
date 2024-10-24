package dev.antonio3a.libraryapi.infrastructure.exceptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class,
            NoSuchElementException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false)),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({
            HandlerMethodValidationException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentNotValidException.class,
            PropertyReferenceException.class,
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex, WebRequest request) {
        String message = switch (ex) {
            case MethodArgumentNotValidException exception -> exception.getFieldErrors()
                    .stream()
                    .map(fieldError -> "field: " + "'" + fieldError.getField() + "'" + " " + fieldError.getDefaultMessage())
                    .reduce((string1, string2) -> string1 + "; " + string2)
                    .orElse(ex.getMessage());
            case HandlerMethodValidationException exception -> exception.getAllValidationResults()
                    .stream()
                    .flatMap(parameterValidationResult -> {
                                if (parameterValidationResult instanceof ParameterErrors parameterErrors) {
                                    return parameterErrors.getFieldErrors()
                                            .stream()
                                            .map(fieldError -> "field: " + "'" + fieldError.getField() + "'" + " " + fieldError.getDefaultMessage());
                                } else {
                                    return parameterValidationResult.getResolvableErrors()
                                            .stream()
                                            .map(messageSourceResolvable ->
                                                    "parameter: " + "'" + parameterValidationResult.getMethodParameter()
                                                            .getParameterName() + "'" + " " + messageSourceResolvable.getDefaultMessage());
                                }
                            }
                    )
                    .reduce((string1, string2) -> string1 + "; " + string2)
                    .orElse(ex.getMessage());
            default -> ex.getMessage();
        };
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, message, request.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
    )
    public ResponseEntity<Void> handleUnauthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(AccessDeniedException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getDescription(false)),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex, WebRequest request) {
        LOGGER.error("Unhandled error (' - ')...", ex);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
    }
}
