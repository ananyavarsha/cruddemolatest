package com.justin.cruddemos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

@JsonInclude(Include.NON_NULL)
@Data
public class RestMessage {
    private String message;
    private HttpStatus httpStatus;
    private Integer code;
    private List<ApiSubError> subErrors;

    public RestMessage() {
        super();
    }

    public RestMessage(String message) {
        this.message = message;
    }

    public RestMessage(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public RestMessage(HttpStatus httpStatus, String message, Throwable ex) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public RestMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public RestMessage(String message, HttpStatus httpStatus, Integer customCode) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = customCode;
    }
    
    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }
    
    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String field, String message) {
        addSubError(new ApiValidationError(field, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectErrorMessage objectError) {
        this.addValidationError(objectError.getField(), objectError.getMessage());
    }

    public void addValidationError(List<ObjectErrorMessage> errorMessages) {
        errorMessages.forEach(this::addValidationError);
    }
}
