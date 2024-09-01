package com.example.projetojogoxadrez.src.boardgame;

public class Board {
    private Integer rows;
    private  Integer columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro criando tabuleiro: é necessario que tenha pelo menos uma linha e uma coluna.");
        }

        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if(!positionExists(row, column)) {
            throw new BoardException("Posição não está no board.");
        }

        return pieces[row][column];
    }

    public Piece piece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Posição não está no board.");
        }

        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if(thereIsAPiece(position)) {
            throw new BoardException("Já tem uma peça na posição " + position);
        }

        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Posição não está no board.");
        }

        return piece(position) != null;
    }
}
