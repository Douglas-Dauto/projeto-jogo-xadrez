package com.example.projetojogoxadrez.src.chess;

import com.example.projetojogoxadrez.src.boardgame.BoardException;

public class ChessException extends BoardException {
    private static final long serialVersionUID = 1L;

    public ChessException(String msg) {
        super(msg);
    }
}
