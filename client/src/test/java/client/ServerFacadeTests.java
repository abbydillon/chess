package client;

import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeEach
    public void clearDatabase()  throws Exception {
        facade.clear();
    }
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new  ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess()  throws Exception {
        var result = facade.register("playerOne", "password", "playerEmail");

        assertNotNull(result);
        assertNotNull(result.authToken());
    }

    @Test
    public void failedRegister() throws Exception {
        facade.register("playerOne", "password", "playerEmail");

        assertThrows(Exception.class, () ->  facade.register("playerOne", "password", "playerEmail"));
    }

    @Test
    public void loginSuccess()  throws Exception {
        facade.register("playerOne", "password", "playerEmail");
        var result = facade.login("playerOne", "password");

        assertNotNull(result);
        assertNotNull(result.authToken());
    }

    @Test
    public void failedLogin() throws Exception {
        facade.register("playerOne", "password", "playerEmail");

        assertThrows(Exception.class, () ->  facade.login("playerOne", "wrong password"));
    }

    @Test
    public void createGameSuccess()  throws Exception {
       var authData = facade.register("playerOne", "password", "playerEmail");
        var result = facade.createGame(authData.authToken(), "test game");

        assertNotNull(result);
        assertTrue(result.gameID() > 0);
    }

    @Test
    public void failedGameCreation() {
        assertThrows(Exception.class, () ->  facade.createGame("bad auth token", "test game"));
    }

    @Test
    public void listGameSuccess()  throws Exception {
        var authData = facade.register("playerOne", "password", "playerEmail");
        facade.createGame(authData.authToken(), "test game one");
        facade.createGame(authData.authToken(), "test game two");
        var result = facade.listGames(authData.authToken());

        assertNotNull(result);
        assertEquals(2, result.games().size());
    }

    @Test
    public void failedListingGames() {
        assertThrows(Exception.class, () ->  facade.listGames("bad auth token"));
    }

    @Test
    public void joinGameSuccess()  throws Exception {
        var authData = facade.register("playerOne", "password", "playerEmail");
        var gameResult = facade.createGame(authData.authToken(), "test game");
        facade.joinGame(authData.authToken(), "WHITE", gameResult.gameID());
        var games = facade.listGames(authData.authToken());

        assertEquals("playerOne", games.games().iterator().next().whiteUsername());
    }

    @Test
    public void failedJoiningGames() throws Exception {
        var authData = facade.register("playerOne", "password", "playerEmail");
        var gameResult = facade.createGame(authData.authToken(), "test game");

       assertThrows(Exception.class, () ->  facade.joinGame(authData.authToken(), "NOT WHITE OR  BLACK", gameResult.gameID()));
    }

    @Test
    public void logoutSuccess()  throws Exception {
        var authData = facade.register("playerOne", "password", "playerEmail");
        facade.logoutRequest(authData.authToken());

        assertThrows(Exception.class, () -> facade.logoutRequest(authData.authToken()));
    }

    @Test
    public void failedLogout() {
        assertThrows(Exception.class, () ->  facade.logoutRequest("bad auth token"));
    }

}
