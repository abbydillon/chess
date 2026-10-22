package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO  implements GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();
    private int gameNumCounter = 1;

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        int gameID = gameNumCounter;
        GameData newGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());

        games.put(gameID, newGame);
        gameNumCounter ++;
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }


    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        games.replace(gameData.gameID(), gameData);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        gameNumCounter = 1;
    }
}
