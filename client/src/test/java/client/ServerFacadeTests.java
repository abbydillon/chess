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
    public void FailedRegister() throws Exception {
        facade.register("playerOne", "password", "playerEmail");

        Assertions.assertThrows(Exception.class, () ->  facade.register("playerOne", "password", "playerEmail"));
    }

}
