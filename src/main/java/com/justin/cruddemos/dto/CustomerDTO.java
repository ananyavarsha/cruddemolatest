package com.justin.cruddemos.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CustomerDTO implements Serializable{

    private String firstName;
    private String lastName;
    private Integer age;
    private String customerNumber;
}
