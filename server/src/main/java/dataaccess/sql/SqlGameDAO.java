package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Statement;

public class SqlGameDAO implements GameDAO {

    private final  Gson serializer = new Gson();

    @Override
    public int createGame(GameData gameData) throws DataAccessException {

        String statement = """
                INSERT INTO game (whiteUsername, blackUsername, gameName, game)
                VALUES (?, ?, ?, ?)
                """;

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, gameData.whiteUsername());
            preparedStatement.setString(2, gameData.blackUsername());
            preparedStatement.setString(3, gameData.gameName());
            preparedStatement.setString(4, serializer.toJson(gameData.game()));

            preparedStatement.executeUpdate();

            try(var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next())  {
                    return generatedKeys.getInt(1);
                }
            }
            throw new DataAccessException("failed to retrieve the gameID");
        } catch (SQLException exception) {
            throw new DataAccessException("failed to create game", exception);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM game
                WHERE gameID = ?                
                """;

        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, gameID);

            try (var results = preparedStatement.executeQuery()) {
                if (results.next()) {
                    ChessGame chessGame = serializer.fromJson(results.getString("game"), ChessGame.class);
                    return new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                            results.getString("blackUsername"), results.getString("gameName"), chessGame);
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new DataAccessException("failed to get game", exception);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException  {
        String statement = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM game
                """;

        Collection<GameData> games = new ArrayList<>();

        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement(statement);
             var results = preparedStatement.executeQuery()) {

            while (results.next()) {
                ChessGame chessGame = serializer.fromJson(results.getString("game"), ChessGame.class);
                games.add(new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                        results.getString("blackUsername"), results.getString("gameName"), chessGame));
            }
            return games;
        } catch (SQLException exception) {
            throw new DataAccessException("failed to list games", exception);
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException  {

        String statement = """
                UPDATE game
                SET whiteUsername = ?,
                    blackUsername = ?, 
                    gameName = ?, 
                    game = ?
                WHERE gameID = ?
                """;

        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement(statement)) {

             preparedStatement.setString(1, gameData.whiteUsername());
             preparedStatement.setString(2, gameData.blackUsername());
             preparedStatement.setString(3, gameData.gameName());
             preparedStatement.setString(4, serializer.toJson(gameData.game()));
             preparedStatement.setInt(5, gameData.gameID());
             preparedStatement.executeUpdate();

    } catch (SQLException exception) {
            throw new DataAccessException("failed to update games", exception);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM game";

        try(var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }  catch (SQLException exception) {
            throw new DataAccessException("failed to clear games", exception);
        }
    }
}
