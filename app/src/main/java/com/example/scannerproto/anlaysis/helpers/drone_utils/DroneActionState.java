package com.example.scannerproto.anlaysis.helpers.drone_utils;

import android.graphics.Color;

public enum DroneActionState {
    ENABLED(Color.GREEN), DISABLED(Color.RED);
    private int color;

    DroneActionState(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
