package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    public void createGameSuccess() throws Exception {

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));
        GameService gameService = new GameService(authDAO, gameDAO);

        CreateGameRequest createGameRequest = new CreateGameRequest("Test Game");
        CreateGameResult createGameResult = gameService.createGame("testtoken",  createGameRequest);

        assertNotNull(createGameResult);
        assertTrue(createGameResult.gameID() > 0);

        GameData storedGame = gameDAO.getGame(createGameResult.gameID());
        assertNotNull(storedGame);
        assertEquals("Test Game", storedGame.gameName());
    }

    @Test
    public void createGameUnauthorized() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GameService gameService = new GameService(authDAO, gameDAO);

        CreateGameRequest createGameRequest = new CreateGameRequest("Test Game");

        assertThrows(IncorrectCredentialsException.class, () -> gameService.createGame("badtoken", createGameRequest));
    }

    @Test
    public void createGameBadRequest() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        GameService gameService = new GameService(authDAO, gameDAO);

        CreateGameRequest createGameRequest = new CreateGameRequest(null);

        assertThrows(BadRequestException.class, () -> gameService.createGame("testtoken", createGameRequest));
    }
}
