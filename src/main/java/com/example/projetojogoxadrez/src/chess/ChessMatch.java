package com.example.projetojogoxadrez.src.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.boardgame.Piece;
import com.example.projetojogoxadrez.src.boardgame.Position;
import com.example.projetojogoxadrez.src.chess.pieces.King;
import com.example.projetojogoxadrez.src.chess.pieces.Rook;

public class ChessMatch {
    private Integer turn;
    private Color currentPlayer;
    private Board board;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
    private Boolean check;

    public Integer getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.BRANCO;
        check = false;
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }

    public ChessPiece peformChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        
        return (ChessPiece) capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        Piece p = board.removePiece(target);
        board.placePiece(p, source);

        if(capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

        return capturedPiece;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.BRANCO)?Color.AMARELO:Color.BRANCO;
    }

    private Color opponent(Color color) {
        return (color == Color.BRANCO)?Color.AMARELO:Color.BRANCO;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

        for(Piece p : list) {
            if(p instanceof King) {
                return (ChessPiece) p;
            }
        }

        throw new IllegalStateException("Não existe o rei " + color + " no tabuleiro.");
    }

    private void validateSourcePosition(Position position) {
        if(!board.thereIsAPiece(position)) {
            throw new ChessException("Não existe peça na posição de origem.");
        }

        if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua.");
        }

        if(!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existe movimentos possiveis para a peça escolhida.");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if(!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino.");
        }
    }

    private void placeNewPiece(int column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece(3, 1, new Rook(board, Color.BRANCO));
        placeNewPiece(3, 2, new Rook(board, Color.BRANCO));
        placeNewPiece(4, 2, new Rook(board, Color.BRANCO));
        placeNewPiece(5, 2, new Rook(board, Color.BRANCO));
        placeNewPiece(5, 1, new Rook(board, Color.BRANCO));
        placeNewPiece(4, 1, new King(board, Color.BRANCO));

        placeNewPiece(3, 7, new Rook(board, Color.AMARELO));
        placeNewPiece(3, 8, new Rook(board, Color.AMARELO));
        placeNewPiece(4, 7, new Rook(board, Color.AMARELO));
        placeNewPiece(5, 7, new Rook(board, Color.AMARELO));
        placeNewPiece(5, 8, new Rook(board, Color.AMARELO));
        placeNewPiece(4, 8, new King(board, Color.AMARELO));
    }
}
