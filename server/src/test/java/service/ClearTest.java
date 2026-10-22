package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearTest {
    @Test
    public void clearData() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        userDAO.createUser(new UserData("testuser",
                "testpassword", "testemail"));

        authDAO.createAuth(new AuthData("testtoken", "testuser"));

        gameDAO.createGame(new GameData(0, null, null,
                "testGame", new ChessGame()));

        Clear clearing = new Clear(userDAO, authDAO, gameDAO);
        clearing.clear();

        assertNull(userDAO.getUser("testuser"));
        assertNull(authDAO.getAuth("testtoken"));
        assertEquals(0, gameDAO.listGames().size());
    }
}
