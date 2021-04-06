package com.justin.cruddemo.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = -2633407878030069460L;
    private final HttpStatus httpStatus;
    private Integer code;

    public ApiException() {
        httpStatus = HttpStatus.OK;
    }

    public ApiException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ApiException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }

    public ApiException(String string, HttpStatus httpStatus, Integer customCode) {
        super(string);
        this.httpStatus = httpStatus;
        this.code = customCode;
    }

    public ApiException(String string, Exception e) {
        super(string, e);
        this.httpStatus = HttpStatus.OK;
    }

}
