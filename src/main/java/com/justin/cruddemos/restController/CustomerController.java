package com.justin.cruddemos.restController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justin.cruddemos.dto.CustomerDTO;
import com.justin.cruddemos.model.Customer;
import com.justin.cruddemos.model.RestMessage;
import com.justin.cruddemos.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private RedisTemplate<String, CustomerDTO> redisCache;

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity<RestMessage> saveCustomer( @RequestBody CustomerDTO customer) {
        return customerService.saveCustomer(customer);
    }

    @PutMapping(path = "/", produces = "application/json")
    public ResponseEntity<RestMessage> updateCustomer( @RequestBody CustomerDTO customer) {
        return customerService.updateCustomer(customer);
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<List<Customer>> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(path = "/{customerNumber}", produces = "application/json")
    public Object getCustomerById(@PathVariable String customerNumber) {
        
        Object dto = redisCache.opsForHash().get(customerNumber, "customers");
        if(dto!=null) {
            return dto;
        }else {
            return customerService.getCustomerById(customerNumber);
        }
    }

    @DeleteMapping(path = "/{customerNumber}", produces = "application/json")
    public ResponseEntity<RestMessage> deleteCustomerById(@PathVariable String customerNumber) {
        redisCache.opsForHash().delete("customer", customerNumber);
        return customerService.deleteCustomerById(customerNumber);
    }
}
