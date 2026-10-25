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

    public RegisterResult register(RegisterRequest registerRequest)
            throws DataAccessException, AlreadyTakenException, BadRequestException {

        UserData existingUser = userDAO.getUser(registerRequest.username());

        if (existingUser != null) { // username already taken
            throw new AlreadyTakenException("already taken");
        }

        if (registerRequest.username() == null || registerRequest.password() == null
                || registerRequest.email() == null) {
            throw new BadRequestException("bad request");
        }

        UserData newUser = new UserData(registerRequest.username(),
                registerRequest.password(), registerRequest.email());
        userDAO.createUser(newUser);

        String authToken = generateToken();

        AuthData authData = new AuthData(authToken, registerRequest.username());

        authDAO.createAuth(authData);

        return new RegisterResult(registerRequest.username(), authToken);

    }

    public  static String generateToken() {
        return UUID.randomUUID().toString();
    }

//    public LoginResult login(LoginRequest loginRequest) {return null}
//    public void logout(LogoutRequest logoutRequest) {}
}
