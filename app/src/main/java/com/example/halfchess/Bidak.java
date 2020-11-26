package com.example.halfchess;

public class Bidak {
    // 1 : Pawn
    // 2 : Bishop
    // 3 : Knight
    // 4 : Queen
    // 5 : King
    private int value;
    private boolean white;

    public Bidak(int value, boolean white) {
        this.value = value;
        this.white = white;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
}
