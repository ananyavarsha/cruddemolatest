package com.justin.cruddemos.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.justin.cruddemos.dto.CustomerDTO;
import com.justin.cruddemos.model.Customer;
import com.justin.cruddemos.model.RestMessage;

public interface CustomerService {

    ResponseEntity<RestMessage> updateCustomer(CustomerDTO customer);

    ResponseEntity<RestMessage> saveCustomer(CustomerDTO customer);

    ResponseEntity<List<Customer>> getCustomers();

    ResponseEntity<Customer> getCustomerById(String customerNumber);

    ResponseEntity<RestMessage> deleteCustomerById(String customerNumber);

}
