package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.exceptions.CustomerNotFoundException;
import com.boardcamp.api.exceptions.GameNotFoundException;
import com.boardcamp.api.exceptions.RentalNotFoundException;
import com.boardcamp.api.exceptions.RentalUnprocessableEntityException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;
import com.boardcamp.api.services.RentalService;

@SpringBootTest
class RentalUnitTests {
    @InjectMocks // quem recebe os mocks é a camada de Services nesse caso
	private RentalService rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void givenWrongCustomerId_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        doReturn(Optional.empty()).when(customerRepository).findById(any());

        CustomerNotFoundException exception = assertThrows(
            CustomerNotFoundException.class,
		    () -> rentalService.save(rental)
	    );

        verify(customerRepository, times(1)).findById(any());
	    verify(rentalRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Customer not found!", exception.getMessage());
    }

    @Test
    void givenWrongGameId_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);

        doReturn(Optional.of(newCustomer)).when(customerRepository).findById(any());
        doReturn(Optional.empty()).when(gameRepository).findById(any());

        GameNotFoundException exception = assertThrows(
            GameNotFoundException.class,
		    () -> rentalService.save(rental)
	    );

        verify(customerRepository, times(1)).findById(any());
        verify(gameRepository, times(1)).findById(any());
	    verify(rentalRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Game not found!", exception.getMessage());
    }

    @Test
    void givenValidRentalButGameIsEmpty_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);
        GameDTO game = new GameDTO("Name", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);

        doReturn(Optional.of(newCustomer)).when(customerRepository).findById(any());
        doReturn(Optional.of(newGame)).when(gameRepository).findById(any());
        doReturn(1L).when(rentalRepository).countByGameIdAndReturnDate(any(), any());

        RentalUnprocessableEntityException exception = assertThrows(
            RentalUnprocessableEntityException.class,
		    () -> rentalService.save(rental)
	    );

        verify(customerRepository, times(1)).findById(any());
        verify(gameRepository, times(1)).findById(any());
        verify(rentalRepository, times(1)).countByGameIdAndReturnDate(any(), any());
	    verify(rentalRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("No games on stock!", exception.getMessage());
    }

    @Test
    void givenValidRental_whenCreatingRental_thenCreatesRental() {
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);
        GameDTO game = new GameDTO("Name", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);
        LocalDate rentDate = LocalDate.now();
        RentalModel newRental = new RentalModel(rental, newGame, newCustomer, rentDate, 1L);

        doReturn(Optional.of(newCustomer)).when(customerRepository).findById(any());
        doReturn(Optional.of(newGame)).when(gameRepository).findById(any());
        doReturn(0L).when(rentalRepository).countByGameIdAndReturnDate(any(), any());
        doReturn(newRental).when(rentalRepository).save(any());

        RentalModel result = rentalService.save(rental);

        verify(customerRepository, times(1)).findById(any());
        verify(gameRepository, times(1)).findById(any());
        verify(rentalRepository, times(1)).countByGameIdAndReturnDate(any(), any());
	    verify(rentalRepository, times(1)).save(any());
	    assertEquals(newRental, result);
    }

    @Test
    void givenWrongRentalId_whenReturningRental_thenThrowsError() {
        long rentalId = 1L;
        doReturn(Optional.empty()).when(rentalRepository).findById(any());

        RentalNotFoundException exception = assertThrows(
            RentalNotFoundException.class,
		    () -> rentalService.returnRental(rentalId)
	    );

        verify(rentalRepository, times(1)).findById(any());
	    verify(rentalRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Rental not found!", exception.getMessage());
    }

    @Test
    void givenValidRentalIdButItsReturned_whenReturningRental_thenThrowsError() {
        long rentalId = 1L;
        LocalDate date = LocalDate.now();
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);
        GameDTO game = new GameDTO("Name", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);
        RentalModel newRental = new RentalModel(rental, newGame, newCustomer, date, 1L);
        newRental.setReturnDate(date);

        doReturn(Optional.of(newRental)).when(rentalRepository).findById(any());

        RentalUnprocessableEntityException exception = assertThrows(
            RentalUnprocessableEntityException.class,
		    () -> rentalService.returnRental(rentalId)
	    );

        verify(rentalRepository, times(1)).findById(any());
	    verify(rentalRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Rental already finished!", exception.getMessage());
    }

    @Test
    void givenValidRentalId_whenReturningRental_thenThrowsError() {
        long rentalId = 1L;
        LocalDate date = LocalDate.now();
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);
        CustomerDTO customer = new CustomerDTO("Name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(customer);
        GameDTO game = new GameDTO("Name", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);
        RentalModel newRental = new RentalModel(rental, newGame, newCustomer, date, 1L);

        doReturn(Optional.of(newRental)).when(rentalRepository).findById(any());
        doReturn(newRental).when(rentalRepository).save(any());

        RentalModel result = rentalService.returnRental(rentalId);

        verify(rentalRepository, times(1)).findById(any());
	    verify(rentalRepository, times(1)).save(any());
        assertEquals(newRental, result);
    }
}
