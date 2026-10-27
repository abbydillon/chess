package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.exception.BadRequestException;
import service.exception.ColorTakenException;
import service.exception.IncorrectCredentialsException;

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

    public  void  joinGame(String authToken, JoinGameRequest joinGameRequest)
    throws DataAccessException, BadRequestException, IncorrectCredentialsException, ColorTakenException {

        AuthData authData  = authDAO.getAuth(authToken);

        if (authToken == null || authData == null) {
            throw new IncorrectCredentialsException("unauthorized");
        }

        if (joinGameRequest.playerColor() == null || (!joinGameRequest.playerColor().equals("WHITE")
                && !joinGameRequest.playerColor().equals("BLACK"))) {
            throw new BadRequestException("bad request");
        }

        GameData gameData = gameDAO.getGame(joinGameRequest.gameID());

        if (gameData == null) {
            throw new BadRequestException("bad request");
        }

        if(joinGameRequest.playerColor().equals("WHITE")) {
            if(gameData.whiteUsername()  != null)  {
                throw new ColorTakenException("already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), authData.username(),
                    gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(updatedGame);

        } else {
            if (gameData.blackUsername() != null) {
                throw new ColorTakenException("already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    authData.username(), gameData.gameName(), gameData.game());

            gameDAO.updateGame(updatedGame);
        }
    }

    public ListGameResult listGames(String authToken)
            throws DataAccessException, IncorrectCredentialsException {
        if (authToken == null || authDAO.getAuth(authToken) == null) {
            throw new IncorrectCredentialsException("unauthorized");
        }

        return new ListGameResult(gameDAO.listGames());
    }
}
