package com.justin.cruddemos.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.justin.cruddemos.model.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {

    Customer findByCustomerNumber(String customerNumber);

}
