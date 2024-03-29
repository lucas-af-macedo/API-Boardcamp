package com.boardcamp.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.services.RentalService;


import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/rentals")
public class RentalController {
    final RentalService rentalService;

    RentalController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<RentalModel> save(@RequestBody @Valid RentalDTO body) {
        RentalModel rental = rentalService.save(body);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping
    public ResponseEntity<List<RentalModel>> getAll() {
        List<RentalModel> rental = rentalService.findAll();
        
        return ResponseEntity.status(HttpStatus.OK).body(rental);
    }

    @PutMapping("/{rentalId}/return")
    public ResponseEntity<RentalModel> returnRental(@PathVariable("rentalId") Long rentalId) {
        RentalModel rental = rentalService.returnRental(rentalId);
        
        return ResponseEntity.status(HttpStatus.OK).body(rental);
    }
    
}