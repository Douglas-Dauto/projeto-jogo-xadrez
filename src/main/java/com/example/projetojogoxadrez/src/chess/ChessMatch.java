package com.example.projetojogoxadrez.src.chess;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.chess.pieces.King;
import com.example.projetojogoxadrez.src.chess.pieces.Rook;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return mat;
    }

    private void placeNewPiece(int column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece(2, 6, new Rook(board, Color.WHITE));
        placeNewPiece(5, 8, new King(board, Color.BLACK));
        placeNewPiece(5, 1, new King(board, Color.WHITE));
    }
}
