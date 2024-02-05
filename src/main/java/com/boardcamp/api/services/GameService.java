package com.boardcamp.api.services;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.exceptions.GameConflictException;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;

@Service
public class GameService {
    final GameRepository gameRepository;
    
    GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public GameModel save(GameDTO dto){
        GameModel game = new GameModel(dto);
        if( gameRepository.existsByName(game.getName())){
            throw new GameConflictException("Game already exists");
        }
        return gameRepository.save(game);
    }
}
