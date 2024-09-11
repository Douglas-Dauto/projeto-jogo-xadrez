package com.example.projetojogoxadrez.src.chess;

import com.example.projetojogoxadrez.src.boardgame.Board;
import com.example.projetojogoxadrez.src.boardgame.Piece;
import com.example.projetojogoxadrez.src.boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private Integer moveCount = 0;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
		return moveCount;
	}
	
	protected void increaseMoveCount() {
		moveCount++;
	}

	protected void decreaseMoveCount() {
		moveCount--;
	}

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);

        return p != null && p.getColor() != color;
    }
}
