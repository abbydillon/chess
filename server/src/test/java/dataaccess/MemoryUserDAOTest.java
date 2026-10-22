package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryUserDAOTest {
    @Test

    public void createAndGetUser() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();

        UserData user = new UserData(
                "testuser", "passwordtest", "userEmail"
        );

        userDAO.createUser(user);
        UserData testResult = userDAO.getUser("testuser");

        assertEquals(user, testResult);
    }

    @ Test

    public void clearUsers() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData user = new UserData(
                "testuser", "passwordtest", "userEmail"
        );

        userDAO.createUser(user);
        userDAO.clear();

        assertNull(userDAO.getUser("testuser"));
    }
}
