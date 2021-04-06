package com.justin.cruddemos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    ApiValidationError(String object, String field, String message) {
        this.object = object;
        this.field = field;
        this.message = message;
    }
}
