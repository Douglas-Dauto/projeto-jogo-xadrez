package com.example.projetojogoxadrez.src.chess.pieces;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.chess.ChessPiece;
import com.example.projetojogoxadrez.src.chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }
}
