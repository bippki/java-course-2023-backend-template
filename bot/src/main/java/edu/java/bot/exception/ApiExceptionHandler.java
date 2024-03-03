package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponse;
import jakarta.ws.rs.Consumes;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleChatNotFoundException(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                exception.getMessage(),
                "400",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }


}
