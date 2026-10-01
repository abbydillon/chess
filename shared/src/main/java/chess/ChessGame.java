package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {

        board = new ChessBoard();
        board.resetBoard(); // reset for a new game
        teamTurn = TeamColor.WHITE; // the white side always starts first

//        System.out.println("starting turn: " + teamTurn);
//        System.out.println("piece at (1,1)" + board.getPiece(new ChessPosition(1,1)));

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
//        System.out.println("team turn changed to: " + teamTurn);
        return teamTurn;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        teamTurn = team;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) { // if there is no peice at this position
            return null;
        }

        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        // test each possible move
        for (ChessMove move : possibleMoves) {
            ChessPosition endPosition = move.getEndPosition();

            // save the piece that is at the ending position
            ChessPiece capturePiece = board.getPiece(endPosition);

            //make the move temporarily
            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);

            //the move is only valid if it doesnt leave the king in check
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }

            // reset the board to bacl before checking valid possible moves
            board.addPiece(startPosition, piece);
            board.addPiece(endPosition, capturePiece);

        }

//        System.out.println("Possible moves: " + validMoves.size());

        return validMoves;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece piece = board.getPiece(startPosition);

        // check that it's the team's turn
        if (piece.getTeamColor() != teamTurn) {
            throw new RuntimeException("Not the team's turn");
        }

        // move the piece
        board.addPiece(startPosition,null);
        board.addPiece(endPosition,piece);

        //switch to the other team's turn
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // find the location of the team's king
        ChessPosition kingPosition = null;

        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){

                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = position;
                }
            }
        }

        // go through other team's pieces and see if they can land on the king's spot
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){

                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMove = piece.pieceMoves(board, position);

                    for (ChessMove move : enemyMove) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }
}
