package ui;

import chess.*;

public class CreateBoard {

    public void drawWhiteBoard(ChessBoard board) {
        System.out.println("   a b c d e f g h");

        for (int row = 8; row >=1; row--) {
            System.out.print(row + " ");

            for (int col = 1; col <= 8; col++) {
                printBoard(board, row, col);
            }
            System.out.println(" " + row);
        }
        System.out.println("   a b c d e f g h");
    }

    public void drawBlackBoard(ChessBoard board) {
        System.out.println("   h g f e d c b a");

        for (int row = 1; row <=8; row++) {
            System.out.print(row + " ");

            for (int col = 8; col >= 1; col--) {
                printBoard(board, row, col);
            }
            System.out.println(" " + row);
        }
        System.out.println("   h g f e d c b a");
    }

    public void printBoard(ChessBoard board,  int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);

        if((row + col) % 2 == 0) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        }

        if(piece !=  null) {
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            }
        }
        System.out.print(getPieceCharacter(piece));

        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private String getPieceCharacter(ChessPiece piece) {
        if (piece == null) {
            return " \u2003 ";
        }
        return switch (piece.getPieceType()) {
            case KING -> " K ";
            case QUEEN -> " Q ";
            case BISHOP -> " B ";
            case KNIGHT -> " N ";
            case ROOK -> " R ";
            case PAWN -> " P ";
        };
    }
}
