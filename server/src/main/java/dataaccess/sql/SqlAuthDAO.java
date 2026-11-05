package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    @Override
    public void  createAuth(AuthData authData) throws DataAccessException {
        String statement = """
                INSERT INTO auth (authToken, username)
                VALUES (?, ?)
                
                """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.setString(2, authData.username());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("failed to create auth", exception);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException  {

        String statement = """
                SELECT authToken, username
                FROM auth
                WHERE authToken = ?                
                """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);

            try (var results = preparedStatement.executeQuery()) {
                if(results.next()) {
                    return new AuthData(results.getString("authtoken"), results.getString("username"));
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new DataAccessException("failed to get auth", exception);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException  {

        String statement = """
               DELETE FROM auth
               WHERE authToken = ?                
               """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new DataAccessException("failed to delete auth", exception);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM auth";

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }  catch (SQLException exception) {
            throw new DataAccessException("failed to clear auths", exception);
        }
    }
}
