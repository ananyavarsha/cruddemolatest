package com.justin.cruddemos2.test.controller;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.cruddemos.CrudDemoApplication;
import com.justin.cruddemos.dto.CustomerDTO;
import com.justin.cruddemos.model.Customer;
import com.justin.cruddemos.repository.CustomerRepository;

import redis.embedded.RedisServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { CrudDemoApplication.class })
@WebAppConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class CustomerTestController {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    public static final String ACCEPT_LANGUAGE = "accept-language";

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private CustomerRepository customerRepository;

    public static RedisServer redisServer;

    @BeforeClass
    public static void startRedis() throws IOException {
        redisServer = new RedisServer(9996);
        redisServer.start();
    }

    @AfterClass
    public static void destory() {
        redisServer.stop();
    }
    
    @Test
    public void TestSaveCustomerSuccess() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(null);
        Mockito.when(customerRepository.save(Matchers.any(Customer.class))).thenReturn(customer);

        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(201, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestSaveCustomerMultiStatus() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(customer);
        Mockito.when(customerRepository.save(Matchers.any(Customer.class))).thenReturn(customer);

        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(409, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestGetCustomer() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        ArrayList<Customer> list = new ArrayList();
        list.add(customer);
        Mockito.when(customerRepository.findAll()).thenReturn(list);

        try {
            final MvcResult mvcResult = mvc
                    .perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestGetCustomerNotFound() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        ArrayList<Customer> list = new ArrayList();
        Mockito.when(customerRepository.findAll()).thenReturn(list);

        try {
            final MvcResult mvcResult = mvc
                    .perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            assertEquals(404, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestGetCustomerByCustomerNumber() throws Exception {
        final String uri = "/customer/1234";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(customer);

        try {
            final MvcResult mvcResult = mvc
                    .perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestGetCustomerByCustomerNumberNotFound() throws Exception {
        final String uri = "/customer/1234";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(null);

        try {
            final MvcResult mvcResult = mvc
                    .perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            assertEquals(404, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestUpdateCustomerSuccess() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(customer);
        Mockito.when(customerRepository.save(Matchers.any(Customer.class))).thenReturn(customer);

        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestUpdateCustomerNotFound() throws Exception {
        final String uri = "/customer/";
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(null);
        Mockito.when(customerRepository.save(Matchers.any(Customer.class))).thenReturn(customer);

        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(404, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestdeleteCustomerSuccess() throws Exception {
        final String uri = "/customer/1234";
        Customer customer = new Customer();
        customer.setId("1");
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(customer);
        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void TestdeleteCustomerNotFound() throws Exception {
        final String uri = "/customer/1234";
        Customer customer = new Customer();
        customer.setId("1");
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");

        Mockito.when(customerRepository.findByCustomerNumber(Matchers.anyString())).thenReturn(null);
        try {
            String inputJson = mapToJson(createCustomer());
            final MvcResult mvcResult = mvc.perform(
                    MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                    .andReturn();
            assertEquals(404, mvcResult.getResponse().getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private CustomerDTO createCustomer() {
        CustomerDTO customer = new CustomerDTO();
        customer.setAge(24);
        customer.setCustomerNumber("1234");
        customer.setFirstName("ananya");
        customer.setLastName("varsha");
        return customer;
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
