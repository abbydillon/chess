package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import  chess.ChessGame;
import service.exception.BadRequestException;
import service.exception.ColorTakenException;
import service.exception.IncorrectCredentialsException;

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

    @Test
    public void joinGameSuccess() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        int gameID = gameDAO.createGame(new GameData(0,null,null, "testgame", new ChessGame()));

        GameService gameService = new GameService(authDAO, gameDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);

        gameService.joinGame("testtoken", joinGameRequest);

        GameData updatedGame = gameDAO.getGame(gameID);

        assertEquals("testuser", updatedGame.whiteUsername());
    }

    @Test
    public void joinGameUnauthorized() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        int gameID = gameDAO.createGame(new GameData(0,null,null, "testgame", new ChessGame()));

        GameService gameService = new GameService(authDAO, gameDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);

        assertThrows(IncorrectCredentialsException.class, () -> gameService.joinGame("badtoken", joinGameRequest));
    }

    @Test
    public void joinGameBadColor() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        int gameID = gameDAO.createGame(new GameData(0,null,null, "testgame", new ChessGame()));

        GameService gameService = new GameService(authDAO, gameDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("BLUE", gameID); // test a wrong color

        assertThrows(BadRequestException.class, () -> gameService.joinGame("testtoken", joinGameRequest));
    }

    @Test
    public void joinGameTakenColor() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        int gameID = gameDAO.createGame(new GameData(0,"other user",null, "testgame", new ChessGame()));

        GameService gameService = new GameService(authDAO, gameDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);

        assertThrows(ColorTakenException.class, () -> gameService.joinGame("testtoken", joinGameRequest));

    }

    @Test
    public void joinGameBadGameID() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        GameService gameService = new GameService(authDAO, gameDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1111); // a wrong game iD

        assertThrows(BadRequestException.class, () -> gameService.joinGame("testtoken", joinGameRequest));
    }

    @Test
    public void listGames() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        GameService gameService = new GameService(authDAO, gameDAO);

        ListGameResult result = gameService.listGames("testtoken");
        assertNotNull(result);
        assertEquals(0, result.games().size());
    }

    @Test
    public void listGamesSuccess() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        gameDAO.createGame(new GameData(0, null, null, "game 1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "game 2", new ChessGame()));

        GameService gameService = new GameService(authDAO, gameDAO);

        ListGameResult result = gameService.listGames("testtoken");
        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesUnauthorized() throws Exception  {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GameService gameService = new GameService(authDAO, gameDAO);

        assertThrows(IncorrectCredentialsException.class, () -> gameService.listGames("badtoken"));
    }

}
