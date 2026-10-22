package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryGameDAOTest {

    @Test
    public void createAndGetGame () throws DataAccessException {
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GameData game = new GameData(0, null,
                null, "testGame", new ChessGame());

        int gameID = gameDAO.createGame(game);

        GameData testResult = gameDAO.getGame(gameID);

        assertEquals(gameID, testResult.gameID());
        assertEquals("testGame", testResult.gameName());
    }

    @Test
    public void listGames() throws DataAccessException {

        MemoryGameDAO gameDAO = new MemoryGameDAO();
        GameData game1 = new GameData(
                0, null, null, "game1", new ChessGame()
        );

        GameData game2 = new GameData(
                0, null, null, "game2", new ChessGame()
        );

        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        assertEquals(2, gameDAO.listGames().size());

    }

    @Test
    public void updateGame() throws DataAccessException {
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GameData game = new GameData(
                0, null, null, "game", new ChessGame()
        );

        int gameID = gameDAO.createGame(game);

        GameData updatedGame = new GameData(gameID, "whitePlayer",
                null, "testGame", new ChessGame());

        gameDAO.updateGame(updatedGame);
        GameData testResult = gameDAO.getGame(gameID);

        assertEquals("testGame", testResult.gameName());
        assertEquals("whitePlayer", testResult.whiteUsername());
    }

    @Test
    public void clearGame() throws DataAccessException {

        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GameData game = new GameData(0, null,
                null, "testGame", new ChessGame());

        int gameID = gameDAO.createGame(game);

        gameDAO.clear();

        assertEquals(0, gameDAO.listGames().size());
        assertNull(gameDAO.getGame(gameID));
    }

}
