package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlDAOTests {

    private SqlUserDAO userDAO;

    @BeforeEach
    public void setup() throws Exception {

        DatabaseManager.createTables();
        userDAO = new SqlUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserSuccess() throws Exception {
        UserData user = new UserData("testuser", "password", "testemail");

        userDAO.createUser(user);

        UserData storedUser = userDAO.getUser("testuser");

        assertNotNull(storedUser);
        assertEquals("testuser", storedUser.username());
        assertEquals("password", storedUser.password());
        assertEquals("testemail", storedUser.email());
    }

    @Test
    public void createDuplicateUser() throws Exception {
        UserData user = new UserData("testuser", "password", "testemail");

        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void getUserNotFound() throws Exception  {
        UserData user = userDAO.getUser("doesntexist");

        assertNull(user);
    }

}
