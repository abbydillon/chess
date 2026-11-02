package dataaccess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDatabaseConnection {

    @Test
    public void testDatabaseConnection() throws Exception {
        DatabaseManager.createDatabase();

        try (var connection = DatabaseManager.getConnection()) {
            assertNotNull(connection);
        }
    }
}
