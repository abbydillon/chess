package server;

import dataaccess.*;
import io.javalin.*;
import service.*;
import com.google.gson.Gson;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        Clear clearing = new Clear(userDAO, authDAO, gameDAO);
        UserService userService  = new UserService(userDAO, authDAO);

        Gson serializer = new Gson();

        javalin.delete("/db", ctx -> {
            try {
                clearing.clear();
                ctx.status(200);
                ctx.result("{}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            }
        });

        javalin.post("/user", ctx -> {
            try {
                RegisterRequest registerRequest = serializer.fromJson(ctx.body(), RegisterRequest.class);
                RegisterResult registerResult = userService.register(registerRequest);

                ctx.status(200);
                ctx.result(serializer.toJson(registerResult));
            } catch (BadRequestException exception){
                ctx.status(400);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (AlreadyTakenException exception) {
                ctx.status(403);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
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
