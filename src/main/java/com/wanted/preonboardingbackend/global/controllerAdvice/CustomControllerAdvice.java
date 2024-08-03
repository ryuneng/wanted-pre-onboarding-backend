package com.wanted.preonboardingbackend.global.controllerAdvice;

import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CustomControllerAdvice {

    // 적절하지 않은 파라미터의 예외처리
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {

        String msg = e.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("Fail", msg));
    }

    // 서버 오류 예외처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {

        String msg = ErrorMessage.INTERNAL_SERVER_ERROR.getMessage();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", msg));
    }
}
