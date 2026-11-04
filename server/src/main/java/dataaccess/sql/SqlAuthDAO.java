package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class SqlAuthDAO implements AuthDAO {

    @Override
    public void  createAuth(AuthData authData) throws DataAccessException {}

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException  {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException  {}

    @Override
    public void clear() throws DataAccessException {}
}
