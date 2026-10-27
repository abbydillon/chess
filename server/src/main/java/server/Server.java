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
        GameService gameService = new GameService(authDAO, gameDAO);

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

        javalin.post("/session", ctx -> {
            try {
                LoginRequest loginRequest = serializer.fromJson(ctx.body(), LoginRequest.class);
                LoginResult loginResult = userService.login(loginRequest);

                ctx.status(200);
                ctx.result(serializer.toJson(loginResult));
            } catch (BadRequestException exception){
                ctx.status(400);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (IncorrectCredentialsException exception) {
                ctx.status(401);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            }
            });

        javalin.delete("/session", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                LogoutRequest logoutRequest = new LogoutRequest(authToken);
                userService.logout(logoutRequest);

                ctx.status(200);
                ctx.result("{}");
            } catch (IncorrectCredentialsException exception) {
                ctx.status(401);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            }
        });

        javalin.post("/game", ctx -> {
            try{
                String authToken = ctx.header("authorization");

                CreateGameRequest createGameRequest = serializer.fromJson(ctx.body(), CreateGameRequest.class);
                CreateGameResult createGameResult = gameService.createGame(authToken, createGameRequest);

                ctx.status(200);
                ctx.result(serializer.toJson(createGameResult));
            } catch (BadRequestException exception) {
                ctx.status(400);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (IncorrectCredentialsException exception) {
                ctx.status(401);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            }
        });


        javalin.put("/game", ctx -> {
            try{
                String authToken = ctx.header("authorization");

                JoinGameRequest joinGameRequest = serializer.fromJson(ctx.body(), JoinGameRequest.class);

                gameService.joinGame(authToken, joinGameRequest);

                ctx.status(200);
                ctx.result("{}");
            } catch (BadRequestException exception) {
                ctx.status(400);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (IncorrectCredentialsException exception) {
                ctx.status(401);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (ColorTakenException exception) {
                ctx.status(403);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            } catch (DataAccessException exception) {
                ctx.status(500);
                ctx.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
            }
        });


        javalin.get("/game", ctx -> {
            try{
                String authToken = ctx.header("authorization");

                ListGameResult listGameResult = gameService.listGames(authToken);

                ctx.status(200);
                ctx.result(serializer.toJson(listGameResult));
            } catch (IncorrectCredentialsException exception) {
                ctx.status(401);
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
