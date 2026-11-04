package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class SqlUserDAO implements UserDAO {

    @Override
    public void  createUser(UserData userData) throws DataAccessException {}

    @Override
    public UserData getUser(String username) throws DataAccessException  {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {}

}
