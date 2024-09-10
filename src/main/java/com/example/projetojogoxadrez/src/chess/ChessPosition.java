package com.example.projetojogoxadrez.src.chess;

import com.example.projetojogoxadrez.src.boardgame.Position;

public class ChessPosition {
    private int column;
    private int row;

    public ChessPosition(int column, int row) {
        if(row < 1 || row > 8 || column < 1 || column > 8) {
            throw new ChessException("Erro instânciando ChessPosition. Valores validos são de 1 à 8.");
        }

        this.column = column;
        this.row = row;
    }

    protected Position toPosition() {
        return new Position(8 - row, column - 1);
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition(1 + position.getColumn(), 8 - position.getRow());
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return column + "-" + row;
    }
}
