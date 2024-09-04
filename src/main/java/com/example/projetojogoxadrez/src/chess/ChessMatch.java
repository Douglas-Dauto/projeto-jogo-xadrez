package com.example.projetojogoxadrez.src.chess;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.boardgame.Piece;
import com.example.projetojogoxadrez.src.boardgame.Position;
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

    public ChessPiece peformChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        Piece capturedPiece = makeMove(source, target);
        
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        return capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if(!board.thereIsAPiece(position)) {
            throw new ChessException("Não existe peça na posição de origem.");
        }
    }

    private void placeNewPiece(int column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece(3, 1, new Rook(board, Color.WHITE));
        placeNewPiece(3, 2, new Rook(board, Color.WHITE));
        placeNewPiece(4, 2, new Rook(board, Color.WHITE));
        placeNewPiece(5, 2, new Rook(board, Color.WHITE));
        placeNewPiece(5, 1, new Rook(board, Color.WHITE));
        placeNewPiece(4, 1, new King(board, Color.WHITE));

        placeNewPiece(3, 7, new Rook(board, Color.BLACK));
        placeNewPiece(3, 8, new Rook(board, Color.BLACK));
        placeNewPiece(4, 7, new Rook(board, Color.BLACK));
        placeNewPiece(5, 7, new Rook(board, Color.BLACK));
        placeNewPiece(5, 8, new Rook(board, Color.BLACK));
        placeNewPiece(4, 8, new King(board, Color.BLACK));
    }
}
