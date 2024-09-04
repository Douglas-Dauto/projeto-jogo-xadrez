package com.example.projetojogoxadrez.src.application;

import java.util.Scanner;

import com.example.projetojogoxadrez.src.chess.ChessMatch;
import com.example.projetojogoxadrez.src.chess.ChessPiece;
import com.example.projetojogoxadrez.src.chess.ChessPosition;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while(true) { 
            UI.printBoard(chessMatch.getPieces());
            System.out.println();
            System.out.println("Digite a posição de origem: ");
            ChessPosition source = UI.readChessPosition(sc);

            System.out.println();
            System.out.println("Digite a posição de destino: ");
            ChessPosition target = UI.readChessPosition(sc);

            ChessPiece capturedPiece = chessMatch.peformChessMove(source, target);
        }
    }
}