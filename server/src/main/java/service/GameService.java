package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.GameData;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest)
            throws DataAccessException, BadRequestException, IncorrectCredentialsException {

        if (authToken == null || authDAO.getAuth(authToken) == null) {
            throw new IncorrectCredentialsException("unauthorized");
        }

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("bad request");
        }

        GameData gameData = new GameData(0, null, null,
                createGameRequest.gameName(), new ChessGame());

        int gameID = gameDAO.createGame(gameData);

        return new CreateGameResult(gameID);
    }
}
