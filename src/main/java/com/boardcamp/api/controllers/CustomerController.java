package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.services.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/customers")
public class CustomerController {
    final CustomerService customerService;

    CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerModel> save(@RequestBody @Valid CustomerDTO body) {
        CustomerModel customer = customerService.save(body);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerModel> getCustomer(@PathVariable("customerId") Long customerId) {
        CustomerModel customer = customerService.findById(customerId);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
    
    
}