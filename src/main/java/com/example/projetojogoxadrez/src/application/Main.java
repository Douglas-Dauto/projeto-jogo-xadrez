package com.example.projetojogoxadrez.src.application;

import com.example.projetojogoxadrez.src.chess.ChessMatch;

public class Main {
    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getPieces());
    }
}