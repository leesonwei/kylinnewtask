package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.exception.KylinException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    protected ServerResponse<Object> handleInternalServerError(RuntimeException ex, WebRequest request) {
        return handleException(ex, request, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {KylinException.class})
    protected ServerResponse<Object> handleKylinError(RuntimeException ex, WebRequest request) {
        return handleException(ex, request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ServerResponse<Object> handleException(RuntimeException ex, WebRequest request, String errorMessage, HttpStatus status) {
        ServerResponse<Object> serverResponse = ServerResponse.createByErrorCodeMessage(status.ordinal(), errorMessage);
        logError(ex, request);
        return serverResponse;
    }

    private void logError(RuntimeException ex, WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        log.error("Method:{} | Path:{} | Message:{}", servletRequest.getMethod(), servletRequest.getServletPath(), ex.getMessage());
    }
}
