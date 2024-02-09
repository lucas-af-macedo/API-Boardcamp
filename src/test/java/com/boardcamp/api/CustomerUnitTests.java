package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.exceptions.CustomerCpfConflictException;
import com.boardcamp.api.exceptions.CustomerNotFoundException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.services.CustomerService;

@SpringBootTest
class CustomerUnitTests {
    @InjectMocks // quem recebe os mocks é a camada de Services nesse caso
	private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void givenRepeatedCpf_whenCreatingCustomer_thenThrowsError() {
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        doReturn(true).when(customerRepository).existsByCpf(any());

        CustomerCpfConflictException exception = assertThrows(
            CustomerCpfConflictException.class,
		    () -> customerService.save(customer)
	    );

        verify(customerRepository, times(1)).existsByCpf(any());
	    verify(customerRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Cpf already exists", exception.getMessage());
    }

    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreatesCustomer() {
        // given
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);

        doReturn(false).when(customerRepository).existsByCpf(any());
        doReturn(newCustomer).when(customerRepository).save(any());

        // when
        CustomerModel result = customerService.save(customer);

        // then
        verify(customerRepository, times(1)).existsByCpf(any());
        verify(customerRepository, times(1)).save(any());
        assertEquals(newCustomer, result);
    }

    @Test
    void givenANonexistentId_whenFindingCustomer_thenThrowsError() {
        // given
        long customerId = 1L;
        doReturn(false).when(customerRepository).existsById(customerId);

        // when
        CustomerNotFoundException exception = assertThrows(
            CustomerNotFoundException.class,
		    () -> customerService.findById(customerId)
	    );

        // then
        verify(customerRepository, times(1)).existsById(customerId);
	    verify(customerRepository, times(0)).findById(customerId);
	    assertNotNull(exception);
	    assertEquals("Customer not found!", exception.getMessage());
    }

    @Test
    void givenAnExistentId_whenFindingCustomer_thenReturnCustomer() {
        // given
        long customerId = 1L;
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);

        doReturn(true).when(customerRepository).existsById(customerId);
        doReturn(newCustomer).when(customerRepository).findById(customerId);

        // when
        CustomerModel result = customerService.findById(customerId);

        // then
        verify(customerRepository, times(1)).existsById(customerId);
	    verify(customerRepository, times(1)).findById(customerId);
	    assertEquals(newCustomer, result);
    }
}
