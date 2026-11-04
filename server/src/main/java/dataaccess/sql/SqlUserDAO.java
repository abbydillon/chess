package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import java.sql.SQLException;

public class SqlUserDAO implements UserDAO {

    @Override
    public void  createUser(UserData userData) throws DataAccessException {
        String statement = """
                INSERT INTO user (username, password, email)
                VALUES (?,?,?)
                
                """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, userData.password());
            preparedStatement.setString(3, userData.email());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("failed to create user", exception);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException  {

        String statement = """
                SELECT username, password, email
                FROM user
                WHERE username = ?                
                """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, username);

            try (var results = preparedStatement.executeQuery()) {
                if(results.next()) {
                    return new UserData(results.getString("username"),
                            results.getString("password"),
                            results.getString("email"));
                }
            }
        return null;
    } catch (SQLException exception) {
            throw new DataAccessException("failed to create user", exception);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM user";

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }  catch (SQLException exception) {
            throw new DataAccessException("failed to clear users", exception);
        }
    }

}
