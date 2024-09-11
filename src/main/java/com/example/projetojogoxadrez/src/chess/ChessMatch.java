package com.example.projetojogoxadrez.src.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.boardgame.Piece;
import com.example.projetojogoxadrez.src.boardgame.Position;
import com.example.projetojogoxadrez.src.chess.pieces.Bishop;
import com.example.projetojogoxadrez.src.chess.pieces.King;
import com.example.projetojogoxadrez.src.chess.pieces.Knight;
import com.example.projetojogoxadrez.src.chess.pieces.Pawn;
import com.example.projetojogoxadrez.src.chess.pieces.Queen;
import com.example.projetojogoxadrez.src.chess.pieces.Rook;

public class ChessMatch {
    private Integer turn;
    private Color currentPlayer;
    private Board board;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
    private Boolean check;
    private Boolean checkMate = false;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public Integer getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Boolean getCheck() {
        return check;
    }

    public Boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

    public ChessPiece getPromoted() {
		return promoted;
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

        if(testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em xeque.");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);
		
		// movimento especial promoção
		promoted = null;

		if(movedPiece instanceof Pawn) {
			if((movedPiece.getColor() == Color.BRANCO && target.getRow() == 0) || (movedPiece.getColor() == Color.AMARELO && target.getRow() == 7)) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}

        check = (testCheck(opponent(currentPlayer)));

        if(testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // movimento especial em passante
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() -2 || target.getRow() == source.getRow() +2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}
        
        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida.");
		}
        
		if(!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			return promoted;
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B")) return new Bishop(board, color);
		if(type.equals("N")) return new Knight(board, color);
		if(type.equals("Q")) return new Queen(board, color);

		return new Rook(board, color);
	}

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if(capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // movimento especial roque e do rei
		if(p instanceof King && target.getColumn() == source.getColumn() +2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() +3);
			Position targetT = new Position(source.getRow(), source.getColumn() +1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// movimento especial roque e da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() -4);
			Position targetT = new Position(source.getRow(), source.getColumn() -1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// movimento especial em passante
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnPosition;

				if(p.getColor() == Color.BRANCO) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn());
				}

				board.placePiece(pawn, pawnPosition);
			}
		}
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

        // movimento especial roque e do rei
		if(p instanceof King && target.getColumn() == source.getColumn() +2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() +3);
			Position targetT = new Position(source.getRow(), source.getColumn() +1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// movimento especial roque e da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() -4);
			Position targetT = new Position(source.getRow(), source.getColumn() -1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}		
		
		// movimento especial em passante
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;

				if(p.getColor() == Color.BRANCO) {
					pawnPosition = new Position(target.getRow() +1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() -1, target.getColumn());
				}

				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
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

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());

        for(Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();

            if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}

		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

		for(Piece p : list) {
			boolean[][] mat = p.possibleMoves();

			for(int i = 0; i < board.getRows(); i++) {
				for(int j = 0; j < board.getColumns(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);

						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}

		return true;
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
        placeNewPiece(1, 1, new Rook(board, Color.BRANCO));
        placeNewPiece(2, 1, new Knight(board, Color.BRANCO));
        placeNewPiece(3, 1, new Bishop(board, Color.BRANCO));
        placeNewPiece(4, 1, new Queen(board, Color.BRANCO));
        placeNewPiece(5, 1, new King(board, Color.BRANCO, this));
        placeNewPiece(6, 1, new Bishop(board, Color.BRANCO));
        placeNewPiece(7, 1, new Knight(board, Color.BRANCO));
        placeNewPiece(8, 1, new Rook(board, Color.BRANCO));
        placeNewPiece(1, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(2, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(3, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(4, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(5, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(6, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(7, 2, new Pawn(board, Color.BRANCO, this));
        placeNewPiece(8, 2, new Pawn(board, Color.BRANCO, this));

        placeNewPiece(1, 8, new Rook(board, Color.AMARELO));
        placeNewPiece(2, 8, new Knight(board, Color.AMARELO));
        placeNewPiece(3, 8, new Bishop(board, Color.AMARELO));
        placeNewPiece(4, 8, new Queen(board, Color.AMARELO));
        placeNewPiece(5, 8, new King(board, Color.AMARELO, this));
        placeNewPiece(6, 8, new Bishop(board, Color.AMARELO));
        placeNewPiece(7, 8, new Knight(board, Color.AMARELO));
        placeNewPiece(8, 8, new Rook(board, Color.AMARELO));
        placeNewPiece(1, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(2, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(3, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(4, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(5, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(6, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(7, 7, new Pawn(board, Color.AMARELO, this));
        placeNewPiece(8, 7, new Pawn(board, Color.AMARELO, this));
    }
}
