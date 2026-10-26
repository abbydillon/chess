package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;


public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService (UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    // Register:
    public RegisterResult register(RegisterRequest registerRequest)
            throws DataAccessException, AlreadyTakenException, BadRequestException {

        if (registerRequest.username() == null || registerRequest.password() == null
                || registerRequest.email() == null) {
            throw new BadRequestException("bad request");
        }

        UserData existingUser = userDAO.getUser(registerRequest.username());

        if (existingUser != null) { // username already taken
            throw new AlreadyTakenException("already taken");
        }

        UserData newUser = new UserData(registerRequest.username(),
                registerRequest.password(), registerRequest.email());
        userDAO.createUser(newUser);

        String authToken = generateToken();

        AuthData authData = new AuthData(authToken, registerRequest.username());

        authDAO.createAuth(authData);

        return new RegisterResult(registerRequest.username(), authToken);

    }

    // Login:
    public LoginResult login (LoginRequest loginRequest)
            throws DataAccessException, BadRequestException, IncorrectCredentialsException {

        UserData existingUser = userDAO.getUser(loginRequest.username());

        if (loginRequest.username() ==  null || loginRequest.password() ==  null) {
            throw new BadRequestException("bad request");
        }

        if (existingUser == null) { // username already taken
            throw new IncorrectCredentialsException("incorrect credentials");
        }

        if (!existingUser.password().equals(loginRequest.password())) { // username already taken
            throw new IncorrectCredentialsException("incorrect credentials");
        }

        String authToken = generateToken();

        AuthData authData = new AuthData(authToken, loginRequest.username());
        authDAO.createAuth(authData);

        return new LoginResult(loginRequest.username(), authToken);

    }

    // Logout:
    public void logout(LogoutRequest logoutRequest) throws DataAccessException, IncorrectCredentialsException{
        AuthData authData =  authDAO.getAuth(logoutRequest.authToken());

        if(authData == null) {
            throw new IncorrectCredentialsException("unauthorized");
        }

        authDAO.deleteAuth(logoutRequest.authToken());
    }

    public  static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
