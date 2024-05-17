package com.example.scannerproto.anlaysis.helpers.drone_utils;

public enum FingerStatus {
    THUMB("Большой согнут", "Большой разогнут", "Большой разогнут", 0, 0, 0),
    INDEX("Указательный согнут", "Указательный разогнут", "Указательный разогнут", 0, 0, 0),
    MIDDLE("Средний согнут", "Средний разогнут", "Средний разогнут", 0, 0, 0),
    RING("Безымянный согнут", "Безымянный разогнут", "Безымянный разогнут", 0, 0, 0),
    PINKIE("Мизинец согнут", "Мизинец разогнут", "Мизинец разогнут", 0, 0, 0);

    private String closedText;
    private String halfOpenedText;
    private String openedText;

    private int closedColor;
    private int halfOpenedColor;
    private int openedColor;

    FingerStatus(String closedText, String halfOpenedText, String openedText, int closedColor, int halfOpenedColor, int openedColor) {
        this.closedText = closedText;
        this.halfOpenedText = halfOpenedText;
        this.openedText = openedText;

        this.closedColor = closedColor;
        this.halfOpenedColor = halfOpenedColor;
        this.openedColor = openedColor;
    }

    public String getMessage(int intensity) {
        if (intensity == 0) {
            return openedText;
        } else if (intensity == 1) {
            return closedText;
        }
        return halfOpenedText;
    }

    public int getColor(int intensity) {
        if (intensity == 0) {
            return openedColor;
        } else if (intensity == 1) {
            return closedColor;
        }
        return halfOpenedColor;
    }
}
