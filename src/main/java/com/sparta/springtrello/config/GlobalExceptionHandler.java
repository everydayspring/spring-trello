package com.sparta.springtrello.config;

import jakarta.security.auth.message.AuthException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.common.exception.ServerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<?>> invalidRequestExceptionException(
            InvalidRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(AuthException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ApiResponse<?>> handleServerException(ServerException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return getErrorResponse(status, ex.getMessage());
    }

    public ResponseEntity<ApiResponse<?>> getErrorResponse(HttpStatus status, String message) {

        return ResponseEntity.status(status).body(ApiResponse.error("오류가 발생했습니다." + message));
    }
}
