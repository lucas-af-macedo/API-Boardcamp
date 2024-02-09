package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RentalIntegrationTests {
    @Autowired
	private TestRestTemplate restTemplate;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void cleanUpDatabase() {
        rentalRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void givenWrongCustomerId_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, 1L, 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); 
        assertEquals(0, rentalRepository.count()); 
    }

    @Test
    void givenWrongGameId_whenCreatingRental_thenThrowsError() {
        CustomerModel customer = new CustomerModel(null, "Name", "01234567890");
        CustomerModel createdCustomer = customerRepository.save(customer);

        RentalDTO rental = new RentalDTO(createdCustomer.getId(), 1L, 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); 
        assertEquals(0, rentalRepository.count()); 
    }

    @Test
    void givenABadCustomerId_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(null, 1L, 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
            
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
        assertEquals(0, rentalRepository.count()); 
    }

    @Test
    void givenABadGameId_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, null, 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
            
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
        assertEquals(0, rentalRepository.count()); 
    }

    @Test
    void givenABadDaysRented_whenCreatingRental_thenThrowsError() {
        RentalDTO rental = new RentalDTO(1L, 1L, 0L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
            
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
        assertEquals(0, rentalRepository.count()); 
    }

    @Test
    void givenValidRentalButGameIsEmpty_whenCreatingRental_thenThrowsError() {
        CustomerModel customer = new CustomerModel(null, "Name", "01234567890");
        CustomerModel createdCustomer = customerRepository.save(customer);

        GameModel game = new GameModel(null, "Name", "link", 1L, 1L);
        GameModel createdGame = gameRepository.save(game);

        LocalDate rentDate = LocalDate.now();

        RentalModel rental = new RentalModel(null, createdCustomer, createdGame, rentDate, 1L, null, 1L, 0L);
        rentalRepository.save(rental);

        RentalDTO newRental = new RentalDTO(createdCustomer.getId(), createdGame.getId(), 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(newRental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
            
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()); 
        assertEquals(1, rentalRepository.count()); 
    }

    @Test
    void givenValidRental_whenCreatingRental_thenCreatesRental() {
        CustomerModel customer = new CustomerModel(null, "Name", "01234567890");
        CustomerModel createdCustomer = customerRepository.save(customer);

        GameModel game = new GameModel(null, "Name", "link", 1L, 1L);
        GameModel createdGame = gameRepository.save(game);

        RentalDTO rental = new RentalDTO(createdCustomer.getId(), createdGame.getId(), 1L);

        HttpEntity<RentalDTO> body = new HttpEntity<>(rental);

        ResponseEntity<String> response = restTemplate.exchange(
		    "/rentals",
		    HttpMethod.POST,
		    body,
		    String.class);
            
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); 
        assertEquals(1, rentalRepository.count()); 
    }
}
