package com.justin.cruddemo.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.justin.cruddemos.model.ObjectErrorMessage;
import com.justin.cruddemos.model.RestMessage;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionControllerHandler extends ResponseEntityExceptionHandler {

    private static final String ACCEPT_LANGUAGE = "accept-language";

    private final MessageSource messageSource;

    @Autowired
    public ExceptionControllerHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<RestMessage> handleApiException(final ApiException exception, final Locale locale) {
        final RestMessage apiError = new RestMessage(messageSource.getMessage(exception.getMessage(), null, locale),
                exception.getHttpStatus(), exception.getCode());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }
}
