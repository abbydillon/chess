package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
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

}
