package edu.java.bot.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import edu.java.bot.entity.ApiErrorResponse;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponseException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;
}
