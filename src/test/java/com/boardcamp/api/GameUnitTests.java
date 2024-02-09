package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.exceptions.GameNameConflictException;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.services.GameService;

@SpringBootTest
class GameUnitTests {
    @InjectMocks // quem recebe os mocks é a camada de Services nesse caso
	private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Test
    void givenRepeatedGame_whenCreatingGame_thenThrowsError() {
        GameDTO game = new GameDTO("Game", "link", 1L, 1L);
        doReturn(true).when(gameRepository).existsByName(any());

        GameNameConflictException exception = assertThrows(
            GameNameConflictException.class,
		    () -> gameService.save(game)
	    );

        verify(gameRepository, times(1)).existsByName(any());
	    verify(gameRepository, times(0)).save(any());
	    assertNotNull(exception);
	    assertEquals("Game already exists", exception.getMessage());
    }

    @Test
    void givenValidGame_whenCreatingGame_thenCreatesGame() {
        // given
        GameDTO game = new GameDTO("Game", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);

        doReturn(false).when(gameRepository).existsByName(any());
        doReturn(newGame).when(gameRepository).save(any());

        // when
        GameModel result = gameService.save(game);

        // then
        verify(gameRepository, times(1)).existsByName(any());
        verify(gameRepository, times(1)).save(any());
        assertEquals(newGame, result);
    }

    @Test
    void givenNone_whenFindingGames_thenReturnGameList() {
        // given
        GameDTO game = new GameDTO("Game", "link", 1L, 1L);
        GameModel newGame = new GameModel(game);
        List<GameModel> listGames = List.of(newGame);

        doReturn(listGames).when(gameRepository).findAll();

        // when
        List<GameModel> result = gameService.findAll();

        // then
        verify(gameRepository, times(1)).findAll();
        assertEquals(listGames, result);
    }
}
