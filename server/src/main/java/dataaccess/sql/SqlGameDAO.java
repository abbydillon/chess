package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import java.util.Collection;

public class SqlGameDAO implements GameDAO {

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        return  0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException  {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException  {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException  {}

    @Override
    public void clear() throws DataAccessException {}
}
