package client;

import chess.*;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        ClientMain client = new ClientMain(8080);
        client.run();
    }

    private String authToken;
    private List<GameData> games =  new ArrayList<>();

    public  ClientMain(int port) {
        facade = new ServerFacade(port);
    }

    private enum State {
        SIGNEDOUT,
        SIGNEDIN,
        QUIT
    }

    private State state = State.SIGNEDOUT; //  default state
    private final Scanner scanner  = new Scanner(System.in);
    private final ServerFacade facade;


    public void run() {

        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            if (state == State.SIGNEDOUT) {
                handlePreLogin(input);
            } else { // signed in
                handlePostLogin(input);
            }
        }
    }

    private void handlePreLogin (String input) {
        if(input.trim().isEmpty()) {
            return;
        }
        String[] loginPieces = input.trim().split("\\s+");
        switch(loginPieces[0].toLowerCase()) {
            case "help" -> printPreLoginHelp();
            case "quit" -> state = State.QUIT;
            case "login"  -> login(loginPieces);
            case "register"  -> register(loginPieces);
            default -> System.out.println("Command not recognized. Try again.");
        }
    }

    private void printPreLoginHelp() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - stop playing chess");
        System.out.println("help - with  possible commands");
    }

    private void  login (String[] loginPieces) {
        if(loginPieces.length != 3) {
            System.out.println("Usage: login <USERNAME> <PASSWORD>");
            return;
        }
        try {
            AuthData result = facade.login(loginPieces[1], loginPieces[2]);

            authToken = result.authToken();
            state  = State.SIGNEDIN;
            System.out.println("login worked!");
        } catch (Exception exception) {
            System.out.println("login did not work!");
        }
    }

    private void  register (String[] loginPieces) {
        if(loginPieces.length != 4) {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            return;
        }
        try {
            AuthData result = facade.register(loginPieces[1], loginPieces[2], loginPieces[3]);

            authToken = result.authToken();
            state  = State.SIGNEDIN;
            System.out.println("registration worked! you are now logged in");
        } catch (Exception exception) {
            System.out.println("registration did not work!");
        }
    }

    private void handlePostLogin (String input) {
        if(input.trim().isEmpty()) {
            return;
        }
        String[] loginPieces = input.trim().split("\\s+");
        switch(loginPieces[0].toLowerCase()) {
            case "create"  -> createGame(loginPieces);
            case "list"  -> listGames();
            case "join"  -> joinGame(loginPieces);
            case "observe"  -> observeGame(loginPieces);
            case "logout"  -> logout();
            case "quit" -> state = State.QUIT;
            case "help" -> printPostLoginHelp();
            default -> System.out.println("Command not recognized. Try again.");
        }
    }

    private void printPostLoginHelp() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE | BLACK] - a game");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("quit - stop playing chess");
        System.out.println("help - with possible commands");
    }
    
    private void createGame (String[] loginPieces) {
        if(loginPieces.length != 2) {
            System.out.println("Usage: create <NAME>");
            return;
        }
        try {
            facade.createGame(authToken, loginPieces[1]);
            System.out.println("game created!");
        } catch (Exception exception) {
            System.out.println("game was not created!");
        }
    }

    private void  listGames () {
        try {
            var result = facade.listGames(authToken);
            games = new ArrayList<>(result.games());

            if(games.isEmpty()) {
                System.out.println("There are no games");
                return;
            }
            for (int i = 0; i < games.size(); i++) {
                GameData game = games.get(i);
                System.out.println((i+1) + " "  + game.gameName() + " White: " +
                        game.whiteUsername() + " Black: " + game.blackUsername());
            }
        } catch (Exception exception) {
            System.out.println("Not able to list games");
        }
    }

    private void joinGame(String[] loginPieces) {
        if(loginPieces.length != 3) {
            System.out.println("Usage:  join <ID> <WHITE | BLACK>");
            return;
        }
        try {
            int gameNumber = Integer.parseInt(loginPieces[1]);

            if  (gameNumber <  1 ||  gameNumber > games.size()) {
                System.out.println("Not a valid game number");
                return;
            }

            String color = loginPieces[2].toUpperCase();
            if(!color.equals("WHITE") && !color.equals("BLACK")) {
                System.out.println("Color has to be WHITE or BLACK");
                return;
            }

            GameData game = games.get(gameNumber - 1);
            facade.joinGame(authToken, color, game.gameID());

            System.out.println("Successfully joined the game!");
        } catch (NumberFormatException exception) {
            System.out.println("The input for the game number must be a number");
        } catch (Exception exception) {
            System.out.println("Unable to join the game!");
        }
    }

    private void observeGame(String[] loginPieces) {
        if(loginPieces.length != 2) {
            System.out.println("Usage: observe <ID>");
            return;
        }
        try {
            int gameNumber = Integer.parseInt(loginPieces[1]);

            GameData game = games.get(gameNumber - 1);

            System.out.println("Observing Game: "  + game.gameName());
        }  catch (NumberFormatException exception) {
            System.out.println("The input for the game number must be a number");
        } catch (Exception exception) {
            System.out.println("Unable to observe the game!");
        }


    }

    private void logout() {
        try {
            facade.logoutRequest(authToken);
            authToken = null;
            state  = State.SIGNEDOUT;
            System.out.println("logout worked!");
        } catch (Exception exception) {
            System.out.println("logout did not work!");
        }
    }

}
