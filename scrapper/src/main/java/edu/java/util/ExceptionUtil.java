package edu.java.util;

import edu.java.entity.dto.bot.ApiErrorResponse;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;

public class ExceptionUtil {
    public static ApiErrorResponse fromResponseStatus(ResponseStatusException exception, String exceptionName) {
        return new ApiErrorResponse(
            exception.getReason(),
            Integer.toString(exception.getStatusCode().value()),
            exceptionName,
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new)
        );
    }
}
