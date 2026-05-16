package com.sapondanai.spring_boot_challenge.exception;

import com.sapondanai.spring_boot_challenge.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleUserNotFound(UserNotFoundException ex) {
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleDuplicateField(DuplicateFieldException ex) {
        return ApiResponse.fail(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "Invalid value for parameter: " + ex.getName());
    }
}
