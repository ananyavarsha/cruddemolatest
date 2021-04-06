package com.justin.cruddemos.model;

import lombok.Data;

@Data
public class ObjectErrorMessage {

    private String objectName;

    private String message;

    private String field;

    public ObjectErrorMessage(String objectName, String message) {
        this.objectName = objectName;
        this.message = message;
    }

    public ObjectErrorMessage(String objectName, String field, String message) {
        this.objectName = objectName;
        this.message = message;
        this.field = field;
    }

}
