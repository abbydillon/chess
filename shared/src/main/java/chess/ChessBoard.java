package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        board[position.getRow()-1][position.getColumn()-1] = piece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return board[position.getRow()-1][position.getColumn()-1];
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        board = new ChessPiece[8][8];

        //create pawns on the board
        for (int col = 1; col <= 8; col++) {
            addPiece(
                new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN)
            );

            addPiece(
                    new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN)
            );
//        }

        //throw new RuntimeException("Not implemented");
    }
}
