package com.reservation.reservation.exception;

import com.reservation.reservation.config.Code;
import com.reservation.reservation.domain.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisorController {

    @ExceptionHandler(CustomExcepiton.class)
    Response processExceptionError(CustomExcepiton e) {
        return new Response(Code.VALIDATE_ERROR_CODE, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    Response validExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String messgeFormat = String.format(fieldError.getDefaultMessage());
        return new Response(Code.VALIDATE_ERROR_CODE, messgeFormat, null);
    }

    @ExceptionHandler(Exception.class)
    Response commonExceptionError(Exception e) {
        log.error("error msg {}", e.getMessage());
        return new Response(Code.INTERNAL_SERVER_ERROR_CODE, e.getMessage() ,null);
    }
}
