package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
        //throw new RuntimeException("Not implemented");
    }

    private boolean moveIsOnBoard(int row, int col) {
        return (row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }

    private boolean pieceCanMoveTo(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        return piece == null || piece.getTeamColor() != pieceColor;
    }

    private void addDirectionalMoves (ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int [][] directions) {

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];

            while (moveIsOnBoard(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; //stop once there isn't another spot to move to
                }
                newRow += direction[0];
                newCol += direction[1];
            }
        }

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = new ArrayList<>();

        if (type == PieceType.KING) {  // can move 1 square in any direction
            for (int rowChange = -1; rowChange <= 1; rowChange++) {
                for (int colChange = -1; colChange <= 1; colChange++) {
                    if (rowChange == 0 && colChange == 0) continue;

                    int newRow = myPosition.getRow() + rowChange;
                    int newCol = myPosition.getColumn() + colChange;

                    if (!moveIsOnBoard(newRow,newCol)) continue;

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    if(pieceCanMoveTo(board, newPosition)) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }

                }
//
            }
        }

        if (type == PieceType.KNIGHT) { // moves over 2 squares in 1 direction and 1 square in the other
            int [][] knightMoves = {
                    {2,1}, {2,-1}, {-2,1}, {-2,-1}, {-1,2}, {-1,-2}, {1,2}, {1,-2}
            };

            for (int[] move : knightMoves) {
                int newRow = myPosition.getRow() + move[0];
                int newCol = myPosition.getColumn() + move[1];

                if (!moveIsOnBoard(newRow, newCol)) continue;

                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (pieceCanMoveTo(board, newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        if (type == PieceType.ROOK) { // moves straight or sideways as many spots
            int [][] rookDirections = {
                    {1,0},
                    {-1,0},
                    {0,1},
                    {0,-1}
            };
            //rook can keep moving after one open space
            addDirectionalMoves(board, myPosition, moves, rookDirections);
        }

        if (type == PieceType.BISHOP) { // moves any number of spaces diagonally
            int [][] bishopDirections = {
                    {1,1},
                    {1,-1},
                    {-1,-1},
                    {-1,1}
            };

            addDirectionalMoves(board, myPosition, moves, bishopDirections);

        }

        if (type == PieceType.QUEEN) {
            int [][] queenDirections = {
                    {1,0},
                    {1,1},
                    {1,-1},
                    {0,1},
                    {0,-1},
                    {-1,-1},
                    {-1,0},
                    {-1,1}
            };
            addDirectionalMoves(board, myPosition, moves, queenDirections);
        }

        if (type == PieceType.PAWN) {
            int side;
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                side = 1;
            } else {
                side = -1;
            }

        // code to move pawns 1 square forwards
        int newRow = myPosition.getRow() + side;
        int col = myPosition.getColumn();
        if (moveIsOnBoard(newRow,col)) {
            ChessPosition forwardPosition = new ChessPosition(newRow,col);

            if (board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(myPosition,forwardPosition, null));
            }
        }

        }

        return moves;
    }
}
