package server;

import dataaccess.*;
import io.javalin.*;

import service.Clear;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        Clear clearing = new Clear(userDAO, authDAO, gameDAO);

        javalin.delete("/db", ctx -> {
            try {
                clearing.clear();
                ctx.status(200);
                ctx.result("{}");
            } catch (DataAccessException exc) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exc.getMessage() + "\"}");
            }
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
