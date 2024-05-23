package com.example.scannerproto.anlaysis.helpers.drone_utils;

import android.graphics.Color;

public enum FingerStatus {
    THUMB("Большой согнут", "Большой полусогнут", "Большой разогнут", Color.rgb(30, 100, 100), Color.rgb(70, 100, 100), Color.rgb(110, 100, 100)),
    INDEX("Указательный согнут", "Указательный полусогнут", "Указательный разогнут", Color.rgb(50, 50, 50), Color.rgb(50, 100, 50), Color.rgb(50, 150, 50)),
    MIDDLE("Средний согнут", "Средний полусогнут", "Средний разогнут", Color.rgb(100, 50, 50), Color.rgb(150, 50, 50), Color.rgb(200, 50, 50)),
    RING("Безымянный согнут", "Безымянный полусогнут", "Безымянный разогнут", Color.rgb(10, 100, 10), Color.rgb(10, 150, 10), Color.rgb(10, 200, 10)),
    PINKIE("Мизинец согнут", "Мизинец полусогнут", "Мизинец разогнут", Color.rgb(50, 50, 100), Color.rgb(50, 50, 150), Color.rgb(50, 50, 200));

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
        } else if (intensity == 16777216) {
            return closedText;
        }
        return halfOpenedText;
    }

    public int getColor(int intensity) {
        if (intensity == 0) {
            return openedColor;
        } else if (intensity == 16777216) {
            return closedColor;
        }
        return halfOpenedColor;
    }
}
