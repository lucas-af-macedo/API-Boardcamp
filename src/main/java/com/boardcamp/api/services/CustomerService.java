package com.boardcamp.api.services;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.exceptions.CustomerConflictException;
import com.boardcamp.api.exceptions.CustomerNotFoundException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;

@Service
public class CustomerService {
    final CustomerRepository customerRepository;
    
    CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public CustomerModel save(CustomerDTO dto){
        CustomerModel customer = new CustomerModel(dto);
        if( customerRepository.existsByCpf(customer.getName())){
            throw new CustomerConflictException("Cpf already exists");
        }
        return customerRepository.save(customer);
    }

    public CustomerModel findById(long id){
        if( !customerRepository.existsById(id)){
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerRepository.findById(id);
    }
}
