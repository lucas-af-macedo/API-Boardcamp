package com.boardcamp.api.services;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.exceptions.RentalNotFoundException;
import com.boardcamp.api.exceptions.RentalUnprocessableEntityException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;

import java.time.LocalDate;

@Service
public class RentalService {
    final RentalRepository rentalRepository;
    final CustomerRepository customerRepository;
    final GameRepository gameRepository;
    
    RentalService(RentalRepository rentalRepository, CustomerRepository customerRepository, GameRepository gameRepository){
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.gameRepository = gameRepository;
    }

    public RentalModel save(RentalDTO dto){
        CustomerModel customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(
			() -> new RentalNotFoundException("Customer not found!")
		);
        GameModel game = gameRepository.findById(dto.getGameId()).orElseThrow(
			() -> new RentalNotFoundException("Game not found!")
		);

        if (!stockAvaible(game.getId(), game.getStockTotal())){
            throw new RentalUnprocessableEntityException("No games on stock!");
        }

        LocalDate rentDate = LocalDate.now();
        long originalPrice = game.getPricePerDay() * dto.getDaysRented();

        RentalModel rental = new RentalModel(dto, game, customer, rentDate, originalPrice);

        return rentalRepository.save(rental);
    }

    boolean stockAvaible(long gameId, long stockTotal){
        long rentalsActive = rentalRepository.countByGameIdAndReturnDate(gameId, null); 
        return rentalsActive < stockTotal;
    }

}
