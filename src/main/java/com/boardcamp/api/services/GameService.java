package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.exceptions.GameNameConflictException;
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
            throw new GameNameConflictException("Game already exists");
        }
        return gameRepository.save(game);
    }

    public List<GameModel> findAll(){
        return gameRepository.findAll();
    }
}
