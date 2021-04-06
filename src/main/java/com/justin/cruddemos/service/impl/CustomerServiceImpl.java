package com.justin.cruddemos.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.justin.cruddemos.dto.CustomerDTO;
import com.justin.cruddemos.model.CustomResponseEntity;
import com.justin.cruddemos.model.Customer;
import com.justin.cruddemos.model.RestMessage;
import com.justin.cruddemos.repository.CustomerRepository;
import com.justin.cruddemos.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String RECORD_NOT_FOUND = "Record Not found for the give Customer Number";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    MessageSource messageSource;
    
    @Autowired
    private RedisTemplate<String, CustomerDTO> redisCache;

    public CustomResponseEntity saveCustomer(CustomerDTO customer) {
        RestMessage successMessage;
        Set<CustomerDTO> customerDTOs = new HashSet<>();
        Customer custFromDb = customerRepository.findByCustomerNumber(customer.getCustomerNumber());
        if (custFromDb != null) {
            successMessage = new RestMessage("Record Already Present with the give Customer Number",
                    HttpStatus.CONFLICT, 10002);
            return new CustomResponseEntity(successMessage, HttpStatus.CONFLICT);

        } else {
            Customer cust = setCustomer(customer);
            customerRepository.save(cust);
            customerDTOs.add(customer);
            redisCache.opsForHash().put("customer", customer.getCustomerNumber(), customer);
            successMessage = new RestMessage("Successfully Created", HttpStatus.CREATED, 201);
            return new CustomResponseEntity(successMessage, HttpStatus.CREATED);

        }
    }

    private Customer setCustomer(CustomerDTO customer) {
        Customer cust = new Customer();
        cust.setAge(customer.getAge());
        cust.setCustomerNumber(customer.getCustomerNumber());
        cust.setFirstName(customer.getFirstName());
        cust.setLastName(customer.getLastName());
        return cust;
    }

    public CustomResponseEntity updateCustomer(CustomerDTO customer) {
        Customer cust = customerRepository.findByCustomerNumber(customer.getCustomerNumber());
        RestMessage successMessage;
        if (cust != null) {
            cust.setAge(customer.getAge());
            cust.setFirstName(customer.getFirstName());
            cust.setLastName(customer.getLastName());
            customerRepository.save(cust);
            successMessage = new RestMessage("Successfully Updated", HttpStatus.OK, 200);
            return new CustomResponseEntity(successMessage, HttpStatus.OK);
        } else {
            successMessage = new RestMessage(RECORD_NOT_FOUND, HttpStatus.NOT_FOUND, 404);
            return new CustomResponseEntity(successMessage, HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings("unchecked")
    public CustomResponseEntity getCustomers() {
        List<Customer> list = customerRepository.findAll();
        if (list.isEmpty()) {
            RestMessage successMessage = new RestMessage(RECORD_NOT_FOUND, HttpStatus.NOT_FOUND, 404);
            return new CustomResponseEntity(successMessage, HttpStatus.NOT_FOUND);
        } else {
            return new CustomResponseEntity(list, HttpStatus.OK);
        }
    }

    @SuppressWarnings("unchecked")
    public CustomResponseEntity getCustomerById(String customerNumber) {
        Customer custFromDb = customerRepository.findByCustomerNumber(customerNumber);

        RestMessage successMessage;
        if (custFromDb != null) {
            return new CustomResponseEntity(custFromDb, HttpStatus.OK);
        } else {
            successMessage = new RestMessage(RECORD_NOT_FOUND, HttpStatus.NOT_FOUND, 404);
            return new CustomResponseEntity(successMessage, HttpStatus.NOT_FOUND);
        }
    }

    public CustomResponseEntity deleteCustomerById(String customerNumber) {
        Customer custFromDb = customerRepository.findByCustomerNumber(customerNumber);

        RestMessage successMessage;
        if (custFromDb != null) {
            customerRepository.deleteById(custFromDb.getId());
            successMessage = new RestMessage("Successfully Deleted", HttpStatus.OK, 200);
            return new CustomResponseEntity(successMessage, HttpStatus.OK);
        } else {
            successMessage = new RestMessage(RECORD_NOT_FOUND, HttpStatus.NOT_FOUND, 404);
            return new CustomResponseEntity(successMessage, HttpStatus.NOT_FOUND);
        }
    }

}
