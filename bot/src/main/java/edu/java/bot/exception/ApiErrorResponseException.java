package edu.java.bot.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import entity.dto.ApiErrorResponse;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponseException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;
}
