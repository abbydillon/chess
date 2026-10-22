package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryAuthDAOTest {

    @Test
    public void createAndGetAuth () throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        AuthData auth = new AuthData("testToken", "testuser");

        authDAO.createAuth(auth);
        AuthData testResult = authDAO.getAuth("testToken");
        assertEquals(auth, testResult);
    }

    @Test
    public void deleteAuth () throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        AuthData auth = new AuthData("testToken", "testuser");

        authDAO.createAuth(auth);
        authDAO.deleteAuth("testToken");
        assertNull(authDAO.getAuth("testToken"));
    }
}
