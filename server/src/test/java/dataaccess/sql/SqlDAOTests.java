package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import model.GameData;
import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlDAOTests {

    private SqlUserDAO userDAO;
    private SqlAuthDAO authDAO;
    private SqlGameDAO gameDAO;

    @BeforeEach
    public void setup() throws Exception {

        DatabaseManager.createTables();
        userDAO = new SqlUserDAO();
        userDAO.clear();

        authDAO = new SqlAuthDAO();
        authDAO.clear();

        gameDAO = new SqlGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createUserSuccess() throws Exception {
        UserData user = new UserData("testuser", "password", "testemail");

        userDAO.createUser(user);

        UserData storedUser = userDAO.getUser("testuser");

        assertNotNull(storedUser);
        assertEquals("testuser", storedUser.username());
        assertEquals("password", storedUser.password());
        assertEquals("testemail", storedUser.email());
    }

    @Test
    public void createDuplicateUser() throws Exception {
        UserData user = new UserData("testuser", "password", "testemail");

        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void getUserNotFound() throws Exception  {
        UserData user = userDAO.getUser("doesntexist");

        assertNull(user);
    }

    @Test
    public void createAuthSuccess() throws Exception {
        AuthData auth = new AuthData("testtoken",  "testuser");

        authDAO.createAuth(auth);

        AuthData storedAuth = authDAO.getAuth("testtoken");

        assertNotNull(storedAuth);
        assertEquals("testtoken", storedAuth.authToken());
        assertEquals("testuser", storedAuth.username());
    }

    @Test
    public void getAuthNotFound() throws Exception  {
        AuthData user = authDAO.getAuth("doesntexist");

        assertNull(user);
    }

    @Test
    public void  deleteAuthSuccess() throws Exception {
        AuthData  auth = new AuthData("testtoken", "testuser");

        authDAO.createAuth(auth);
        authDAO.deleteAuth("testtoken");
        assertNull(authDAO.getAuth("testtoken"));
    }

    @Test
    public void createDuplicateAuth() throws Exception {
        AuthData auth = new AuthData("testtoken", "testuser");

        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void createGameSuccess() throws Exception {
        GameData game = new GameData(0, null, null, "testgame", new ChessGame());

        int gameID = gameDAO.createGame(game);
        assertTrue(gameID > 0);
    }

    @Test public void getGameSuccess() throws Exception {
        GameData game = new GameData(0, null, null, "testgame", new ChessGame());
        int gameID = gameDAO.createGame(game); GameData storedGame = gameDAO.getGame(gameID);
        assertNotNull(storedGame); assertEquals(gameID,  storedGame.gameID()); assertEquals("testgame",  storedGame.gameName());
        assertNull(storedGame.whiteUsername());
        assertNull(storedGame.blackUsername()); assertNotNull(storedGame.game());
    }

    @Test public void getGameNotFound() throws Exception {
        GameData game = gameDAO.getGame(11111); assertNull(game);
    }

    @Test
    public void listGamesSuccess() throws Exception {
        gameDAO.createGame(new GameData(0, null, null, "game1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "game2", new ChessGame()));
        var games = gameDAO.listGames(); assertEquals(2, games.size());
    }

    @Test public void updateGameSuccess() throws Exception {
        GameData game = new GameData( 0, null, null, "testgame", new ChessGame());
        int gameID = gameDAO.createGame(game); GameData updatedGame  =  new GameData(gameID, "whiteplayer",
                "blackplayer", "updatedgame", new ChessGame());
        gameDAO.updateGame(updatedGame); GameData storedGame = gameDAO.getGame(gameID);
        assertEquals("whiteplayer", storedGame.whiteUsername());
        assertEquals("blackplayer", storedGame.blackUsername());
        assertEquals("updatedgame", storedGame.gameName());
    }

    @Test
    public void clearGamesSuccess() throws Exception  { gameDAO.createGame(new GameData(0, null, null,
            "game1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "game2", new ChessGame()));
        gameDAO.clear(); assertEquals(0, gameDAO.listGames().size());
    }
}
