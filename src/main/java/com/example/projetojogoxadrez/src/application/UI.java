package com.example.projetojogoxadrez.src.application;

import com.example.projetojogoxadrez.src.chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {
        for(int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");

            for(int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]);
            }

            System.out.println();
        }

        System.out.println("  1 2 3 4 5 6 7 8");
    }

    private static void printPiece(ChessPiece piece) {
        if(piece == null) {
            System.out.print("-");
        } else {
            System.out.print(piece);
        }

        System.out.print(" ");
    }
}
