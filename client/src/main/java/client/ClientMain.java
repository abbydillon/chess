package client;

import chess.*;
import model.AuthData;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        ClientMain client = new ClientMain(8080);
        client.run();
    }

    private String authToken;

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
            } else {
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
            System.out.print(exception.getMessage());
        }
    }

    private void handlePostLogin (String input) {

    }

}
