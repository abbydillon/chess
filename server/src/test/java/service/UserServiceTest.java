package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.IncorrectCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void registerUser() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest registerRequest =  new RegisterRequest("testuser", "testpassword", "testemail");

        RegisterResult registerResult = userService.register(registerRequest);

        assertEquals("testuser", registerResult.username());
        assertNotNull(registerResult.authToken());

        UserData storedUser = userDAO.getUser("testuser");
        assertNotNull(storedUser);

        AuthData storedAuth = authDAO.getAuth(registerResult.authToken());
        assertNotNull(storedAuth);
        assertEquals("testuser", storedAuth.username());
    }

    @Test
    public  void registerDuplicateUser() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest registerRequest =  new RegisterRequest("testuser", "testpassword", "testemail");

        userService.register(registerRequest);

        assertThrows(AlreadyTakenException.class, () -> userService.register(registerRequest));

    }

    @Test
    public void registerBadRequest() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest registerRequest =  new RegisterRequest(null, "testpassword", "testemail");

        assertThrows(BadRequestException.class, () -> userService.register(registerRequest));
    }

    @Test
    public void loginSuccess() throws Exception {

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        userDAO.createUser(new UserData("testuser", "testpassword", "testemail"));
        UserService userService = new UserService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("testuser", "testpassword");
        LoginResult loginResult = userService.login(loginRequest);

        assertEquals("testuser", loginResult.username());
        assertNotNull(loginResult.authToken());

        AuthData storedAuth = authDAO.getAuth(loginResult.authToken());
        assertNotNull(storedAuth);
        assertEquals("testuser", storedAuth.username());
    }

    @Test
    public void loginBadRequest() {

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest(null, "testpassword");

        assertThrows(BadRequestException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void loginIncorrectCredentials () throws Exception {

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        userDAO.createUser(new UserData("testuser", "testpassword", "testemail"));
        UserService userService = new UserService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("testuser", "testwrongpassword");

        assertThrows(IncorrectCredentialsException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void logoutSuccess() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);
        AuthData authData =  new AuthData("testtoken", "testuser");
        authDAO.createAuth(authData);

        LogoutRequest logoutRequest = new LogoutRequest("testtoken");
        userService.logout(logoutRequest);

        assertNull(authDAO.getAuth("testtoken"));
    }

    @Test
    public void logoutUnauthorized()  throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        LogoutRequest logoutRequest = new LogoutRequest("badtoken");

        assertThrows(IncorrectCredentialsException.class, ()  -> userService.logout(logoutRequest));
    }

}
