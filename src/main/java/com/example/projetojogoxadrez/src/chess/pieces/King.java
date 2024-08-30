package com.example.projetojogoxadrez.src.chess.pieces;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.chess.ChessPiece;
import com.example.projetojogoxadrez.src.chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }
}
