package client;

import org.junit.jupiter.api.*;
import server.Server;


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

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void failedRegister() throws Exception {
        facade.register("playerOne", "password", "playerEmail");

        Assertions.assertThrows(Exception.class, () ->  facade.register("playerOne", "password", "playerEmail"));
    }

    @Test
    public void loginSuccess()  throws Exception {
        facade.register("playerOne", "password", "playerEmail");
        var result = facade.login("playerOne", "password");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void failedLogin() throws Exception {
        facade.register("playerOne", "password", "playerEmail");

        Assertions.assertThrows(Exception.class, () ->  facade.login("playerOne", "wrong password"));
    }

    @Test
    public void createGameSuccess()  throws Exception {
       var authData = facade.register("playerOne", "password", "playerEmail");
        var result = facade.createGame(authData.authToken(), "test game");

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.gameID() > 0);
    }

    @Test
    public void failedGameCreation() {
        Assertions.assertThrows(Exception.class, () ->  facade.createGame("bad auth token", "test game"));
    }

}
