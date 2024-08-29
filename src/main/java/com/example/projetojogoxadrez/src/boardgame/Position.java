package com.example.projetojogoxadrez.src.boardgame;

public class Position {
    private Integer row;
    private  Integer column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return row + ", " + column;
    }
}
