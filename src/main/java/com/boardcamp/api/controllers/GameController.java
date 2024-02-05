package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.services.GameService;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/games")
public class GameController {
    final GameService gameService;

    GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameModel> save(@RequestBody @Valid GameDTO body) {
        GameModel game = gameService.save(body);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @GetMapping
    public ResponseEntity<List<GameModel>> getGames() {
        List<GameModel> game = gameService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(game);
    }
    
    
}