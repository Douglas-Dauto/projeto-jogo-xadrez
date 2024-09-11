package com.example.projetojogoxadrez.src.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.example.projetojogoxadrez.src.chess.ChessException;
import com.example.projetojogoxadrez.src.chess.ChessMatch;
import com.example.projetojogoxadrez.src.chess.ChessPiece;
import com.example.projetojogoxadrez.src.chess.ChessPosition;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(!chessMatch.getCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println();
                System.out.println("Digite a posição de origem: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.println("Digite a posição de destino: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.peformChessMove(source, target);

                if(capturedPiece != null) {
					captured.add(capturedPiece);
				}

                if(chessMatch.getPromoted() != null) {
					System.out.print("Entre com a peça a ser promovida (B/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();

					while(!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
						System.out.print("Valor inválido! Entre com a peça a ser promovida (B/N/R/Q): ");
						type = sc.nextLine().toUpperCase();
					}

					chessMatch.replacePromotedPiece(type);
				}
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
                sc.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }
}