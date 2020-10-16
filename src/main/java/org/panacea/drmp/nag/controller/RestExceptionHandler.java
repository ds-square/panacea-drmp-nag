package org.panacea.drmp.nag.controller;


import lombok.extern.slf4j.Slf4j;

import org.panacea.drmp.nag.exception.NAGException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("WeakerAccess")
    public static final String INTERNAL_SERVER_ERROR_ERR_MSG = "Internal server error: ";

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(NAGException.class)
    protected ResponseEntity<Object> resourceNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}

